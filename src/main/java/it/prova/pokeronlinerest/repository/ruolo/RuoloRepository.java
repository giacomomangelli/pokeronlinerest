package it.prova.pokeronlinerest.repository.ruolo;

import org.springframework.data.repository.CrudRepository;

import it.prova.pokeronlinerest.model.Ruolo;

public interface RuoloRepository extends CrudRepository<Ruolo, Long> {

	Ruolo findByDescrizioneAndCodice(String descrizione, String codice);

}
