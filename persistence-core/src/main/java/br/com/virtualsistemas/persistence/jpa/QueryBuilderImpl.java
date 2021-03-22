package br.com.virtualsistemas.persistence.jpa;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import br.com.virtualsistemas.persistence.QueryBuilder;
import br.com.virtualsistemas.persistence.jpa.JPAFacade.QueryStrategy;

public class QueryBuilderImpl extends QueryBuilder {

	public QueryBuilderImpl(JPAFacade facade, QueryStrategy queryStrategy, Serializable queryValue) {
		this.queryStrategy = queryStrategy;
		this.queryValue = queryValue;
		this.facade = facade;
	}

	private QueryStrategy queryStrategy;
	private Serializable queryValue;
	private JPAFacade facade;

	@Override
	public <T> T single() {
		return facade.single(queryStrategy, queryValue, getParams());
	}

	@Override
	public int execute() {
		return facade.execute(queryStrategy, queryValue, getParams());
	}

	@Override
	public <T> List<T> list() {
		return facade.list(queryStrategy, queryValue, getParams(), getStartResult(), getMaxResults());
	}

	@Override
	public <T> Iterator<T> iterator() {
		return facade.iterator(queryStrategy, queryValue, getParams(), getStartResult());
	}

}
