package it.prova.pokeronlinerest.web.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TavoloNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TavoloNotFoundException(String message) {
		super(message);
	}

}
