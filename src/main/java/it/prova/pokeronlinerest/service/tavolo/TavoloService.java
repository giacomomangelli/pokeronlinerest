package it.prova.pokeronlinerest.service.tavolo;

import java.util.List;

import it.prova.pokeronlinerest.model.Tavolo;
import it.prova.pokeronlinerest.model.Utente;

public interface TavoloService {

	public List<Tavolo> listAllTavoli();

	public Tavolo caricaSingoloTavolo(Long id);

	public Tavolo aggiorna(Tavolo tavoloInstance);

	public Tavolo inserisci(Tavolo tavoloInstance);

	public void rimuovi(Tavolo tavoloInstance);

	public List<Tavolo> findByExample(Tavolo tavoloExemple);

	public Tavolo caricaSingoloTavoloEager(Long id);
	
	public Tavolo caricaTavoloByAuthorization(Utente utente, Long idTavolo);
	
	public Tavolo caricaTavoloByUtenteCreazione(Long id, Utente utente);
	
	public List<Tavolo> listAllTavoliByUtenteCreazione(Utente utente);
	
	public Tavolo aggiungiEdAggiornaPlayerAlTavolo(Utente utente, Tavolo tavolo);
	
}
