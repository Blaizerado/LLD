package at.ltd.adds.utils.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Get HTML text from a webpage. <br>
 * Thread safe.
 * @author NyroForce
 * @since 12.01.2018
 * @version 1.0.1
 */
public class WebGet {

	public static String get(String link) {
		try {
			return getText(link);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getHaste(String link) {
		if (!link.contains("/raw/")) {
			throw new IllegalArgumentException("Only accept /raw format!");
		}
		try {
			return getText(link);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String getText(String url) throws Exception {
		URL website = new URL(url);
		URLConnection connection = website.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

		StringBuilder response = new StringBuilder();
		String inputLine;

		while ((inputLine = in.readLine()) != null)
			response.append(inputLine);

		in.close();

		return response.toString();
	}

}
