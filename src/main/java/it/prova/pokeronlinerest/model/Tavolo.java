package it.prova.pokeronlinerest.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "tavolo")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tavolo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotNull(message = "{esperienzaMin.notnull}")
	@DecimalMin("0.0")
	@Column(name = "esperienza_min")
	private Double esperienzaMin;

	@NotNull(message = "{cifraMinima.notnull}")
	@DecimalMin("0.0")
	@Column(name = "cifra_minima")
	private Double cifraMinima;

	@NotBlank(message = "{denominazione.notblank}")
	@Column(name = "denominazione")
	private String denominazione;

	@NotNull(message = "{dataCreazione.notnull}")
	@Column(name = "data_creazione")
	private Date dataCreazione;

	@JsonIgnoreProperties(value = { "tavolo" })
	@NotNull(message = "{utenteCreazione.notnull}")
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "utente_creazione_id")
	private Utente utenteCreazione;

	@JsonIgnoreProperties(value = { "tavolo" })
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tavolo")
	private Set<Utente> utenti = new HashSet<>(0);

}