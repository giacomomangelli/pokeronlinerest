package it.prova.pokeronlinerest.repository.tavolo;

import java.util.List;

import it.prova.pokeronlinerest.model.Tavolo;

public interface CustomTavoloRepository {

	List<Tavolo> findByExample(Tavolo exampleInstance);
	
}
