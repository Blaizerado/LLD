package at.ltd.adds.utils.net.web;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import at.ltd.Main;
import at.ltd.adds.utils.net.web.calls.HomeWebManager;

public class Web {

	public static void init() {
		if (Main.isDevModeEnabled()) {
			return;
		}
		HomeWebManager.init();

	}

	public static String readHtmlFile(String name) {
		File f = new File("plugins/LTD/web/" + name);
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		byte[] encoded = null;
		try {
			encoded = Files.readAllBytes(Paths.get("plugins/LTD/web/" + name));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(encoded, Charset.defaultCharset());
	}

}
