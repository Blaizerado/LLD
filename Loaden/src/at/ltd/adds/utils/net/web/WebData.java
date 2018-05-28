package at.ltd.adds.utils.net.web;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

import at.ltd.adds.utils.TPS;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;

public class WebData {

	private static ConcurrentHashMap<String, String> DATA = new ConcurrentHashMap<>();

	public static String addData(String text) {
		String quer = null;
		while (quer == null) {
			quer = randomString(13);
			if (DATA.containsKey(quer)) {
				quer = null;
			}
		}
		final String querfin = quer;
		DATA.put(querfin, text);
		AsyncThreadWorkers.submitDelayedWorkMin(() -> DATA.remove(querfin), 50);
		return "http://ltd-net.eu:2333/data/" + quer;
	}

	private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static SecureRandom rnd = new SecureRandom();

	private static String randomString(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}

}
