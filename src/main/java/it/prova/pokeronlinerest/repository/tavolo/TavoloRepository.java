package it.prova.pokeronlinerest.repository.tavolo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.prova.pokeronlinerest.model.Tavolo;

public interface TavoloRepository extends CrudRepository<Tavolo, Long>, CustomTavoloRepository {
	
	@Query("select t from Tavolo t left join fetch t.utenti u left join fetch t.utenteCreato uc where t.id = ?1")
	Tavolo findByIdEager(Long id);

}
