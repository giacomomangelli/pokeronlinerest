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

import org.apache.commons.lang3.builder.EqualsExclude;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import it.prova.pokeronlinerest.validator.InsertTavoloValid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"}, ignoreUnknown = true)
@Entity
@Table(name = "tavolo")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Tavolo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotNull(message = "{esperienzaMin.notnull}", groups = InsertTavoloValid.class)
	@DecimalMin("0.0")
	@Column(name = "esperienza_min")
	private Double esperienzaMin;

	@NotNull(message = "{cifraMinima.notnull}", groups = InsertTavoloValid.class)
	@DecimalMin("0.0")
	@Column(name = "cifra_minima")
	private Double cifraMinima;

	@NotBlank(message = "{denominazione.notblank}", groups = InsertTavoloValid.class)
	@Column(name = "denominazione")
	private String denominazione;

	@NotNull(message = "{dataCreazione.notnull}")
	@Column(name = "data_creazione")
	private Date dataCreazione;

	@JsonIgnoreProperties(value = { "tavolo", "ruoli" })
	@NotNull(message = "{utenteCreazione.notnull}")
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "utente_creazione_id")
	private Utente utenteCreazione;

	@JsonIgnoreProperties(value = { "tavolo" , "ruoli" })
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tavolo")
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@EqualsExclude
	private Set<Utente> utenti = new HashSet<>(0);

}