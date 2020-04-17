package br.com.virtualsistemas.common.builders;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ListBuilderTest {

	@Test
	public void build() {
		Assert.assertTrue(new ListBuilder<String>().build(List::isEmpty));
	}

}
