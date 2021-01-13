package br.com.virtualsistemas.persistence;

import java.io.Serializable;

public interface Ativavel extends Serializable {
	
	final String NOT_NULL_MESSAGE = "{entidade.ativo.notnull}";

	Boolean getAtivo();

	void setAtivo(Boolean ativo);

	default boolean isAtivo() {
		return Boolean.TRUE.equals(getAtivo());
	}

}