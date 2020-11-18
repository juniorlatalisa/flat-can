package br.com.virtualsistemas.common.utils;

import java.io.Serializable;
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

import br.com.virtualsistemas.common.builders.MimeMessageBuilder;

public class MailUtils {

	private static final Logger LOGGER = Logger.getLogger("MailUtils");
	private static boolean debug = false;

	public static void setDebug(boolean debug) {
		MailUtils.debug = debug;
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

	public static boolean send(Message message, MailSessionData data) {
		try {
			message.saveChanges();// do this first
			Transport transport = message.getSession().getTransport(data.getProtocol());
			transport.connect(data.getUser(), data.getPassword());
			try {
				transport.sendMessage(message, message.getAllRecipients());
			} finally {
				transport.close();
			}
			return true;
		} catch (MessagingException e) {
			return false;
		}
	}

	/**
	 * Validar autenticação via SMTP Google:
	 * https://myaccount.google.com/lesssecureapps
	 */
	public static boolean authentication(MailSessionData data) {
		try {
			Service service = getSession(data).getTransport(data.getProtocol());
			service.connect(data.getUser(), data.getPassword());
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

	protected static void setSession(Properties properties, String protocol, String key, Object data) {
		String value;
		if (data == null || (value = (data instanceof String) ? (String) data : data.toString()).isEmpty()) {
			return;
		}
		properties.setProperty(protocol.concat(key), value);
	}

	public static Session getSession(MailSessionData data) {
		Properties properties = new Properties();
		String protocol = "mail.".concat((data.getProtocol() == null || data.getProtocol().isEmpty()) //
				? "smtp"
				: data.getProtocol());

		setSession(properties, protocol, ".host", data.getHost());
		setSession(properties, protocol, ".port", data.getPort().toString());
		setSession(properties, protocol, ".ssl.trust", data.getHost());
		setSession(properties, protocol, ".auth", data.getAuth());
		setSession(properties, protocol, ".connectiontimeout", data.getConnectionTimeout());
		setSession(properties, protocol, ".starttls.enable", data.getStartTLS());
		setSession(properties, protocol, ".user", data.getUser());

		return getSession(properties, data.getUser(), data.getPassword());
	}

	public static Session getSession(Properties properties, String user, String password) {
		Session session = Session.getInstance(properties, getAuthenticator(user, password));
		session.setDebug(MailUtils.debug);
		return session;
	}

	public static MimeMessageBuilder createMimeMessageBuilder(MailSessionData data) {
		return new MimeMessageBuilder(getSession(data));
	}

	final public static class MailSessionData implements Serializable {

		private static final long serialVersionUID = -7396924913588888480L;

		private String host;
		private String user;
		private String password;
		private String address;

		private String protocol = "smtp";
		private Integer port = 465;
		private Boolean auth = Boolean.TRUE;
		private Boolean startTLS = Boolean.TRUE;
		private Integer connectionTimeout = 5000;

		public String getAddress() {
			return address;
		}

		public MailSessionData setAddress(String address) {
			this.address = address;
			return this;
		}

		public Boolean getStartTLS() {
			return startTLS;
		}

		public MailSessionData setStartTLS(Boolean startTLS) {
			this.startTLS = startTLS;
			return this;
		}

		public Integer getConnectionTimeout() {
			return connectionTimeout;
		}

		public MailSessionData setConnectionTimeout(Integer connecTiontimeout) {
			this.connectionTimeout = connecTiontimeout;
			return this;
		}

		public Boolean getAuth() {
			return auth;
		}

		public MailSessionData setAuth(Boolean auth) {
			this.auth = auth;
			return this;
		}

		public String getProtocol() {
			return protocol;
		}

		public MailSessionData setProtocol(String protocol) {
			this.protocol = protocol;
			return this;
		}

		public String getHost() {
			return host;
		}

		public MailSessionData setHost(String host) {
			this.host = host;
			return this;
		}

		public Integer getPort() {
			return port;
		}

		public MailSessionData setPort(Integer port) {
			this.port = port;
			return this;
		}

		public MailSessionData setPort(String port) {
			return this.setPort(Integer.parseInt(port, 10));
		}

		public String getUser() {
			return user;
		}

		public MailSessionData setUser(String user) {
			this.user = user;
			return this;
		}

		public String getPassword() {
			return password;
		}

		public MailSessionData setPassword(String password) {
			this.password = password;
			return this;
		}
	}
}