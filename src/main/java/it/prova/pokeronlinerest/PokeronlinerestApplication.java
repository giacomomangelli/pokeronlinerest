package it.prova.pokeronlinerest;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import it.prova.pokeronlinerest.model.Ruolo;
import it.prova.pokeronlinerest.model.StatoUtente;
import it.prova.pokeronlinerest.model.Utente;
import it.prova.pokeronlinerest.service.ruolo.RuoloService;
import it.prova.pokeronlinerest.service.utente.UtenteService;

@SpringBootApplication
public class PokeronlinerestApplication implements CommandLineRunner{
	
	@Autowired
	private RuoloService ruoloServiceInstance;
	@Autowired
	private UtenteService utenteServiceInstance;
	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(PokeronlinerestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		if (ruoloServiceInstance.cercaPerDescrizioneECodice("Administrator", "ROLE_ADMIN") == null) {
			ruoloServiceInstance.inserisci(new Ruolo("Administrator", "ROLE_ADMIN"));
		}

		if (ruoloServiceInstance.cercaPerDescrizioneECodice("Special Player", "ROLE_SPECIAL_PLAYER") == null) {
			ruoloServiceInstance.inserisci(new Ruolo("Special Player", "ROLE_SPECIAL_PLAYER"));
		}
		
		if (ruoloServiceInstance.cercaPerDescrizioneECodice("Player", "ROLE_PLAYER") == null) {
			ruoloServiceInstance.inserisci(new Ruolo("Player", "ROLE_PLAYER"));
		}

		if (utenteServiceInstance.findByUserName("admin") == null) {
			Utente admin = new Utente("Mario", "Rossi", "admin", passwordEncoder.encode("admin"), "giacomo.mang@tiscali.it" ,new Date());
			admin.getRuoli().add(ruoloServiceInstance.cercaPerDescrizioneECodice("Administrator", "ROLE_ADMIN"));
			admin.setStato(StatoUtente.ATTIVO);
			admin.setCreditoAccumulato(0.0);
			admin.setEsperienzaAccumulata(0.0);
			utenteServiceInstance.inserisci(admin);
		}
		
	}

}
