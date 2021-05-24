package it.prova.pokeronlinerest.web.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
public class OperationDeniedException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public OperationDeniedException(String message) {
		super(message);
	}


	
	
}
