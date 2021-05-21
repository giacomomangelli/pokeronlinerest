package it.prova.pokeronlinerest.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ruolo")
@Data
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Ruolo {

	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_SPECIAL_PLAYER = "ROLE_SPECIAL_PLAYER";
	public static final String ROLE_PLAYER = "ROLE_PLAYER";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotBlank(message = "descrizione.notblank")
	@Column(name = "descrizione")
	private String descrizione;

	@NotBlank(message = "{codice.notblank}")
	@Column(name = "codice")
	private String codice;

	public Ruolo(@NotBlank String descrizione, @NotBlank String codice) {
		super();
		this.descrizione = descrizione;
		this.codice = codice;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Ruolo other = (Ruolo) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

}
