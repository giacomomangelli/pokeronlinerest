package it.prova.pokeronlinerest.web.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronlinerest.model.Tavolo;
import it.prova.pokeronlinerest.model.Utente;
import it.prova.pokeronlinerest.service.tavolo.TavoloService;
import it.prova.pokeronlinerest.service.utente.UtenteService;
import it.prova.pokeronlinerest.web.api.exception.CreditInvalidException;
import it.prova.pokeronlinerest.web.api.exception.TavoloNotFoundException;
import it.prova.pokeronlinerest.web.api.exception.UtenteNotAuthorizedException;

@RestController
@RequestMapping("api/play")
public class PlayManagementController {

	@Autowired
	private UtenteService utenteServiceInstance;
	@Autowired
	private TavoloService tavoloServiceInstance;

	@PutMapping("/addCredito")
	public Utente compraCredito(@RequestHeader("Authorization") String message, @RequestBody Double credito) {
		if (credito < 0) {
			throw new CreditInvalidException("Not modified cause the credit " + credito + " is not valid");
		}
		Utente utente = utenteServiceInstance.findByUserName(message);
		if (utente == null || !utente.isAttivo()) {
			throw new UtenteNotAuthorizedException("utente not authorized with username: " + message);
		}
		utente.setCreditoAccumulato(utente.getCreditoAccumulato() + credito);
		return utenteServiceInstance.aggiorna(utente);
	}

	@GetMapping("/lastGame")
	public Tavolo getLastGame(@RequestHeader("Authorization") String message) {
		Utente utente = utenteServiceInstance.findByUserName(message);
		if (utente == null || !utente.isAttivo()) {
			throw new UtenteNotAuthorizedException("utente not authorized with username: " + message);
		}
		Tavolo tavolo = utente.getTavolo();
		if (tavolo == null) {
			throw new TavoloNotFoundException("Nessun tavolo attivo per utente con id: " + message);
		}
		return tavolo;
	}

	@PutMapping("/abbandonaPartita")
	@ResponseStatus(HttpStatus.OK)
	public Double quitGame(@RequestHeader("Authorization") String message) {
		Utente utente = utenteServiceInstance.findByUserName(message);
		if(utente == null || !utente.isAttivo()) {
			throw new UtenteNotAuthorizedException("utente not authorized with username: " + message);
		}
		if(utente.getTavolo()==null) {
			throw new TavoloNotFoundException("Nessun tavolo attivo per utente con id: " + message);
		}
		utente.setTavolo(null);
		utente.setEsperienzaAccumulata(utente.getEsperienzaAccumulata()+1);
		utenteServiceInstance.aggiorna(utente);
		return utente.getEsperienzaAccumulata();
	}

	@PutMapping("/gioca")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> playGame(@RequestHeader("Authorization") String message, @RequestBody Long id) {
		Utente utente = utenteServiceInstance.findByUserName(message);
		if(utente == null || !utente.isAttivo()) {
			throw new UtenteNotAuthorizedException("utente not authorized with username: " + message);
		}
		if(utente.getCreditoAccumulato()<10) {
			throw new CreditInvalidException("Il credito non e' sufficiente a giocare la partita!");
		}
		
		return ResponseEntity.status(null).body(null);
	}
	
	@GetMapping("/search")
	public List<Tavolo> getTavoliWithExpMin(@RequestHeader("Authorization") String message, @RequestBody Tavolo tavoloExample){
		Utente utente = utenteServiceInstance.findByUserName(message);
//		if(utente.isAdmin()) {
//			return tavoloServiceInstance.findByExample(tavoloExample);
//		}
//		CheckUtenteAuthorization.checkAthorizationOfSpecialPlayerWithTable(message, utente, tavoloExample);
		tavoloExample.setEsperienzaMin(utente.getEsperienzaAccumulata());
		return tavoloServiceInstance.findByExample(tavoloExample);
	}
}
