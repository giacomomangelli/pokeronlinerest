package it.prova.pokeronlinerest.security.service;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import it.prova.pokeronlinerest.model.Utente;
import it.prova.pokeronlinerest.repository.utente.UtenteRepository;
import it.prova.pokeronlinerest.security.jwt.dto.JwtUserDetailsImpl;


@Service
@Transactional
public class JwtUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UtenteRepository utenteRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Utente user = utenteRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));

		return JwtUserDetailsImpl.build(user);

	}

	public static Collection<GrantedAuthority> getAuthorities(Utente user) {
		String[] userRoles = user.getRuoli().stream().map((role) -> role.getCodice()).toArray(String[]::new);
		Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userRoles);
		return authorities;
	}

}
