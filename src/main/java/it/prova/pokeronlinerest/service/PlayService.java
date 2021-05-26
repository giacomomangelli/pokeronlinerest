package it.prova.pokeronlinerest.service;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class PlayService {
	
	public Double play() {
		Random random = new Random(System.currentTimeMillis());
		Double segno = random.nextDouble();

		if (segno >= 0.5) {
			segno = 1D;
		} else {
			segno = -1D;
		}
		Double somma = (double) (Math.random() * 1000);
		Double totale = segno * somma;
		return totale;
	}
	
}
