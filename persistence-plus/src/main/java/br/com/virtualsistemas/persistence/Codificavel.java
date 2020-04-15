package br.com.virtualsistemas.persistence;

import java.io.Serializable;
import java.util.Comparator;

public interface Codificavel<T extends Number> extends Serializable {

	public static final Comparator<Codificavel<Long>> COMPARATOR_POR_CODIGO = (cn1, cn2) -> cn1.getCodigo()
			.compareTo(cn2.getCodigo());

	T getCodigo();

}