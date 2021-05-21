package it.prova.pokeronlinerest.repository.utente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;

import it.prova.pokeronlinerest.model.Utente;

public class CustomUtenteRepositoryImpl implements CustomUtenteRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Utente> findByExample(Utente utenteInstance) {

		List<String> whereClauses = new ArrayList<>();
		Map<String, Object> parameterMap = new HashMap<>();

		StringBuilder queryBuilder = new StringBuilder(
				"select u from Utente u left join fetch u.ruoli r left join fetch u.tavoli t where u.id = u.id");

		if (StringUtils.isNotBlank(utenteInstance.getNome())) {
			whereClauses.add(" u.nome like :nome ");
			parameterMap.put("nome", "%" + utenteInstance.getNome() + "%");
		}
		if (StringUtils.isNotBlank(utenteInstance.getCognome())) {
			whereClauses.add(" u.cognome like :cognome ");
			parameterMap.put("cognome", "%" + utenteInstance.getCognome() + "%");
		}
		if (StringUtils.isNotBlank(utenteInstance.getUsername())) {
			whereClauses.add(" u.username like :username ");
			parameterMap.put("username", "%" + utenteInstance.getUsername() + "%");
		}
		if (utenteInstance.getDataRegistrazione() != null) {
			whereClauses.add(" u.dataRegistrazione >= :dataRegistrazione ");
			parameterMap.put("dataRegistrazione", utenteInstance.getDataRegistrazione());
		}
		if (utenteInstance.getCreditoAccumulato() != null && utenteInstance.getCreditoAccumulato() >= 0) {
			whereClauses.add(" u.creditoAccumulato >= :creditoAccumulato ");
			parameterMap.put("creditoAccumulato", utenteInstance.getCreditoAccumulato());
		}
		if (utenteInstance.getRuoli() != null && !utenteInstance.getRuoli().isEmpty()) {
			whereClauses.add(" r in :ruoli ");
			parameterMap.put("ruoli", utenteInstance.getRuoli());
		}
		if (utenteInstance.getStato() != null) {
			whereClauses.add(" u.stato = :stato ");
			parameterMap.put("stato", utenteInstance.getStato());
		}
		if (utenteInstance.getTavolo() != null ) {
			whereClauses.add(" t = :tavolo ");
			parameterMap.put("tavolo", utenteInstance.getTavolo());
		}

		queryBuilder.append(!whereClauses.isEmpty() ? " and " : "");
		queryBuilder.append(StringUtils.join(whereClauses, " and "));
		TypedQuery<Utente> typedQuery = entityManager.createQuery(queryBuilder.toString(), Utente.class);

		for (String key : parameterMap.keySet()) {
			typedQuery.setParameter(key, parameterMap.get(key));
		}

		return typedQuery.getResultList();

	}

}
