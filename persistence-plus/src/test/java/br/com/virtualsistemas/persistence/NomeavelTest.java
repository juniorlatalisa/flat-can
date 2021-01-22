package br.com.virtualsistemas.persistence;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class NomeavelTest {

	private static final List<Nomeavel> letras = Arrays.asList(() -> "z", () -> "b", () -> "a", () -> "Y");

	@Test
	public void comparator() {
		Object[] ordenado = letras.stream().sorted(Nomeavel.COMPARATOR_POR_NOME).map(l -> l.getNome()).toArray();
		// System.out.println(Arrays.toString(ordenado));
		Assert.assertEquals("Y", ordenado[0]);
	}

	@Test
	public void comparatorIgnoreCase() {
		Object[] ordenado = letras.stream().sorted(Nomeavel.COMPARATOR_POR_NOME_IGNORE_CASE).map(l -> l.getNome())
				.toArray();
		// System.out.println(Arrays.toString(ordenado));
		Assert.assertEquals("a", ordenado[0]);
	}

}
