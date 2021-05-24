package it.prova.pokeronlinerest.utility;

import it.prova.pokeronlinerest.model.Tavolo;
import it.prova.pokeronlinerest.model.Utente;
import it.prova.pokeronlinerest.web.api.exception.TavoloNotFoundException;
import it.prova.pokeronlinerest.web.api.exception.UtenteNotAuthorizedException;
import it.prova.pokeronlinerest.web.api.exception.UtenteNotFoundException;

public class CheckUtenteAuthorization {

	public static void checkAuthorizationAdmin(String username, Utente utenteAuth) {
		if(utenteAuth == null) {
			throw new UtenteNotFoundException("Utente not found in session");
		}
		if(!utenteAuth.isAdmin()) {
			throw new UtenteNotAuthorizedException("Utente non autorizzato con username: " + username);
		}
	}
	
	public static void checkAuthorizationAdminOrSpecialPlayer(String username, Utente utenteAuth) {
		if(utenteAuth == null) {
			throw new UtenteNotFoundException("Utente not found in session");
		}
		if(!utenteAuth.isAdmin() && !utenteAuth.isSpecialPlayer()) {
			throw new UtenteNotAuthorizedException("Utente non autorizzato con username: " + username);
		}
	}
	
	public static void checkAthorizationOfSpecialPlayerWithTable(String username, Utente utente, Tavolo tavolo) {
		if(utente == null) {
			throw new UtenteNotFoundException("Utente not found in session");
		}
		if (tavolo == null) {
			throw new TavoloNotFoundException("Tavolo not found");
		}
		if(!utente.isSpecialPlayer()) {
			throw new UtenteNotAuthorizedException("Utente not authorized with username: " + username);
		}
		if(utente.isSpecialPlayer() && !tavolo.getUtenteCreazione().equals(utente)) {
			throw new UtenteNotAuthorizedException("Utente not authorized with username: " + username);
		}
	}
	
}
