package br.com.virtualsistemas.common.utils;

import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Service;
import javax.mail.Session;
import javax.mail.Transport;

public class MailUtils {

	private static final Logger LOGGER = Logger.getLogger("MailUtils");
	private static boolean debug = false;

	public static void setDebug(boolean debug) {
		MailUtils.debug = debug;
	}

	public static Session getSession(Properties properties, String user, String password) {
		Session session = Session.getInstance(properties, getAuthenticator(user, password));
		session.setDebug(MailUtils.debug);
		return session;
	}

	public static Session getSession(String host, int port, String user, String password) {
		Properties properties = new Properties(System.getProperties());
		if (port == 587) {
			properties.setProperty("mail.smtp.host", host);
			properties.setProperty("mail.smtp.port", "587");
			properties.setProperty("mail.smtp.ssl.trust", host);
			properties.setProperty("mail.smtp.auth", "true");
			properties.setProperty("mail.smtp.connectiontimeout", "5000");
			properties.setProperty("mail.smtp.starttls.enable", "true");
		} else if (port == 465) {
			properties.setProperty("mail.smtps.host", host);
			properties.setProperty("mail.smtps.port", "465");
			properties.setProperty("mail.smtps.ssl.trust", host);
			properties.setProperty("mail.smtps.auth", "true");
			properties.setProperty("mail.smtps.connectiontimeout", "5000");
		} else {
			throw new RuntimeException("Unknow port: " + port);
		}
		return getSession(properties, user, password);
	}

	public static Authenticator getAuthenticator(String user, String password) {
		return new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		};
	}

	public static boolean send(Message message) {
		try {
			Transport.send(message);
			return true;
		} catch (MessagingException e) {
			return false;
		}
	}

	/**
	 * Validar autenticação via SMTP Google:
	 * https://myaccount.google.com/lesssecureapps
	 */
	public static boolean authentication(String host, int port, String user, String password) {
		try {
			Service service = getSession(host, port, user, password).getTransport();
			service.connect();
			try {
				return service.isConnected();
			} finally {
				service.close();
			}
		} catch (MessagingException e) {
			if (!(e instanceof AuthenticationFailedException)) {
				LOGGER.warning(e.getMessage());
			}
			return false;
		}
	}
}