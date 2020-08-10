package br.com.virtualsistemas.persistence.jpa;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class ResourceLocalFacade extends JPAFacade {

	public ResourceLocalFacade(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	protected <T> T transaction(Supplier<T> command) {
		EntityTransaction transaction = getEntityManager().getTransaction();
		transaction.begin();
		try {
			T _return = command.get();
			transaction.commit();
			return _return;
		} finally {
			if (transaction.isActive()) {
				transaction.rollback();
			}
		}
	}

	@Override
	public int execute(QueryStrategy queryStrategy, Serializable queryValue, Map<Object, Object> params) {
		return transaction(() -> super.execute(queryStrategy, queryValue, params));
	}

	@Override
	public <T extends Serializable> T update(T entity) {
		return transaction(() -> super.update(entity));
	}

	@Override
	public <T extends Serializable> List<T> update(List<T> entities) {
		return transaction(() -> super.update(entities));
	}

	@Override
	public <T extends Serializable> T insert(T entity) {
		return transaction(() -> super.insert(entity));
	}

	@Override
	public <T extends Serializable> List<T> insert(List<T> entities) {
		return transaction(() -> super.insert(entities));
	}

	@Override
	public <T extends Serializable> boolean delete(Class<T> entityClass, Serializable primaryKey) {
		return transaction(() -> super.delete(entityClass, primaryKey));
	}

	@Override
	public <T extends Serializable> T single(QueryStrategy queryStrategy, Serializable queryValue,
			Map<Object, Object> params) {
		getEntityManager().clear();
		return super.single(queryStrategy, queryValue, params);
	}

	@Override
	public <T> List<T> list(QueryStrategy queryStrategy, Serializable queryValue, Map<Object, Object> params,
			int startResult, int maxResults) {
		getEntityManager().clear();
		return super.list(queryStrategy, queryValue, params, startResult, maxResults);
	}

	@Override
	public <T> Iterator<T> iterator(QueryStrategy queryStrategy, Serializable queryValue, Map<Object, Object> params,
			int startResult) {
		getEntityManager().clear();
		return super.iterator(queryStrategy, queryValue, params, startResult);
	}

	@Override
	public <T extends Serializable> T find(Class<T> entityClass, Serializable primaryKey) {
		getEntityManager().clear();
		return super.find(entityClass, primaryKey);
	}
}