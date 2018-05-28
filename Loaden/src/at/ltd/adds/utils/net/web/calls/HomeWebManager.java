package at.ltd.adds.utils.net.web.calls;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import at.ltd.Main;
import at.ltd.adds.sql.sqlutils.stats.round.RoundStatsWatcher;
import at.ltd.adds.utils.net.web.Web;
import at.ltd.adds.utils.net.web.WebData;
import at.ltd.events.custom.EventConfigReload;
import at.ltd.gungame.GameUtils;
import at.ltd.gungame.GameUtils.GameStatus;
import at.ltd.gungame.events.custom.EventGameRoundStart;
import at.ltd.gungame.events.custom.EventGameRoundStop;
import at.ltd.gungame.maps.maputils.GameMap;

public class HomeWebManager implements Listener {

	public static GameMap gamemap;
	public static GameStatus gameStatus;
	public static int player = 0;
	public static int coins = 0;
	public static int kills = 0;
	public static int shoots = 0;

	public static String statsHTML;
	public static String playerHTML;

	public static String status_voting_HTML;
	public static String status_waiting_HTML;
	public static String status_map_HTML;

	public static void init() {

		statsHTML = Web.readHtmlFile("stats.html");
		playerHTML = Web.readHtmlFile("player.html");
		status_voting_HTML = Web.readHtmlFile("status_voting.html");
		status_waiting_HTML = Web.readHtmlFile("status_wait.html");
		status_map_HTML = Web.readHtmlFile("status_map.html");

		GameStatus gm = GameUtils.getServerStatus();
		gameStatus = gm;

		Main.registerListener(new HomeWebManager());
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {

			@Override
			public void run() {
				GameStatus gm = GameUtils.getServerStatus();
				if (gm == gameStatus.MATCH) {
					gamemap = GameUtils.getMap();
					if (RoundStatsWatcher.currenRound != null) {
						coins = RoundStatsWatcher.currenRound.getCoinsCashOut();
						kills = RoundStatsWatcher.currenRound.getKills();
						shoots = RoundStatsWatcher.currenRound.getShoots();
					}
				}
				gameStatus = gm;
				player = Bukkit.getOnlinePlayers().size();

			}
		}, 0, 10);
	}

	public static String getStatus() {
		if (gameStatus == GameStatus.MATCH) {
			String string = new String(status_map_HTML);
			string = string.replace("[MAP]", ChatColor.stripColor(gamemap.getMapName()));
			return string;
		} else if (gameStatus == GameStatus.VOTE) {
			String string = new String(status_voting_HTML);
			return string;
		} else {
			String string = new String(status_waiting_HTML);
			return string;
		}
	}

	public static String getStats() {
		String string = new String(statsHTML);
		string = string.replace("[KILLS]", kills + "");
		string = string.replace("[COINS]", coins + "");
		string = string.replace("[SHOOTS]", shoots + "");
		return string;
	}

	public static String getPlayers() {
		String string = new String(playerHTML);
		return string = string.replace("[PLAYERS]", player + "");
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(EventGameRoundStop e) {
		kills = 0;
		coins = 0;
		shoots = 0;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(EventGameRoundStart e) {
		kills = 0;
		coins = 0;
		shoots = 0;
	}

	@EventHandler
	public void on(EventConfigReload e) {
		statsHTML = Web.readHtmlFile("stats.html");
		playerHTML = Web.readHtmlFile("player.html");
		status_voting_HTML = Web.readHtmlFile("status_voting.html");
		status_waiting_HTML = Web.readHtmlFile("status_wait.html");
		status_map_HTML = Web.readHtmlFile("status_map.html");
	}

}
