package br.com.virtualsistemas.persistence.jpa;

import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

import br.com.virtualsistemas.persistence.QueryBuilder;
import br.com.virtualsistemas.persistence.VSPersistence;

public abstract class JPAFacade {

	protected abstract EntityManager getEntityManager();

	protected Query createDefaultQuery(String queryValue, Map<Object, Object> params) {
		return getEntityManager().createQuery(queryValue);
	}

	protected Query createNamedQuery(String queryValue, Map<Object, Object> params) {
		return getEntityManager().createNamedQuery(queryValue);
	}

	protected Query createNativeQuery(String queryValue, Map<Object, Object> params) {
		return getEntityManager().createNativeQuery(queryValue);
	}

	protected <T> TypedQuery<T> createCriteriaQuery(Class<T> entityClass, Map<Object, Object> params) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(entityClass);
		Root<T> root = cq.from(entityClass);
		cq.select(root);
		setParams(cb, cq, root, params);
		return getEntityManager().createQuery(cq);
	}

	protected Query createQuery(QueryStrategy queryStrategy, Serializable queryValue, Map<Object, Object> params) {
		queryValue = ((QueryStrategy.CRITERIA.equals(queryStrategy)) || (queryValue instanceof String)) ? queryValue
				: queryValue.toString();
		switch (queryStrategy) {
		case DEFAULT:
			return createDefaultQuery((String) queryValue, params);
		case NAMED:
			return createNamedQuery((String) queryValue, params);
		case NATIVE:
			return createNativeQuery((String) queryValue, params);
		case CRITERIA:
			return createCriteriaQuery((Class<?>) queryValue, params);
		default:
			throw new PersistenceException("QueryStrategy inválido: " + queryStrategy);
		}
	}

	protected <T> void setParams(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root, Map<Object, Object> params) {
		if (!((params == VSPersistence.PARAMS_NONE) || (params.isEmpty()))) {
			params.keySet().forEach(key -> setParams(cb, cq, root, params, key));
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected <T> void setParams(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root, Map<Object, Object> params,
			Object key) {
		Object value = params.get(key);
		Expression<?> expression;
		if (key instanceof String) {
			expression = root.get((String) key);
		} else if (key instanceof SingularAttribute) {
			expression = root.get((SingularAttribute) key);
		} else if (key instanceof PluralAttribute) {
			expression = root.get((PluralAttribute) key);
		} else {
			return;
		}
		if (value instanceof Collection<?>) {
			In<Object> in = cb.in(expression);
			((Collection<Object>) value).forEach(v -> in.value(v));
			cq.where(in);
		} else {
			cq.where(cb.and(cb.equal(expression, value)));
		}
	}

	protected void setParams(QueryStrategy queryStrategy, Query query, Map<Object, Object> params, int startResult,
			int maxResults) {
		if (startResult != VSPersistence.START_RESULT_NONE) {
			query.setFirstResult(startResult);
		}
		if (maxResults != VSPersistence.MAX_RESULT_NONE) {
			query.setMaxResults(maxResults);
		}
		setParams(queryStrategy, query, params);
	}

	@SuppressWarnings("unchecked")
	protected void setParams(QueryStrategy queryStrategy, Query query, Map<Object, Object> params) {
		if (!((params == VSPersistence.PARAMS_NONE) || (params.isEmpty()))) {
			params.keySet().forEach((Object key) -> {
				Object value = params.get(key);
				if (!QueryStrategy.CRITERIA.equals(queryStrategy)) {
					if (key instanceof String) {
						query.setParameter((String) key, value);
						return;
					} else if (key instanceof Number) {
						query.setParameter(((Number) key).intValue(), value);
						return;
					} else if (key instanceof Parameter<?>) {// TODO TESTAR
						query.setParameter((Parameter<Object>) key, value);
						return;
					} else if (key instanceof Attribute<?, ?>) {// TODO TESTAR
						query.setParameter(((Attribute<?, ?>) key).getName(), value);
						return;
					}
				}
				if ((LockModeType.class.equals(key)) && (value instanceof LockModeType)) {
					query.setLockMode((LockModeType) value);
				} else if ((FlushModeType.class.equals(key)) && (value instanceof FlushModeType)) {
					query.setFlushMode((FlushModeType) value);
				}
			});
		}
	}

	/**
	 * @param <T>
	 * @param queryStrategy
	 * @param queryValue
	 * @param params
	 * @return
	 * @see Query#getSingleResult()
	 */
	@SuppressWarnings("unchecked")
	public <T extends Serializable> T single(QueryStrategy queryStrategy, Serializable queryValue,
			Map<Object, Object> params) {
		Query query = createQuery(queryStrategy, queryValue, params);
		setParams(queryStrategy, query, params, VSPersistence.START_RESULT_NONE, VSPersistence.MAX_RESULT_NONE);
		return (T) query.getSingleResult();
	}

	/**
	 * @param <T>
	 * @param queryStrategy
	 * @param queryValue
	 * @param params
	 * @param startResult
	 * @param maxResults
	 * @return
	 * @see Query#getResultList()
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> list(QueryStrategy queryStrategy, Serializable queryValue, Map<Object, Object> params,
			int startResult, int maxResults) {
		Query query = createQuery(queryStrategy, queryValue, params);
		setParams(queryStrategy, query, params, startResult, maxResults);
		return query.getResultList();
	}

	/**
	 * @param queryStrategy
	 * @param queryValue
	 * @param params
	 * @return
	 * @see Query#executeUpdate()
	 */
	public int execute(QueryStrategy queryStrategy, Serializable queryValue, Map<Object, Object> params) {
		Query query = createQuery(queryStrategy, queryValue, params);
		setParams(queryStrategy, query, params);
		return query.executeUpdate();
	}

	public <T> Iterator<T> iterator(QueryStrategy queryStrategy, Serializable queryValue, Map<Object, Object> params,
			int startResult) {
		return new JPAIterator<T>(queryStrategy, queryValue, params, startResult);
	}

	/**
	 * @param <T>
	 * @param entityClass
	 * @param primaryKey
	 * @return
	 * @see EntityManager#getReference(Class, Object)
	 */
	public <T extends Serializable> T reference(Class<T> entityClass, Serializable primaryKey) {
		return getEntityManager().getReference(entityClass, primaryKey);
	}

	/**
	 * @param <T>
	 * @param entityClass
	 * @param primaryKey
	 * @return
	 * @see EntityManager#find(Class, Object)
	 */
	public <T extends Serializable> T find(Class<T> entityClass, Serializable primaryKey) {
		return getEntityManager().find(entityClass, primaryKey);
	}

	/**
	 * @param <T>
	 * @param entity
	 * @return
	 * @see EntityManager#merge(Object)
	 */
	public <T extends Serializable> T update(T entity) {
		return getEntityManager().merge(entity);
	}

	public <T extends Serializable> List<T> update(List<T> entities) {
		EntityManager em = getEntityManager();
		return entities.stream().map(entity -> em.merge(entity)).collect(Collectors.toList());
	}

	/**
	 * @param <T>
	 * @param entity
	 * @return
	 * @see EntityManager#persist(Object)
	 */
	public <T extends Serializable> T insert(T entity) {
		getEntityManager().persist(entity);
		return entity;
	}

	public <T extends Serializable> List<T> insert(List<T> entities) {
		EntityManager em = getEntityManager();
		entities.forEach(entity -> em.persist(entity));
		return entities;
	}

	public <T extends Serializable> boolean delete(Class<T> entityClass, Serializable primaryKey) {
		try {
			getEntityManager().remove(getEntityManager().getReference(entityClass, primaryKey));
			return true;
		} catch (EntityNotFoundException e) {
			return false;
		}
	}

	public QueryBuilder createQueryBuilder(QueryStrategy queryStrategy, Serializable queryValue) {
		return new QueryBuilderImpl(queryStrategy, queryValue);
	}

	public QueryBuilder createQueryBuilder(QueryStrategy queryStrategy, InputStream queryValue) {
		return createQueryBuilder(queryStrategy, QueryBuilder.load(queryValue, StandardCharsets.UTF_8));
	}

	protected class QueryBuilderImpl extends QueryBuilder {

		public QueryBuilderImpl(QueryStrategy queryStrategy, Serializable queryValue) {
			this.queryStrategy = queryStrategy;
			this.queryValue = queryValue;
		}

		private QueryStrategy queryStrategy;
		private Serializable queryValue;

		@Override
		public int execute() {
			return JPAFacade.this.execute(queryStrategy, queryValue, getParams());
		}

		@Override
		public <T> List<T> list() {
			return JPAFacade.this.list(queryStrategy, queryValue, getParams(), getStartResult(), getMaxResults());
		}

		@Override
		public <T> Iterator<T> iterator() {
			return JPAFacade.this.iterator(queryStrategy, queryValue, getParams(), getStartResult());
		}
	}

	protected class JPAIterator<E> implements Iterator<E> {

		public JPAIterator(QueryStrategy queryStrategy, Serializable queryValue, Map<Object, Object> params,
				int startResult) {
			this.element = null;
			this.params = params;
			this.queryStrategy = queryStrategy;
			this.queryValue = queryValue;
			this.startResult = (VSPersistence.START_RESULT_NONE == startResult) ? 0 : startResult;
		}

		private E element;
		private int startResult;

		private QueryStrategy queryStrategy;
		private Serializable queryValue;
		private Map<Object, Object> params;

		@SuppressWarnings("unchecked")
		protected List<E> getResultList() {
			Query query = createQuery(queryStrategy, queryValue, params);
			setParams(queryStrategy, query, params, startResult++, 1);
			return query.getResultList();
		}

		@Override
		public boolean hasNext() {
			List<E> lista = getResultList();
			if ((lista == null) || (lista.isEmpty())) {
				return false;
			}
			return (element = lista.get(0)) != null;
		}

		@Override
		public E next() {
			if (element == null) {
				throw new NoSuchElementException();
			}
			try {
				return element;
			} finally {
				element = null;
			}
		}
	}

	/**
	 * Padrões aceitaves pelo {@link JPAFacade} para retornar uma instância de
	 * {@link Query}.
	 * 
	 * @author Junior Latalisa
	 */
	public static enum QueryStrategy {
		/**
		 * Padrão.
		 * 
		 * @author Junior Latalisa
		 * @see EntityManager#createQuery(String)
		 */
		DEFAULT,
		/**
		 * Nomeada.
		 * 
		 * @author Junior Latalisa
		 * @see EntityManager#createNamedQuery(String)
		 */
		NAMED,
		/**
		 * Nativa.
		 * 
		 * @author Junior Latalisa
		 * @see EntityManager#createNativeQuery(String)
		 */
		NATIVE,
		/**
		 * Critérios.
		 * <p>
		 * Atenção¹: o queryValue deverá ser a class da entidade que será retornada.
		 * <p>
		 * Atenção²: No mapa de parâmetros serão aceitas chaves do tipo:
		 * <ul>
		 * <li>{@link String}: será utilizado o método {@link Root#get(String)}</li>
		 * <li>{@link SingularAttribute}: será utilizado o método
		 * {@link Root#get(SingularAttribute)}</li>
		 * <li>{@link PluralAttribute}: será utilizado o método
		 * {@link Root#get(PluralAttribute)}</li>
		 * </ul>
		 * 
		 * @author Junior Latalisa
		 * @see CriteriaBuilder#createQuery(Class)
		 * @see EntityManager#getCriteriaBuilder()
		 * @see CriteriaQuery#from(Class)
		 * @see CriteriaQuery#select(Selection)
		 * @see EntityManager#createQuery(CriteriaQuery)
		 */
		CRITERIA,;
	}
}