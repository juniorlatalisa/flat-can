package br.com.virtualsistemas.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Message.RecipientType;
import javax.ws.rs.core.MediaType;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.virtualsistemas.common.builders.MimeMessageBuilder;

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
	public void build() {
		if (!PROPS.isEmpty()) {
			Assert.assertNotNull(new MimeMessageBuilder(PROPS//
					.getProperty("host"), // host
					Integer.parseInt(PROPS.getProperty("port"), 10), // port
					PROPS.getProperty("user"), // user
					PROPS.getProperty("password")// password
			).build());
		}
	}

	@Test
	public void send() {
		if (!PROPS.isEmpty()) {
			Assert.assertTrue(new MimeMessageBuilder(//
					PROPS.getProperty("host"), // host
					Integer.parseInt(PROPS.getProperty("port"), 10), // port
					PROPS.getProperty("user"), // user
					PROPS.getProperty("password")// password
			)//
					.addHeader("virtual-teste", "header-test")//
					// .addHeader("Disposition-Notification-To", PROPS.getProperty("user"))//
					.setFrom(PROPS.getProperty("user"), "Origem do teste \uD83D\uDE00")//
					.setConfirmation(PROPS.getProperty("user")).setSubject("\uD83C\uDF55".concat(new Date().toString()))//
					.addRecipient(RecipientType.TO, PROPS.getProperty("to"), "Destino do teste \uD83D\uDE0E")//
					.addBodyHtml("Texto HTML de teste do <strong>body</strong> &#128545; \uD83D\uDE21")//
					.addAttachment(MailUtilsTest.class.getResourceAsStream("/META-INF/MailUtilsTest.properties"),
							MediaType.TEXT_PLAIN, "teste.txt")//
					.build(MailUtils::send));
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