package it.prova.pokeronlinerest.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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
@Table(name = "utente")
@Data 
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Utente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotBlank(message = "{nome.notblank}")
	@Column(name = "nome")
	private String nome;

	@NotBlank(message = "{cognome.notblank}")
	@Column(name = "cognome")
	private String cognome;

	@NotBlank(message = "{username.notblank}")
	@Column(name = "username", unique = true)
	private String username;

	@NotBlank(message = "{password.notblank}")
	@Column(name = "password")
	private String password;

	@NotNull(message = "{dataRegistrazione.notnull}")
	@Column(name = "data_registrazione")
	private Date dataRegistrazione;

	@NotNull(message = "{creditoAccumulato.notnull}")
	@DecimalMin("0.0")
	@Column(name = "credito_accumulato")
	private Double creditoAccumulato;
	
	@NotNull(message = "{esperienzaAccumulata.notnull}")
	@DecimalMin("0.0")
	@Column(name = "esperienza_accumulata")
	private Double esperienzaAccumulata;
	
	@NotNull(message = "{stato.notnull}")
	@Enumerated(EnumType.STRING)
	private StatoUtente stato = StatoUtente.ATTIVO;

	@NotEmpty(message = "{ruoli.notempty}")
	@ManyToMany
	@JoinTable(name = "utente_ruolo", joinColumns = @JoinColumn(name = "utente_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "ruolo_id", referencedColumnName = "id"))
	private Set<Ruolo> ruoli = new HashSet<>(0);
	
	@JsonIgnoreProperties(value= {"utenti", "utenteCreazione"})
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tavolo_id")
	private Tavolo tavolo;

	public boolean isAdmin() {
		for (Ruolo ruoloItem : ruoli) {
			if (ruoloItem.getCodice().equals(Ruolo.ROLE_ADMIN))
				return true;
		}
		return false;
	}

	public boolean isAttivo() {
		return this.stato.equals(StatoUtente.ATTIVO);
	}

	public Utente(@NotBlank(message = "{nome.notblank}") String nome,
			@NotBlank(message = "{cognome.notblank}") String cognome,
			@NotBlank(message = "{username.notblank}") String username,
			@NotBlank(message = "{password.notblank}") String password,
			@NotNull(message = "{dataRegistrazione.notnull}") Date dataRegistrazione) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.username = username;
		this.password = password;
		this.dataRegistrazione = dataRegistrazione;
	}

	
}
