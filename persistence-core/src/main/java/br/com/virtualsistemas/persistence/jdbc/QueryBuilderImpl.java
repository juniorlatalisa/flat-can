package br.com.virtualsistemas.persistence.jdbc;

import java.util.Iterator;
import java.util.List;

import javax.persistence.PersistenceException;

import br.com.virtualsistemas.persistence.QueryBuilder;

public class QueryBuilderImpl extends QueryBuilder {

	public QueryBuilderImpl(ConnectionFacade facade, String queryValue) {
		this.queryValue = queryValue;
		this.facade = facade;
	}

	private String queryValue;
	private ConnectionFacade facade;

	@Override
	public <T> T single() {
		throw new PersistenceException("unsuported single");
	}

	@Override
	public int execute() {
		return facade.execute(queryValue, getParams());
	}

	@Override
	public <T> List<T> list() {
		return facade.list(queryValue, getParams(), getStartResult(), getMaxResults());
	}

	@Override
	public <T> Iterator<T> iterator() {
		return facade.iterator(queryValue, getParams(), getStartResult(), getMaxResults());
	}
}
