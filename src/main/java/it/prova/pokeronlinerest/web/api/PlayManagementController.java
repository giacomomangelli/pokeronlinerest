package it.prova.pokeronlinerest.web.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronlinerest.model.Tavolo;
import it.prova.pokeronlinerest.model.Utente;
import it.prova.pokeronlinerest.security.jwt.JwtTokenUtil;
import it.prova.pokeronlinerest.service.PlayService;
import it.prova.pokeronlinerest.service.tavolo.TavoloService;
import it.prova.pokeronlinerest.service.utente.UtenteService;
import it.prova.pokeronlinerest.web.api.exception.CreditInvalidException;
import it.prova.pokeronlinerest.web.api.exception.ExperienceInvalidException;
import it.prova.pokeronlinerest.web.api.exception.TavoloNotFoundException;

@RestController
@RequestMapping("api/play")
public class PlayManagementController {

	@Autowired
	private UtenteService utenteServiceInstance;
	@Autowired
	private TavoloService tavoloServiceInstance;
	@Autowired
	private PlayService playService;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@PutMapping("/addCredito")
	public Utente compraCredito(@RequestHeader("Authorization") String token, @RequestBody Double credito) {

		if (credito < 0) {
			throw new CreditInvalidException("Not modified cause the credit " + credito + " is not valid");
		}
		Utente utente = utenteServiceInstance.findByUserName(jwtTokenUtil.getUserNameFromJwtToken(parseJwt(token)));

		utente.setCreditoAccumulato(utente.getCreditoAccumulato() + credito);
		return utenteServiceInstance.aggiorna(utente);
	}

	@GetMapping("/lastGame")
	public Tavolo getLastGame(@RequestHeader("Authorization") String token) {
		Utente utente = utenteServiceInstance.findByUserName(jwtTokenUtil.getUserNameFromJwtToken(parseJwt(token)));

		Tavolo tavolo = utente.getTavolo();
		if (tavolo == null) {
			throw new TavoloNotFoundException("Nessun tavolo attivo per utente con username: " + utente.getUsername());
		}
		return tavolo;
	}

	@PutMapping("/abbandonaPartita")
	@ResponseStatus(HttpStatus.OK)
	public Double quitGame(@RequestHeader("Authorization") String token) {
		Utente utente = utenteServiceInstance.findByUserName(jwtTokenUtil.getUserNameFromJwtToken(parseJwt(token)));
		if (utente.getTavolo() == null) {
			throw new TavoloNotFoundException("Nessun tavolo attivo per utente con username: " + utente.getUsername());
		}
		utente.setTavolo(null);
		utente.setEsperienzaAccumulata(utente.getEsperienzaAccumulata() + 1);
		utenteServiceInstance.aggiorna(utente);
		return utente.getEsperienzaAccumulata();
	}

	@GetMapping("/search")
	public List<Tavolo> getTavoliWithExpMin(@RequestHeader("Authorization") String token,
			@RequestBody Tavolo tavoloExample) {
		
		Utente utente = utenteServiceInstance.findByUserName(jwtTokenUtil.getUserNameFromJwtToken(parseJwt(token)));
		tavoloExample.setEsperienzaMin(utente.getEsperienzaAccumulata());
		return tavoloServiceInstance.findByExample(tavoloExample);
	}

	@PutMapping("/gioca")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> playGame(@RequestHeader("Authorization") String token, @RequestBody Long id) {
		
		Utente utente = utenteServiceInstance.findByUserName(jwtTokenUtil.getUserNameFromJwtToken(parseJwt(token)));

		Tavolo tavolo = tavoloServiceInstance.caricaSingoloTavoloEager(id);
		if (tavolo == null) {
			throw new TavoloNotFoundException("Tavolo not found con id: " + id);
		}
		if (utente.getEsperienzaAccumulata() < tavolo.getEsperienzaMin()) {
			throw new ExperienceInvalidException("L'esperienza non e' sufficiente a giocare la partita!");
		}
		if (utente.getCreditoAccumulato() < tavolo.getCifraMinima()) {
			utente.setTavolo(null);
			utenteServiceInstance.aggiorna(utente);
			throw new CreditInvalidException("Il credito non e' sufficiente a giocare la partita!");
		}

		String result = "La partita è stata ";
		
		Double risultatoPartita = playService.play();
		
		if (risultatoPartita >= 0) {
			result += "vinta.";
		} else {
			result += "persa.";
		}

		utente.setCreditoAccumulato(utente.getCreditoAccumulato() + risultatoPartita);

		if (utente.getCreditoAccumulato() < 0) {
			utente.setTavolo(null);
			utente.setCreditoAccumulato(0.0);
			utente.setEsperienzaAccumulata(utente.getEsperienzaAccumulata() + 1);
			utenteServiceInstance.aggiorna(utente);
			return ResponseEntity.status(HttpStatus.OK).body(result + " Il credito residuo è pari a: "
					+ utente.getCreditoAccumulato() + ". Sei stato rimosso dal tavolo.");
		}

		tavoloServiceInstance.aggiungiEdAggiornaPlayerAlTavolo(utente, tavolo);
		return ResponseEntity.status(HttpStatus.OK)
				.body(result + " Il credito residuo è pari a: " + utente.getCreditoAccumulato());
	}

	private String parseJwt(String token) {
		if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
			return token.substring(7, token.length());
		}
		return null;
	}
}
