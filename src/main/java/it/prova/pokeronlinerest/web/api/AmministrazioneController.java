package it.prova.pokeronlinerest.web.api;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronlinerest.model.StatoUtente;
import it.prova.pokeronlinerest.model.Utente;
import it.prova.pokeronlinerest.service.utente.UtenteService;
import it.prova.pokeronlinerest.web.api.exception.UtenteNotFoundException;

@RestController
@RequestMapping("api/amministrazione")
public class AmministrazioneController {

	@Autowired
	private UtenteService utenteServiceInstance;

	@GetMapping
	public List<Utente> listAll() {
		return utenteServiceInstance.listAllUtenti();
	}

	@GetMapping("/{id}")
	public Utente findById(@PathVariable(value = "id", required = true) Long id,
			@RequestHeader("Authorization") String message) {
		Utente utente = utenteServiceInstance.caricaSingoloUtenteEager(id);
		if (utente == null) {
			throw new UtenteNotFoundException("Utente not found with id: " + id);
		}
		return utente;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Utente createNewUtente(@Valid @RequestBody Utente utenteInstance) {
		utenteInstance.setDataRegistrazione(new Date());
		utenteInstance.setStato(StatoUtente.ATTIVO);
		;
		return utenteServiceInstance.inserisci(utenteInstance);
	}

	@PutMapping("/{id}")
	public Utente updateUtente(@PathVariable(value = "id", required = true) Long id,
			@Valid @RequestBody Utente utenteInstance) {
		Utente utente = utenteServiceInstance.caricaSingoloUtenteEager(id);
		if (utente == null) {
			throw new UtenteNotFoundException("Utente not found with id: " + id);
		}
		utenteInstance.setId(id);
		return utenteServiceInstance.aggiorna(utenteInstance);

	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteUtente(@PathVariable(value = "id", required = true) Long id) {
		Utente utente = utenteServiceInstance.caricaSingoloUtente(id);
		if (utente == null) {
			throw new UtenteNotFoundException("Utente not found with id: " + id);
		}
		utenteServiceInstance.invertUserAbilitation(utente.getId());
	}

	@PostMapping("/search")
	public List<Utente> searchUtente(Utente utenteExample) {
		return utenteServiceInstance.findByExample(utenteExample);
	}

}
