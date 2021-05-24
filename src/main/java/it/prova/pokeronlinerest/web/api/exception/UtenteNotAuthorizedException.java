package it.prova.pokeronlinerest.web.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UtenteNotAuthorizedException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public UtenteNotAuthorizedException(String message) {
		super(message);
	}

	
}
