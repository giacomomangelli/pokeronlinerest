package it.prova.pokeronlinerest.security.jwt.dto;

import java.util.Collection;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import it.prova.pokeronlinerest.model.StatoUtente;
import it.prova.pokeronlinerest.model.Utente;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JwtUserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;
	private final String username;
	private final String password;
	private final String email;
	private final Collection<? extends GrantedAuthority> authorities;
	private final StatoUtente stato;

	public JwtUserDetailsImpl(String username, String password, String email, Collection<? extends GrantedAuthority> authorities,
			StatoUtente statoUtente) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.authorities = authorities;
		this.stato = statoUtente;
	}
	
	public static JwtUserDetailsImpl build(Utente user) {
		Collection<GrantedAuthority> authorities = getAuthorities(user);
		
        return new JwtUserDetailsImpl(
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                authorities,
                user.getStato()
        );
    }
	
	private static Collection<GrantedAuthority> getAuthorities(Utente user) {
		String[] userRoles = user.getRuoli().stream().map((role) -> role.getCodice()).toArray(String[]::new);
		Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userRoles);
		return authorities;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		JwtUserDetailsImpl user = (JwtUserDetailsImpl) o;
		return Objects.equals(username, user.username);
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isEnabled() {
		return getStato().equals(StatoUtente.ATTIVO)?true:false;
	}

}