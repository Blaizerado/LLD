package at.ltd.adds.superuser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.entity.Player;

import at.ltd.Main;
import at.ltd.adds.sql.SQLQuery;
import at.ltd.adds.superuser.google.GoogleAuthenticator;
import at.ltd.adds.superuser.google.GoogleAuthenticatorKey;

public class SuperUser {

	public static ArrayList<String> SU_USER = new ArrayList<>();

	public static String register(String uuid, String name) {
		GoogleAuthenticator ga = new GoogleAuthenticator();
		GoogleAuthenticatorKey googleAuthenticatorKey = ga.createCredentials();
		SQLQuery query = Main.getSQLQuery();
		try {
			query.updateSQL("INSERT INTO `GOOGLE_AUTH` (`UUID`, `NAME`, `GKEY`) VALUES ('" + uuid + "', '" + name + "', '" + googleAuthenticatorKey.getKey() + "');");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return googleAuthenticatorKey.getKey();
	}

	public static boolean login(String uuid, int id, Player player) {
		boolean is = check(uuid, id);
		logout(uuid);
		if (is) {
			synchronized (SU_USER) {
				SU_USER.add(uuid);
				SuperUserCache.login(uuid, player);
			}
		}
		return is;
	}

	public static boolean isRegistrated(String uuid) {
		String s = getSecKey(uuid);
		if (s == null) {
			return false;
		}
		return true;
	}

	public static void logout(String uuid) {
		synchronized (SU_USER) {
			if (SU_USER.contains(uuid)) {
				SU_USER.remove(uuid);
				SuperUserCache.logout(uuid);
			}
		}

	}

	public static boolean isSuperUser(String uuid) {
		synchronized (SU_USER) {
			return SU_USER.contains(uuid);
		}
	}

	public static boolean isSuperUser(Player p) {
		synchronized (SU_USER) {
			return SU_USER.contains(p.getUniqueId().toString());
		}
	}

	public static String getSecKey(String uuid) {
		SQLQuery query = Main.getSQLQuery();
		try {
			ResultSet rs = query.querySQL("SELECT * FROM `GOOGLE_AUTH` WHERE `UUID` LIKE '" + uuid + "'");
			if (rs.next()) {
				return rs.getString("GKEY");
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static boolean check(String uuid, int id) {
		GoogleAuthenticator authenticator = new GoogleAuthenticator();
		String key = getSecKey(uuid);
		if (key == null) {
			return false;
		}
		return authenticator.authorize(key, id);
	}

}
