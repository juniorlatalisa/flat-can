package br.com.virtualsistemas.persistence.jdbc;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import javax.persistence.PersistenceException;

import br.com.virtualsistemas.persistence.QueryBuilder;
import br.com.virtualsistemas.persistence.VSPersistence;

public abstract class ConnectionFacade {

	protected abstract Connection getConnection();

	public abstract void close();

	protected Statement getStatement(int startResult) throws SQLException {
		return (VSPersistence.START_RESULT_NONE == startResult) ? getConnection().createStatement()
				: getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
	}

	protected PreparedStatement getPreparedStatement(String queryValue, Map<Object, Object> params, int startResult)
			throws SQLException {
		PreparedStatement retorno = (VSPersistence.START_RESULT_NONE == startResult)
				? getConnection().prepareStatement(queryValue)
				: getConnection().prepareStatement(queryValue, ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
		for (Entry<Object, Object> entry : params.entrySet()) {
			retorno.setObject(((Number) entry.getKey()).intValue(), entry.getValue());
		}
		return retorno;
	}

	protected Statement getStatement(String queryValue, Map<Object, Object> params, int startResult)
			throws SQLException {
		return ((params == null) || (params.isEmpty())) ? getStatement(VSPersistence.START_RESULT_NONE)
				: getPreparedStatement(queryValue, params, VSPersistence.START_RESULT_NONE);
	}

	public int execute(String queryValue, Map<Object, Object> params) {
		try {
			Statement statement = getStatement(queryValue, params, VSPersistence.START_RESULT_NONE);
			try {
				return (statement instanceof PreparedStatement) ? ((PreparedStatement) statement).executeUpdate()
						: statement.executeUpdate(queryValue);
			} finally {
				statement.close();
			}
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	protected ResultSet getResultSet(Statement statement, String queryValue, int startResult, int maxResults)
			throws SQLException {
		if (VSPersistence.MAX_RESULT_NONE != maxResults) {
			statement.setMaxRows(maxResults);
		}
		ResultSet resultSet = (statement instanceof PreparedStatement) ? ((PreparedStatement) statement).executeQuery()
				: statement.executeQuery(queryValue);
		if (VSPersistence.START_RESULT_NONE != startResult) {
			resultSet.absolute(startResult);
		}
		return resultSet;
	}

	protected Object getObject(ResultSet resultSet, int columnCount) throws SQLException {
		if (columnCount > 1) {
			Object[] linha = new Object[columnCount];
			for (int i = columnCount; i > 0; i--) {
				linha[i - 1] = resultSet.getObject(i);
			}
			return linha;
		} else {
			return resultSet.getObject(1);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> list(String queryValue, Map<Object, Object> params, int startResult, int maxResults) {
		try {
			Statement statement = getStatement(queryValue, params, startResult);
			try {
				ResultSet resultSet = getResultSet(statement, queryValue, startResult, maxResults);
				try {
					int columnCount = resultSet.getMetaData().getColumnCount();
					List<T> retorno = new ArrayList<>();
					while (resultSet.next()) {
						retorno.add((T) getObject(resultSet, columnCount));
					}
					return retorno;
				} finally {
					resultSet.close();
				}
			} finally {
				statement.close();
			}
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}

	public <T> Iterator<T> iterator(String queryValue, Map<Object, Object> params, int startResult, int maxResults) {
		return new JDBCIterator<T>(queryValue, params, startResult, maxResults);
	}

	public QueryBuilder createQueryBuilder(String queryValue) {
		return new QueryBuilderImpl(queryValue);
	}

	public QueryBuilder createQueryBuilder(InputStream queryValue) {
		return createQueryBuilder(QueryBuilder.load(queryValue, StandardCharsets.UTF_8));
	}

	protected class JDBCIterator<E> implements Iterator<E>, Supplier<E> {

		public JDBCIterator(String queryValue, Map<Object, Object> params, int startResult, int maxResults) {
			try {
				this.statement = getStatement(queryValue, params, startResult);
				try {
					this.resultSet = getResultSet(statement, queryValue, startResult, maxResults);
				} catch (SQLException e) {
					this.statement.close();
					throw e;
				}
				try {
					this.columnCount = resultSet.getMetaData().getColumnCount();
				} catch (SQLException e) {
					this.statement.close();
					this.resultSet.close();
					throw e;
				}
			} catch (SQLException e) {
				throw new PersistenceException(e);
			}
		}

		private E element;

		private Statement statement;
		private ResultSet resultSet;
		private int columnCount;

		@SuppressWarnings("unchecked")
		@Override
		public boolean hasNext() {
			try {
				if (!resultSet.next()) {
					close();
					return false;
				}
				return (element = (E) getObject(resultSet, columnCount)) != null;
			} catch (SQLException e) {
				throw new PersistenceException(e);
			}
		}

		protected void close() throws SQLException {
			try {
				this.resultSet.close();
				this.statement.close();
			} finally {
				this.resultSet = null;
				this.statement = null;
			}
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

		@Override
		public E get() {
			try {
				return next();
			} finally {
				try {
					close();
				} catch (SQLException e) {
					throw new PersistenceException(e);
				}
			}
		}
	}

	protected class QueryBuilderImpl extends QueryBuilder {

		public QueryBuilderImpl(String queryValue) {
			this.queryValue = queryValue;
		}

		private String queryValue;

		@Override
		public <T> T single() {
			throw new PersistenceException("unsuported single");
		}

		@Override
		public int execute() {
			return ConnectionFacade.this.execute(queryValue, getParams());
		}

		@Override
		public <T> List<T> list() {
			return ConnectionFacade.this.list(queryValue, getParams(), getStartResult(), getMaxResults());
		}

		@Override
		public <T> Iterator<T> iterator() {
			return ConnectionFacade.this.iterator(queryValue, getParams(), getStartResult(), getMaxResults());
		}
	}
}