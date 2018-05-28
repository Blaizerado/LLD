package at.ltd.gungame.round;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import at.ltd.Main;
import at.ltd.adds.sql.sqlutils.stats.round.RoundData;
import at.ltd.adds.sql.sqlutils.stats.round.RoundStatsWatcher;
import at.ltd.adds.utils.ScoreBoardManager;
import at.ltd.adds.utils.config.Config;
import at.ltd.adds.utils.config.ConfigAble;
import at.ltd.gungame.events.custom.EventGameRoundStop;
import at.ltd.gungame.events.custom.EventVoteStart;

public class DisplayStats implements Listener {

	@ConfigAble(key = "", value = "&e&l&nRunden Stats ")
	public static String NAME;
	@ConfigAble(key = "", value = "&6 ➠ &7MapName:&6 ")
	public static String MAPNAME;
	@ConfigAble(key = "", value = "&6 ➠ &7Kills:&6 ")
	public static String KILLS;
	@ConfigAble(key = "", value = "&7Coins ausgeschüttet:&6 ")
	public static String COINS;
	@ConfigAble(key = "", value = "&7Schüsse abgegeben:&6 ")
	public static String SHOOTS;
	@ConfigAble(key = "", value = "&6 ➠ &7Spieleranzahl:&6 ")
	public static String PLAYERCOUNT;

	public static void init() {
		Config.loadMyClass("DisplayStatsScoreboard", DisplayStats.class).setReloadAble(true).setAsyncReloadAble(false).setReloadHandler(() -> {
			Config.loadMyClass("DisplayStatsScoreboard", DisplayStats.class);
			translate();
		});
		translate();
		Main.registerListener(new DisplayStats());
	}

	public static void translate() {
		NAME = ChatColor.translateAlternateColorCodes('&', NAME);
		MAPNAME = ChatColor.translateAlternateColorCodes('&', MAPNAME);
		KILLS = ChatColor.translateAlternateColorCodes('&', KILLS);
		COINS = ChatColor.translateAlternateColorCodes('&', COINS);
		SHOOTS = ChatColor.translateAlternateColorCodes('&', SHOOTS);
		PLAYERCOUNT = ChatColor.translateAlternateColorCodes('&', PLAYERCOUNT);
	}

	@EventHandler
	public void on(EventGameRoundStop e) {
		Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {

			@Override
			public void run() {
				Scoreboard sc = ScoreBoardManager.getScoreBoard();
				Objective obj = sc.registerNewObjective("STATS", "NJAA");
				obj.setDisplaySlot(DisplaySlot.SIDEBAR);
				obj.setDisplayName(NAME);
				RoundData rd = RoundStatsWatcher.currenRound;
				obj.getScore(" ").setScore(5);
				obj.getScore(MAPNAME + rd.getMapName()).setScore(4);
				obj.getScore(KILLS + rd.getKills()).setScore(3);
				obj.getScore(COINS + rd.getCoinsCashOut()).setScore(2);
				obj.getScore(SHOOTS + rd.getShoots()).setScore(1);
				obj.getScore(PLAYERCOUNT + rd.getPlayerCount()).setScore(0);
			}
		}, 5);

	}

	@EventHandler(priority = EventPriority.HIGH)
	public void on(EventVoteStart e) {
		Scoreboard sb = ScoreBoardManager.getScoreBoard();
		if (sb.getObjective("STATS") != null) {
			sb.getObjective("STATS").unregister();
		}
	}

}
