package br.com.virtualsistemas.common.utils;

import org.junit.Assert;
import org.junit.Test;

public class MavenUtilsTest {

	@Test
	public void version() {
		Assert.assertNotNull(MavenUtils.getVersion(getClass(), //
				"br.com.virtualsistemas.common", "common-utils")//
				.orElse("Não enconrado"));
	}

	public static void main(String[] args) {
		System.out.println(MavenUtils.getVersion(MavenUtils.class, //
				"br.com.virtualsistemas.common", "common-utils"));
	}
}