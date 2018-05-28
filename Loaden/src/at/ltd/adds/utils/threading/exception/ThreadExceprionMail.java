package at.ltd.adds.utils.threading.exception;

import java.io.IOException;

import com.google.common.base.Throwables;

import at.ltd.adds.utils.net.Mail;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;

public class ThreadExceprionMail {

	private Thread thread;
	private Throwable throwable;

	private static final String to = "ltd.net.eu@gmail.com";
	private static final String from = "exceprionhandler@gltd.eu";

	public ThreadExceprionMail(Thread thread, Throwable ex) {
		this.thread = thread;
		this.throwable = ex;
	}

	public void send() {
		AsyncThreadWorkers.submitWork(() -> {
			Mail mail = new Mail(from, to, "Exception on thread: " + thread.getName() + ":" + thread.getId(), null);
			String text = new String(mail.getSubject() + "<br>Cause:<br>" + throwable.getCause() + "<br>ST:<br>" + Throwables.getStackTraceAsString(throwable));
			mail.setText(text);
			try {
				mail.sendEmail();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	
	public static void send(Thread th, Throwable tr) {
		new ThreadExceprionMail(th, tr).send();
	}

}
