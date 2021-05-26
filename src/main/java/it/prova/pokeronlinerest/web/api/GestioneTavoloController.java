package it.prova.pokeronlinerest.web.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
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

import it.prova.pokeronlinerest.model.Tavolo;
import it.prova.pokeronlinerest.model.Utente;
import it.prova.pokeronlinerest.security.jwt.JwtTokenUtil;
import it.prova.pokeronlinerest.service.tavolo.TavoloService;
import it.prova.pokeronlinerest.service.utente.UtenteService;
import it.prova.pokeronlinerest.validator.InsertTavoloValid;
import it.prova.pokeronlinerest.web.api.exception.OperationDeniedException;
import it.prova.pokeronlinerest.web.api.exception.TavoloNotFoundException;
import it.prova.pokeronlinerest.web.api.exception.UtenteNotAuthorizedException;
import it.prova.pokeronlinerest.web.api.exception.UtenteNotFoundException;

@RestController
@RequestMapping("api/tavolo")
public class GestioneTavoloController {

	@Autowired
	private TavoloService tavoloServiceInstance;
	@Autowired
	private UtenteService utenteServiceInstance;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<Tavolo> listTavoli(@RequestHeader("Authorization") String token) {
		Utente utente = utenteServiceInstance.findByUserName(jwtTokenUtil.getUserNameFromJwtToken(parseJwt(token)));
		if (utente.isSpecialPlayer()) {
			return tavoloServiceInstance.listAllTavoliByUtenteCreazione(utente);
		}
		return tavoloServiceInstance.listAllTavoli();
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Tavolo findById(@PathVariable(value = "id", required = true) Long id,
			@RequestHeader("Authorization") String token) {
		Utente utente = utenteServiceInstance.findByUserName(jwtTokenUtil.getUserNameFromJwtToken(parseJwt(token)));
		if (utente == null) {
			throw new UtenteNotFoundException("Utente not found in session");
		}
		Tavolo tavolo = tavoloServiceInstance.caricaTavoloByAuthorization(utente, id);
		if (tavolo == null) {
			throw new TavoloNotFoundException("Tavolo not found " + id);
		}
		return tavolo;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Tavolo createNewTavolo(@Validated(InsertTavoloValid.class) @RequestBody Tavolo tavoloInstance,
			@RequestHeader("Authorization") String token) {
		tavoloInstance.setUtenteCreazione(
				utenteServiceInstance.findByUserName(jwtTokenUtil.getUserNameFromJwtToken(parseJwt(token))));
		return tavoloServiceInstance.inserisci(tavoloInstance);
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Tavolo updateTavolo(@PathVariable Long id, @Valid @RequestBody Tavolo tavoloInstance,
			@RequestHeader("Authorization") String token) {
		Utente utente = utenteServiceInstance.findByUserName(jwtTokenUtil.getUserNameFromJwtToken(parseJwt(token)));
		Tavolo tavolo = tavoloServiceInstance.caricaSingoloTavoloEager(id);
		if (tavolo == null) {
			throw new TavoloNotFoundException("Tavolo not found " + id);
		}
		if (!tavolo.getUtenti().isEmpty()) {
			throw new OperationDeniedException("Vi sono ancora utenti attivi nel tavolo");
		}
		if (utente.isSpecialPlayer() && tavoloServiceInstance.caricaTavoloByUtenteCreazione(id, utente) == null) {
			throw new UtenteNotAuthorizedException("Utente not authorized with id: " + utente.getId());
		}
		tavoloInstance.setId(id);
		return tavoloServiceInstance.aggiorna(tavoloInstance);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteTavolo(@PathVariable Long id, @RequestHeader("Authorization") String token) {
		Utente utente = utenteServiceInstance.findByUserName(jwtTokenUtil.getUserNameFromJwtToken(parseJwt(token)));
		Tavolo tavolo = tavoloServiceInstance.caricaSingoloTavoloEager(id);
		if (tavolo == null) {
			throw new TavoloNotFoundException("Tavolo not found " + id);
		}
		if (!tavolo.getUtenti().isEmpty()) {
			throw new OperationDeniedException("Impossibile eliminare! Vi sono ancora utenti attivi nel tavolo");
		}
		if (utente.isSpecialPlayer() && tavoloServiceInstance.caricaTavoloByUtenteCreazione(id, utente) == null) {
			throw new UtenteNotAuthorizedException("Utente not authorized with id: " + utente.getId());
		}
		tavoloServiceInstance.rimuovi(tavolo);
	}

	@PostMapping("/search")
	@ResponseStatus(HttpStatus.OK)
	public List<Tavolo> searchTavolo(@RequestHeader("Authorization") String token, @RequestBody Tavolo tavoloExample) {

		Utente utente = utenteServiceInstance.findByUserName(jwtTokenUtil.getUserNameFromJwtToken(parseJwt(token)));
		if (utente.isAdmin()) {
			return tavoloServiceInstance.findByExample(tavoloExample);
		}
		tavoloExample.setUtenteCreazione(utente);
		return tavoloServiceInstance.findByExample(tavoloExample);
	}

	private String parseJwt(String token) {
		if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
			return token.substring(7, token.length());
		}
		return null;
	}

}
