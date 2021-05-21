package it.prova.pokeronlinerest.service.tavolo;

import java.util.List;

import it.prova.pokeronlinerest.model.Tavolo;

public interface TavoloService {

	public List<Tavolo> listAllTavoli();

	public Tavolo caricaSingoloTavolo(Long id);

	public Tavolo aggiorna(Tavolo tavoloInstance);

	public Tavolo inserisci(Tavolo tavoloInstance);

	public void rimuovi(Tavolo tavoloInstance);

	public List<Tavolo> findByExample(Tavolo tavoloExemple);

	public Tavolo caricaSingoloTavoloEager(Long id);
	
}
