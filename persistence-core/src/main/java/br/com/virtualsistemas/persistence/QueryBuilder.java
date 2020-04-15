package br.com.virtualsistemas.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import javax.persistence.PersistenceException;

public abstract class QueryBuilder {

	private int maxResults = VSPersistence.MAX_RESULT_NONE;
	private int startResult = VSPersistence.START_RESULT_NONE;
	private Map<Object, Object> params = VSPersistence.PARAMS_NONE;

	public int getMaxResults() {
		return maxResults;
	}

	public QueryBuilder setMaxResults(int maxResults) {
		this.maxResults = maxResults;
		return this;
	}

	public int getStartResult() {
		return startResult;
	}

	public QueryBuilder setStartResult(int startResult) {
		this.startResult = startResult;
		return this;
	}

	public Map<Object, Object> getParams() {
		return getParams(false);
	}

	protected Map<Object, Object> createParams(Map<Object, Object> copy) {
		if (copy == null) {
			return new HashMap<Object, Object>();
		} else {
			return new HashMap<Object, Object>(copy);
		}
	}

	protected Map<Object, Object> getParams(boolean create) {
		if ((create) && (params == null)) {
			params = createParams(null);
		}
		return params;
	}

	public QueryBuilder setParams(Map<Object, Object> params) {
		this.params = params;
		return this;
	}

	public QueryBuilder addParams(Map<Object, Object> params) {
		getParams(true).putAll(params);
		return this;
	}

	public QueryBuilder setParam(Object key, Object value) {
		getParams(true).put(key, value);
		return this;
	}

	public QueryBuilder clearParams() {
		if (getParams(false) != null) {
			getParams(false).clear();
		}
		return this;
	}

	public QueryBuilder removeParam(Object key) {
		if (getParams(false) != null) {
			getParams(false).remove(key);
		}
		return this;
	}

	public QueryBuilder clearMaxResults() {
		return setMaxResults(VSPersistence.MAX_RESULT_NONE);
	}

	public QueryBuilder clearStartResult() {
		return setStartResult(VSPersistence.START_RESULT_NONE);
	}

	@SuppressWarnings("unchecked")
	public <T> T find() {
		Iterator<T> iterator = iterator();
		return (iterator.hasNext()) ? (iterator instanceof Supplier) ? ((Supplier<T>) iterator).get() : iterator.next()
				: null;
	}

	public <T> Optional<T> optional() {
		return Optional.ofNullable(find());
	}

	public abstract int execute();

	public abstract <T> List<T> list();

	public abstract <T> Iterator<T> iterator();

	public static String load(InputStream is, Charset charset) {
		String sql;
		try {
			try {
				byte[] buffer = new byte[is.available()];
				is.read(buffer);
				sql = new String(buffer, charset);
			} finally {
				is.close();
			}
		} catch (IOException e) {
			throw new PersistenceException(e);
		}
		if ((StandardCharsets.UTF_8.equals(charset)) && (sql.contains(VSPersistence.UTF8_BOM))) {
			sql = sql.replace(VSPersistence.UTF8_BOM, "");
		}
		return sql;
	}
}