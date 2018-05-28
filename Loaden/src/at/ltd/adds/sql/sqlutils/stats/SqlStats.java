package at.ltd.adds.sql.sqlutils.stats;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import at.ltd.Main;
import at.ltd.adds.sql.SQLQuery;
import at.ltd.adds.sql.sqlutils.stats.round.RoundData;
import at.ltd.adds.sql.sqlutils.stats.round.RoundStatsWatcher;

public class SqlStats implements Listener {

	public static void init() {
		if (RoundStatsWatcher.currenRound == null) {
			int id = 0;
			SQLQuery query = Main.getSQLQuery();
			try {
				ResultSet rs = query.querySQL("SELECT * FROM `ROUND_STATS` ORDER BY `ROUND_STATS`.`ROUNDID` DESC LIMIT 1");
				if (rs.next()) {
					id = rs.getInt("ROUNDID") + 1;
				}
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			RoundStatsWatcher.currenRound = new RoundData(id, "NO");
		}
		Bukkit.getPluginManager().registerEvents(new RoundStatsWatcher(), Main.getPlugin());
		

	}

}
