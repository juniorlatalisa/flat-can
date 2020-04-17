package br.com.virtualsistemas.common.builders;

import org.junit.Assert;
import org.junit.Test;

import br.com.virtualsistemas.common.utils.StringUtils;

public class DateBuilderTest {

	@Test
	public void build() {
		Assert.assertNotNull(new DateBuilder().build(StringUtils::formatDateTime));
	}

}