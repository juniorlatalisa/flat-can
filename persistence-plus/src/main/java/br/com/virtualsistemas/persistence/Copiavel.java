package br.com.virtualsistemas.persistence;

import java.io.Serializable;

public interface Copiavel<T> extends Serializable {

	void copiar(T origem);

}