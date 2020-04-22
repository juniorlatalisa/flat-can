package br.com.virtualsistemas.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class MailUtilsTest {

	private static final Properties PROPS = new Properties(System.getProperties());

	@BeforeClass
	public static void beforeClass() {
		MailUtils.setDebug(true);
		InputStream is = MailUtilsTest.class.getResourceAsStream("/META-INF/MailUtilsTest.properties");
		if (is == null) {
			return;
		}
		try {
			try {
				PROPS.load(is);
			} finally {
				is.close();
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	@Test
	public void authentication() {
		if (!PROPS.isEmpty()) {
			Assert.assertTrue(MailUtils.authentication(PROPS//
					.getProperty("host"), // host
					Integer.parseInt(PROPS.getProperty("port"), 10), // port
					PROPS.getProperty("user"), // user
					PROPS.getProperty("password")// password
			));
		}
	}

	@Test
	public void authenticationFail() {
		if (!PROPS.isEmpty()) {
			Assert.assertFalse(MailUtils.authentication(PROPS//
					.getProperty("host"), // host
					Integer.parseInt(PROPS.getProperty("port"), 10), // port
					PROPS.getProperty("user"), // user
					UUID.randomUUID().toString()// password
			));
		}
	}
}