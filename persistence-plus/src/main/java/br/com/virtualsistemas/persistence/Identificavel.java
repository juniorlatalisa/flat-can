package br.com.virtualsistemas.persistence;

import java.io.Serializable;
import java.util.Comparator;

import javax.persistence.PersistenceException;

public interface Identificavel<T extends Serializable> extends Serializable {

	@SuppressWarnings("unchecked")
	final Comparator<Identificavel<?>> COMPARATOR_POR_IDENTIFICADOR = (cn1, cn2) -> //
	(cn1 == null || cn2 == null || cn1.getIdentificador() == null) ? 0
			: ((Comparable<? super Serializable>) cn1.getIdentificador()).compareTo(cn2.getIdentificador());

	T getIdentificador();

	default void setIdentificador(String identificador) {
		throw new PersistenceException(identificador);
	}
}