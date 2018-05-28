package at.ltd.adds.utils.threading.exception;

import java.util.ArrayList;

import at.ltd.adds.utils.threading.removeable.RemoveAble;

public class ThreadExceprionHandler {

	private static ArrayList<Throwable> EX = new ArrayList<>();
	private static boolean disabled = false;

	public static void init() {
	}

	private static synchronized void handle(Thread thread, Throwable e) {
		if (disabled) {
			return;
		}
		synchronized (EX) {
			if (EX.size() > 10) {
				disabled = true;
				RemoveAble.addMin(() -> disabled = false, 10);
				return;
			}
			EX.add(e);
			RemoveAble.addSec(() -> {
				synchronized (EX) {
					EX.remove(e);
				}
			}, 20);
		}
		ThreadExceprionMail tem = new ThreadExceprionMail(thread, e);
		tem.send();
	}

}
