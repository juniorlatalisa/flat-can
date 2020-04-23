package br.com.virtualsistemas.common.builders;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Optional;
import java.util.function.Function;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import br.com.virtualsistemas.common.utils.MailUtils;

public class MimeMessageBuilder implements Builder<MimeMessage> {

	protected MimeMessageBuilder(Session session) {
		System.setProperty("mail.mime.encodefilename", "true");
		try {
			(this.message = new MimeMessage(session))//
					.setContent(this.content = new MimeMultipart());
			setDispositionAttachment();
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public MimeMessageBuilder(String host, int port, String user, String password) {
		this(MailUtils.getSession(host, port, user, password));
		setFrom(user);
	}

	private MimeMessage message;
	private MimeMultipart content;
	private String disposition;

	@Override
	public MimeMessage build() {
		return message;
	}

	public MimeMessageBuilder addHeader(String name, String value) {
		try {
			message.addHeader(name, value);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	public MimeMessageBuilder setFrom(String address) {
		try {
			message.setFrom(address);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	public MimeMessageBuilder setFrom(String address, String personal) {
		try {
			message.setFrom(new InternetAddress(address, personal, "UTF-8"));
		} catch (UnsupportedEncodingException | MessagingException e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	public MimeMessageBuilder setConfirmation(String address) {
		try {
			message.setHeader("Disposition-Notification-To", address);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	public MimeMessageBuilder setReplyTo(String address) {
		try {
			message.setReplyTo(new Address[] { new InternetAddress(address) });
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	public MimeMessageBuilder setReplyTo(String address, String personal) {
		try {
			message.setReplyTo(new Address[] { new InternetAddress(address, personal, "UTF-8") });
		} catch (UnsupportedEncodingException | MessagingException e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	public MimeMessageBuilder setSubject(String subject) {
		try {
			message.setSubject(subject, "UTF-8");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	public MimeMessageBuilder addRecipient(RecipientType type, String address) {
		try {
			message.addRecipients(type, address);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	public MimeMessageBuilder addRecipient(RecipientType type, String address, String personal) {
		try {
			message.addRecipient(type, new InternetAddress(address, personal, "UTF-8"));
		} catch (MessagingException | UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	public MimeMessageBuilder addRecipient(RecipientType type, Address... addresses) {
		try {
			message.addRecipients(type, addresses);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	public MimeMessageBuilder addBody(String text, String charset, String subtype) {
		try {
			MimeBodyPart body = new MimeBodyPart();
			body.setText(text, charset, subtype);
			content.addBodyPart(body);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	public MimeMessageBuilder addBodyText(String value) {
		return addBody(value, "UTF-8", "plain");
	}

	public MimeMessageBuilder addBodyHtml(String value) {
		return addBody(value, "UTF-8", "html");
	}

	public MimeMessageBuilder setDispositionAttachment() {
		this.disposition = "attachment";
		return this;
	}

	public MimeMessageBuilder setDispositionInline() {
		this.disposition = "inline";
		return this;
	}

	private static final Function<DataHandler, String> CONTENT_ID_RESOLVER = DataHandler::getName;

	private Function<DataHandler, String> attachmentIDReolver = CONTENT_ID_RESOLVER;

	public MimeMessageBuilder setAttachmentID(Function<DataHandler, String> reolver) {
		this.attachmentIDReolver = Optional.ofNullable(reolver).orElse(CONTENT_ID_RESOLVER);
		return this;
	}

	public MimeMessageBuilder addAttachment(DataHandler dataHandler) {
		try {
			MimeBodyPart attachment = new MimeBodyPart();
			attachment.setDataHandler(dataHandler);
			attachment.setDisposition(disposition);
			attachment.setFileName(dataHandler.getName());
			attachment.setContentID(attachmentIDReolver.apply(dataHandler));
			content.addBodyPart(attachment);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	public MimeMessageBuilder addAttachment(DataSource dataSource) {
		return addAttachment(new DataHandler(dataSource));
	}

	public MimeMessageBuilder addAttachment(URL url) {
		return addAttachment(new DataHandler(url));
	}

	public MimeMessageBuilder addAttachment(File file) {
		return addAttachment(new DataHandler(new FileDataSource(file)));
	}

	public MimeMessageBuilder addAttachment(byte[] data, String type, String name) {
		ByteArrayDataSource dataSource = new ByteArrayDataSource(data, type);
		dataSource.setName(name);
		return addAttachment(new DataHandler(dataSource));
	}

	public MimeMessageBuilder addAttachment(InputStream is, String type, String name) {
		try {
			ByteArrayDataSource dataSource = new ByteArrayDataSource(is, type);
			dataSource.setName(name);
			return addAttachment(new DataHandler(dataSource));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}