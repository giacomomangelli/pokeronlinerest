package it.prova.pokeronlinerest.service.tavolo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.pokeronlinerest.model.Tavolo;
import it.prova.pokeronlinerest.repository.tavolo.TavoloRepository;

@Service
public class TavoloServiceImpl implements TavoloService {

	@Autowired
	private TavoloRepository repository;
	
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

	@Override
	public Tavolo caricaSingoloTavoloEager(Long id) {
		return repository.findByIdEager(id);
	}
	
	
	

}
