package it.prova.pokeronlinerest.repository.tavolo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;

import it.prova.pokeronlinerest.model.Tavolo;

public class CustomTavoloRepositoryImpl implements CustomTavoloRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<Tavolo> findByExample(Tavolo exampleInstance) {
		
		List<String> whereClauses = new ArrayList<>();
		Map<String, Object> parameterMap = new HashMap<>();

		StringBuilder queryBuilder = new StringBuilder("select t from Tavolo t left join fetch t.utenti u left join fetch t.utenteCreazione uc where u.id = u.id");

		if (StringUtils.isNotBlank(exampleInstance.getDenominazione())) {
			whereClauses.add(" t.denominazione like :denominazione ");
			parameterMap.put("nome", "%" + exampleInstance.getDenominazione() + "%");
		}
		if (exampleInstance.getCifraMinima()!=null && exampleInstance.getCifraMinima() >= 0) {
			whereClauses.add(" t.cifraMinima >= :cifraMinima ");
			parameterMap.put("cifraMinima", exampleInstance.getCifraMinima());
		}
		if (exampleInstance.getEsperienzaMin()!=null && exampleInstance.getEsperienzaMin() >= 0) {
			whereClauses.add(" t.esperienzaMin >= :esperienzaMin ");
			parameterMap.put("esperienzaMin", exampleInstance.getEsperienzaMin());
		}
		if (exampleInstance.getUtenti() != null && !exampleInstance.getUtenti().isEmpty()) {
			whereClauses.add(" u in :utenti ");
			parameterMap.put("ruoli", exampleInstance.getUtenti());
		}
		if (exampleInstance.getDataCreazione() != null) {
			whereClauses.add(" t.dataCreazione >= :dataCreazione ");
			parameterMap.put("dataCreazione", exampleInstance.getDataCreazione());
		}
		if (exampleInstance.getUtenteCreazione() != null ) {
			whereClauses.add(" uc = :utenteCreazione ");
			parameterMap.put("utenteCreazione", exampleInstance.getUtenteCreazione());
		}

		queryBuilder.append(!whereClauses.isEmpty() ? " and " : "");
		queryBuilder.append(StringUtils.join(whereClauses, " and "));
		TypedQuery<Tavolo> typedQuery = entityManager.createQuery(queryBuilder.toString(), Tavolo.class);

		for (String key : parameterMap.keySet()) {
			typedQuery.setParameter(key, parameterMap.get(key));
		}

		return typedQuery.getResultList();

	}

}
