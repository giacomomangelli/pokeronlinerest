package it.prova.pokeronlinerest.repository.tavolo;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.prova.pokeronlinerest.model.Tavolo;
import it.prova.pokeronlinerest.model.Utente;

public interface TavoloRepository extends CrudRepository<Tavolo, Long>, CustomTavoloRepository {
	
	@Query("select t from Tavolo t left join fetch t.utenti u left join fetch t.utenteCreazione uc where t.id = ?1")
	Tavolo findByIdEager(Long id);
	
	Optional<Tavolo> findByIdAndUtenteCreazione(Long id, Utente utenteCreazione);

}
