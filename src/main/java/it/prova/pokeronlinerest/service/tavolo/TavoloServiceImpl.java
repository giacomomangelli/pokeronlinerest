package it.prova.pokeronlinerest.service.tavolo;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.pokeronlinerest.model.Tavolo;
import it.prova.pokeronlinerest.model.Utente;
import it.prova.pokeronlinerest.repository.tavolo.TavoloRepository;
import it.prova.pokeronlinerest.service.utente.UtenteService;
import it.prova.pokeronlinerest.web.api.exception.UtenteNotAuthorizedException;

@Service
public class TavoloServiceImpl implements TavoloService {

	@Autowired
	private TavoloRepository repository;
	@Autowired
	private UtenteService utenteServiceInstance;

	@Transactional(readOnly = true)
	public List<Tavolo> listAllTavoli() {
		return (List<Tavolo>) repository.findAll();
	}

	@Transactional(readOnly = true)
	public Tavolo caricaSingoloTavolo(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Transactional
	public Tavolo aggiorna(Tavolo tavoloInstance) {
		return repository.save(tavoloInstance);
	}

	@Transactional
	public Tavolo inserisci(Tavolo tavoloInstance) {
		tavoloInstance.setDataCreazione(new Date());
		return repository.save(tavoloInstance);
	}

	@Transactional
	public void rimuovi(Tavolo tavoloInstance) {
		repository.delete(tavoloInstance);
	}

	@Transactional(readOnly = true)
	public List<Tavolo> findByExample(Tavolo tavoloExemple) {
		return repository.findByExample(tavoloExemple);
	}

	@Transactional(readOnly = true)
	public Tavolo caricaSingoloTavoloEager(Long id) {
		return repository.findByIdEager(id);
	}

	@Transactional(readOnly = true)
	public Tavolo caricaTavoloByAuthorization(Utente utente, Long idTavolo) {
		Utente utenteAuth = utenteServiceInstance.caricaSingoloUtenteEager(utente.getId());
		if (utenteAuth.isPlayer()) {
			throw new UtenteNotAuthorizedException("Utente not authorized for displaying table with id: " + idTavolo);
		}
		Tavolo tavolo = repository.findByIdEager(idTavolo);
		if (utenteAuth.isAdmin()) {
			return tavolo;
		} else if (utente.isSpecialPlayer() && tavolo.getUtenteCreazione().equals(utente)) {
			return tavolo;
		} else {
			throw new UtenteNotAuthorizedException("Utente not authorized for displaying table with id: " + idTavolo);
		}

	}

	@Transactional(readOnly = true)
	public Tavolo caricaTavoloByUtenteCreazione(Long id, Utente utente) {
		return repository.findByIdAndUtenteCreazione(id, utente).orElse(null);
	}

	@Transactional
	public Tavolo aggiungiEdAggiornaPlayerAlTavolo(Utente utente, Tavolo tavolo) {
		if (utente.getTavolo()!= null && utente.getTavolo().equals(tavolo)) {
			utenteServiceInstance.aggiorna(utente);
			return tavolo;
		}
		tavolo.getUtenti().add(utente);
		utente.setTavolo(tavolo);
		utenteServiceInstance.aggiorna(utente);
		return aggiorna(tavolo);
	}

	@Transactional(readOnly = true)
	public List<Tavolo> listAllTavoliByUtenteCreazione(Utente utente) {
		return repository.findByUtenteCreazione(utente);
	}

}
