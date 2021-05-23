package it.prova.pokeronlinerest.web.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronlinerest.model.Tavolo;
import it.prova.pokeronlinerest.service.tavolo.TavoloService;
import it.prova.pokeronlinerest.web.api.exception.TavoloNotFoundException;

@RestController
@RequestMapping("api/tavolo")
public class GestioneTavoloController {

	@Autowired
	private TavoloService tavoloServiceInstance;

	@GetMapping
	public List<Tavolo> listAllTavoli() {
		return tavoloServiceInstance.listAllTavoli();
	}

	@GetMapping("/{id}")
	public Tavolo findById(@PathVariable(value = "id", required = true) Long id) {
		Tavolo tavolo = tavoloServiceInstance.caricaSingoloTavoloEager(id);
		if (tavolo == null) {
			throw new TavoloNotFoundException("Tavolo not found " + id);
		}
		return tavolo;
	}

}
