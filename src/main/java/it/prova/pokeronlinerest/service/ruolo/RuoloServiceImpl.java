package it.prova.pokeronlinerest.service.ruolo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.pokeronlinerest.model.Ruolo;
import it.prova.pokeronlinerest.repository.ruolo.RuoloRepository;

@Service
public class RuoloServiceImpl implements RuoloService {
	
	@Autowired
	private RuoloRepository repository;

	@Transactional(readOnly = true)
	public List<Ruolo> listAllRuoli() {
		return (List<Ruolo>) repository.findAll();
	}

	@Transactional(readOnly = true)
	public Ruolo caricaSingoloElemento(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Transactional
	public void aggiorna(Ruolo ruoloInstance) {
		repository.save(ruoloInstance);
	}

	@Transactional
	public void inserisci(Ruolo ruoloInstance) {
		repository.save(ruoloInstance);
	}

	@Transactional
	public void rimuovi(Ruolo ruoloInstance) {
		repository.delete(ruoloInstance);
	}

	@Transactional(readOnly = true)
	public Ruolo cercaPerDescrizioneECodice(String descrizione, String codice) {
		return repository.findByDescrizioneAndCodice(descrizione, codice);
	}

}
