package it.prova.pokeronlinerest.service.utente;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.pokeronlinerest.model.Ruolo;
import it.prova.pokeronlinerest.model.StatoUtente;
import it.prova.pokeronlinerest.model.Utente;
import it.prova.pokeronlinerest.repository.utente.UtenteRepository;
import it.prova.pokeronlinerest.service.ruolo.RuoloService;
import it.prova.pokeronlinerest.web.api.exception.UtenteNotFoundException;

@Service
public class UtenteServiceImpl implements UtenteService {

	@Autowired
	private UtenteRepository repository;
	@Autowired
	private RuoloService ruoloService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional(readOnly = true)
	public List<Utente> listAllUtenti() {
		return (List<Utente>) repository.findAll();
	}

	@Transactional(readOnly = true)
	public Utente caricaSingoloUtente(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Transactional
	public Utente aggiorna(Utente utenteInstance) {
		return repository.save(utenteInstance);
	}

	@Transactional
	public Utente inserisci(Utente utenteInstance) {
		if (utenteInstance == null) {
			throw new RuntimeException("Errore nell'inserimento dell'utente.");
		}
		utenteInstance.setPassword(passwordEncoder.encode(utenteInstance.getPassword()));
		utenteInstance.setDataRegistrazione(new Date());
		utenteInstance.setStato(StatoUtente.ATTIVO);
		return repository.save(utenteInstance);
	}

	@Transactional
	public void rimuovi(Utente utenteInstance) {
		repository.delete(utenteInstance);
	}

	@Transactional(readOnly = true)
	public List<Utente> findByExample(Utente utenteExample) {
		return repository.findByExample(utenteExample);
	}

	@Transactional(readOnly = true)
	public Utente findByUserName(String username) {
		return repository.findByUsername(username).orElse(null);
	}

	@Transactional
	public void invertUserAbilitation(Long utenteInstanceId) {
		if (utenteInstanceId == null || utenteInstanceId < 1) {
			throw new RuntimeException("Input invalido.");
		}
		Utente utenteInstance = caricaSingoloUtente(utenteInstanceId);
		if (utenteInstance == null) {
			throw new UtenteNotFoundException("Utente not found with id: " + utenteInstanceId);
		}
		if (utenteInstance.isAdmin() && repository
				.findByRuoliAndStato(ruoloService.cercaPerDescrizioneECodice("Administrator", "ROLE_ADMIN"),
						StatoUtente.ATTIVO)
				.size() == 1) {
			throw new RuntimeException("Impossibile eliminare l'ultimo Admin!!!");

		}
		if (utenteInstance.getStato().equals(StatoUtente.ATTIVO)) {
			utenteInstance.setStato(StatoUtente.DISABILITATO);
		} else if (utenteInstance.getStato().equals(StatoUtente.DISABILITATO)) {
			utenteInstance.setStato(StatoUtente.ATTIVO);
		}
	}

	@Transactional(readOnly = true)
	public List<Utente> findByCognomeNomeRuolo(String cognome, String nome, Ruolo ruolo) {
		return repository.findByCognomeIgnoreCaseContainingAndNomeIgnoreCaseContainingAndRuoli(cognome, nome, ruolo);
	}

	@Transactional(readOnly = true)
	public Utente caricaSingoloUtenteEager(Long id) {
		return repository.findByIdEager(id).orElse(null);
	}

}
