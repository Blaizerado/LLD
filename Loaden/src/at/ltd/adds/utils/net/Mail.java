package at.ltd.adds.utils.net;
import java.io.IOException;

/**
 * Using Sendgrid API. <br>
 * All methods are Thread safe.<br>
 * Uses the Mail Object as Lock. <br>
 * <br>
 * 
 * {@link https://sendgrid.com/}
 * 
 * @author NyroForce
 * @since 11.01.2018
 * @version 1.0.1
 */
public class Mail {

	private String MAIL_Text;
	private String MAIL_From;
	private String MAIL_To;
	private String MAIL_Subject;

	public Mail() {
	}

	public Mail(String from, String to, String subject, String text) {
		synchronized (this) {
			this.MAIL_From = from;
			this.MAIL_To = to;
			this.MAIL_Subject = subject;
			this.MAIL_Text = text;
		}
	}

	public String getText() {
		synchronized (this) {
			return MAIL_Text;
		}
	}

	public void setText(String text) {
		synchronized (this) {
			MAIL_Text = text;
		}
	}

	public String getEmailAdressFrom() {
		synchronized (this) {
			return MAIL_From;
		}
	}

	public void setEmailAdressFrom(String mail) {
		synchronized (this) {
			MAIL_From = mail;
		}
	}

	public String getEmailAdressTo() {
		synchronized (this) {
			return MAIL_To;
		}
	}

	public void setEmailAdressTo(String mail) {
		synchronized (this) {
			MAIL_To = mail;
		}
	}

	public String getSubject() {
		synchronized (this) {
			return MAIL_Subject;
		}
	}

	public void setSubject(String subject) {
		synchronized (this) {
			MAIL_Subject = subject;
		}
	}

	public static void sendEmail(String from, String to, String subject, String text) {
		Mail mail = new Mail();
		mail.setEmailAdressFrom(from);
		mail.setEmailAdressTo(to);
		mail.setSubject(subject);
		mail.setText(text);
	}

}
