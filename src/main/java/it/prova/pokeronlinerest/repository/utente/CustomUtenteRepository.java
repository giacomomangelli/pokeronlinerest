package it.prova.pokeronlinerest.repository.utente;

import java.util.List;

import it.prova.pokeronlinerest.model.Utente;

public interface CustomUtenteRepository {
	
	List<Utente> findByExample(Utente utenteInstance);

}
