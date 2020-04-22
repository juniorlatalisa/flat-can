package br.com.virtualsistemas.common.utils;

import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.Service;
import javax.mail.Session;

public class MailUtils {

	private static final Logger LOGGER = Logger.getLogger("MailUtils");
	private static boolean debug = false;

	public static void setDebug(boolean debug) {
		MailUtils.debug = debug;
	}

	/**
	 * Validar autenticação via SMTP
	 */
	public static boolean authentication(String host, int port, String user, String password) {
		Properties props = new Properties(System.getProperties());
		String protocol;
		if (port == 587) {
			props.setProperty("mail.smtp.auth", "true");
			props.setProperty("mail.smtp.connectiontimeout", "5000");
			props.setProperty("mail.smtp.starttls.enable", "true");
			protocol = "smtp";
		} else if (port == 465) {
			props.setProperty("mail.smtps.auth", "true");
			props.setProperty("mail.smtps.connectiontimeout", "5000");
			props.setProperty("mail.smtps.ssl.trust", host);
			protocol = "smtps";
		} else {
			throw new RuntimeException("Unknow port: " + port);
		}
		try {
			Session session = Session.getDefaultInstance(props);
			session.setDebug(MailUtils.debug);
			Service service = session.getTransport(protocol);
			service.connect(host, port, user, password);
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