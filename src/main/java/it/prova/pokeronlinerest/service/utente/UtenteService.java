package it.prova.pokeronlinerest.service.utente;

import java.util.List;

import it.prova.pokeronlinerest.model.Ruolo;
import it.prova.pokeronlinerest.model.Utente;

public interface UtenteService {

	public List<Utente> listAllUtenti();

	public Utente caricaSingoloUtente(Long id);

	public Utente aggiorna(Utente utenteInstance);

	public Utente inserisci(Utente utenteInstance);

	public void rimuovi(Utente utenteInstance);

	public List<Utente> findByExample(Utente utenteExample);

	public Utente findByUserName(String usernameInput);

	public void invertUserAbilitation(Long utenteInstanceId);

	public List<Utente> findByCognomeNomeRuolo(String cognome, String nome, Ruolo ruolo);
	
	public Utente caricaSingoloUtenteEager(Long id);

}
