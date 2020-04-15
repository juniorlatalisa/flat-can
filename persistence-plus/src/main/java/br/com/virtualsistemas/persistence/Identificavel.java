package br.com.virtualsistemas.persistence;

import java.io.Serializable;

import javax.persistence.PersistenceException;

public interface Identificavel<T extends Serializable> extends Serializable {

	T getIdentificador();

	default void setIdentificador(String identificador) {
		throw new PersistenceException(identificador);
	}
}