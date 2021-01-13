package br.com.virtualsistemas.persistence;

import java.io.Serializable;
import java.util.Comparator;

public interface Codificavel<T extends Number> extends Serializable {

	@SuppressWarnings("unchecked")
	public static final Comparator<Codificavel<? extends Number>> COMPARATOR_POR_CODIGO = (cn1, cn2) -> ((Comparable<Number>)cn1.getCodigo())
			.compareTo(cn2.getCodigo());

	T getCodigo();

}