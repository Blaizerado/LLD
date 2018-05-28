package at.ltd.adds.utils.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

	public static HashMap<String, String> readConfig(String location) {
		try {
			HashMap<String, String> content = new HashMap<>();
			File f = new File(location);
			if (!f.exists()) {
				File parent = f.getParentFile();
				if (!parent.exists() && !parent.mkdirs()) {
					throw new IllegalStateException("Couldn't create dir: " + parent);
				} else {
					PrintWriter writer = new PrintWriter(location, "UTF-8");
					writer.println("#**");
					writer.close();
				}
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(location), "UTF-8"));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.trim().length() > 0) {
					if (!line.startsWith("#")) {
						String[] splited = line.split("\\=", 2);
						content.put(dereplace(splited[0]), splited[1]);
					}
				}
			}
			br.close();

			return content;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public static void setConfig(Map<String, String> content, String location) {
		try {
			File f = new File(location);
			if (!f.exists()) {
				File parent = f.getParentFile();
				if (!parent.exists() && !parent.mkdirs()) {
					throw new IllegalStateException("Couldn't create dir: " + parent);
				}
			}
			PrintWriter writer = new PrintWriter(location, "UTF-8");
			for (String s : content.keySet()) {
				writer.println(replace(s) + "=" + content.get(s));
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void setRAW(String text, String location) throws IOException {
		try {
			File f = new File(location);
			if (!f.exists()) {
				File parent = f.getParentFile();
				if (!parent.exists() && !parent.mkdirs()) {
					throw new IllegalStateException("Couldn't create dir: " + parent);
				}
			}
			PrintWriter writer = new PrintWriter(location, "UTF-8");
			writer.println(text);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void addElement(String first, String second, String location) {
		try {
			File f = new File(location);
			if (!f.exists()) {
				File parent = f.getParentFile();
				if (!parent.exists() && !parent.mkdirs()) {
					throw new IllegalStateException("Couldn't create dir: " + parent);
				}
			}
			Writer output;
			output = new BufferedWriter(new FileWriter(location, true));
			output.append("\r\n" + replace(first) + "=" + second);
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void addRAWText(String text, String location) {
		try {
			File f = new File(location);
			if (!f.exists()) {
				File parent = f.getParentFile();
				if (!parent.exists() && !parent.mkdirs()) {
					throw new IllegalStateException("Couldn't create dir: " + parent);
				}
			}
			Writer output;
			output = new BufferedWriter(new FileWriter(location, true));
			output.append(text);
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String readElement(String name, String location) {
		return readConfig(location).get(name);
	}

	public static void removeElement(String first, String sec, String location) {
		String line = replace(first) + "=" + sec;
		removeLineFromFile(location, line);
	}

	private static void removeLineFromFile(String file, String lineToRemove) {

		try {

			File inFile = new File(file);

			if (!inFile.isFile()) {
				System.out.println("Parameter is not an existing file");
				return;
			}

			File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

			BufferedReader br = new BufferedReader(new FileReader(file));
			PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

			String line = null;

			while ((line = br.readLine()) != null) {

				if (!line.trim().equals(lineToRemove)) {

					pw.println(line);
					pw.flush();
				}
			}
			pw.close();
			br.close();

			if (!inFile.delete()) {
				System.out.println("Could not delete file");
				return;
			}

			if (!tempFile.renameTo(inFile))
				System.out.println("Could not rename file");

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private static String replace(String string) {
		return string.replace("=", ":':");
	}

	private static String dereplace(String string) {
		return string.replace(":':", "=");
	}
}
