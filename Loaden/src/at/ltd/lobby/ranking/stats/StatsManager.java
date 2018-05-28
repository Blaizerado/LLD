package at.ltd.lobby.ranking.stats;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import at.ltd.Main;
import at.ltd.adds.sql.SQLQuery;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.adds.utils.time.events.EventMonth;
import at.ltd.events.custom.EventPlayerJoinAsync;
import at.ltd.gungame.events.custom.EventGunGameDeath;

public class StatsManager implements Listener {

	public static void init() {
		Bukkit.getServer().getPluginManager().registerEvents(new StatsManager(), Main.getPlugin());
	}

	public static ArrayList<PlayerStats> getBestPlayers(int limit) {
		ArrayList<PlayerStats> list = new ArrayList<>();
		boolean done = false;
		while (!done) {
			try {
				list.clear();
				SQLQuery query = Main.getSQLQuery();
				ResultSet rs = query.querySQL("SELECT * FROM `MONTH_STATS` ORDER BY `MONTH_STATS`.`KILLS` DESC LIMIT " + limit);
				while (rs.next()) {
					PlayerStats ps = new PlayerStats();
					ResultSet rspl = query.querySQL("SELECT * FROM `MC` WHERE `UUID` LIKE '" + rs.getString("UUID") + "';");
					if (rspl.next()) {
						ps.setName(rspl.getString("NAME"));
					} else {
						ps.setName("Unknown");
					}
					ps.setDeaths(rs.getInt("DEATHS"));
					ps.setKills(rs.getInt("KILLS"));
					ps.setUUID(rs.getString("UUID"));
					list.add(ps);
				}
				done = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	@EventHandler
	public void on(EventMonth e) {
		AsyncThreadWorkers.submitWork(() -> {
			try {
				SQLQuery query = Main.getSQLQuery();
				query.updateSQL("TRUNCATE MONTH_STATS");
				for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
					query.updateSQL("INSERT INTO `MONTH_STATS` (`UUID`, `KILLS`, `DEATHS`) VALUES ('" + p.getUniqueId().toString() + "', '0', '0');");
				}
				ResultSet rs = query.querySQL("SELECT * FROM `MC` ORDER BY `MC`.`KILLS` DESC LIMIT 100");
				while (rs.next()) {
					if (!Main.isOnlineUUID(rs.getString("UUID"))) {
						query.updateSQL("INSERT INTO `MONTH_STATS` (`UUID`, `KILLS`, `DEATHS`) VALUES ('" + rs.getString("UUID") + "', '0', '0');");
					}
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		});
	}

	public static String getCurrentDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM");
		Date currentTime = new Date();
		return formatter.format(currentTime);
	}

	@EventHandler
	public void on(EventGunGameDeath e) {
		AsyncThreadWorkers.submitWork(() -> {
			try {
				SQLQuery query = Main.getSQLQuery();
				if (e.getKiller() != null) {

					ResultSet rs = query.querySQL("SELECT * FROM `MONTH_STATS` WHERE `UUID` LIKE '" + e.getKiller().getUniqueId().toString() + "'");
					if (rs.next()) {
						int kills = rs.getInt("KILLS");
						kills++;
						query.updateSQL("UPDATE `MONTH_STATS` SET `KILLS`= '" + kills + "' WHERE `UUID` LIKE '" + e.getKiller().getUniqueId().toString() + "'");
					}
				}
				if (e.getKilled() != null) {
					ResultSet rs = query.querySQL("SELECT * FROM `MONTH_STATS` WHERE `UUID` LIKE '" + e.getKilled().getUniqueId().toString() + "'");
					if (rs.next()) {
						int deaths = rs.getInt("DEATHS");
						deaths++;
						query.updateSQL("UPDATE `MONTH_STATS` SET `DEATHS`= '" + deaths + "' WHERE `UUID` LIKE '" + e.getKilled().getUniqueId().toString() + "'");
					}
				}

			} catch (Exception e2) {
				e2.printStackTrace();
			}

		});
	}

	@EventHandler
	public void on(EventPlayerJoinAsync e) {
		try {
			SQLQuery query = Main.getSQLQuery();
			if (!query.querySQL("SELECT * FROM `MONTH_STATS` WHERE `UUID` LIKE '" + e.getPlayer().getUniqueId().toString() + "'").next()) {
				query.updateSQL("INSERT INTO `MONTH_STATS` (`UUID`, `KILLS`, `DEATHS`) VALUES ('" + e.getPlayer().getUniqueId().toString() + "', '0', '0');");
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}

	}

}
