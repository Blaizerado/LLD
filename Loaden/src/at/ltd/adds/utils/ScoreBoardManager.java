package at.ltd.adds.utils;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;

import at.ltd.Main;

public class ScoreBoardManager implements Listener {

	private static Scoreboard scoreboard;

	public static void init() {
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		Main.registerListener(new ScoreBoardManager());
	}

	public static Scoreboard getScoreBoard() {
		return scoreboard;
	}

	@EventHandler(priority = EventPriority.LOW)
	public void on(PlayerJoinEvent e) {
		try {
			Scoreboard sb = ScoreBoardManager.getScoreBoard();
			e.getPlayer().setScoreboard(sb);
		} catch (Exception e2) {
			e2.printStackTrace();
		}

	}

}
