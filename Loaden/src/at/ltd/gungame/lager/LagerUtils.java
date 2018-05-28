package at.ltd.gungame.lager;

import java.io.File;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;

import at.ltd.Main;
import at.ltd.adds.sql.SQLQuery;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;

public class LagerUtils {

	public static void unloackFirst(Player player) {

	}

	public static synchronized LagerUnit registerLager(Player p) {
		int x = getX();
		if (x == 0) {
			return null;
		}
		x = x + 50;
		int finalx = x;
		AsyncThreadWorkers.submitSyncWork(() -> {
			try {
				build(finalx);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		addSQL(p.getUniqueId().toString(), x);
		return new LagerUnit(x, p);

	}

	public static void build(int x) throws Exception {
		Vector v = new Vector(x, 30, 85);
		File file = new File("plugins/WorldEdit/schematics/lager.schematic");
		EditSession es = new EditSession(new BukkitWorld(Bukkit.getWorld("GG_Lager")), 999999999);
		try {
			CuboidClipboard cc = CuboidClipboard.loadSchematic(file);
			cc.paste(es, v, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int getX() {
		SQLQuery query = Main.createSQLQuery();
		try {
			ResultSet rs = query.querySQL("SELECT * FROM `LAGER` ORDER BY `LAGER`.`X` DESC LIMIT 1");
			if (rs.next()) {
				return rs.getInt("X");
			} else {
				return 1;
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int getX(Player p) {
		SQLQuery query = Main.createSQLQuery();
		try {
			ResultSet rs = query.querySQL("SELECT * FROM `LAGER` WHERE `UUID` LIKE '" + p.getUniqueId().toString() + "' LIMIT 1");
			if (rs.next()) {
				return rs.getInt("X");
			} else {
				return 1;
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static void addSQL(String uuid, int x) {
		Date date = new Date(System.currentTimeMillis());
		SQLQuery query = Main.createSQLQuery();
		try {
			query.updateSQL("INSERT INTO `LAGER` (`UUID`, `X`, `DATE_CREATE`) VALUES ('" + uuid + "', '" + x + "', '" + date + "');");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
