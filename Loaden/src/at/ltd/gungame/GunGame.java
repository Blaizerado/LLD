package at.ltd.gungame;

import org.bukkit.Bukkit;

import at.ltd.Main;
import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.adds.utils.visual.GhostManager;
import at.ltd.gungame.events.BrawlEvent;
import at.ltd.gungame.events.ChatEvent;
import at.ltd.gungame.events.GunGameBroadcastEvent;
import at.ltd.gungame.events.GunGameWatcher;
import at.ltd.gungame.events.HealthEvent;
import at.ltd.gungame.events.HungerEvent;
import at.ltd.gungame.events.ItemDropEvent;
import at.ltd.gungame.events.KillEvent;
import at.ltd.gungame.events.MagmaBlockDamageEvent;
import at.ltd.gungame.events.MoveEvent;
import at.ltd.gungame.events.QuitEvent;
import at.ltd.gungame.events.SpawnProtectionEvent;
import at.ltd.gungame.guns.utils.GunRegister;
import at.ltd.gungame.level.GLevelUtils;
import at.ltd.gungame.ranks.GRankUtils;
import at.ltd.gungame.round.RoundManager;
import at.ltd.gungame.round.broadcast.GameCountdown;
import at.ltd.gungame.round.broadcast.GameMapVote;
import at.ltd.gungame.round.broadcast.GameVoteCountdown;
import at.ltd.gungame.round.events.RoundEndEvent;
import at.ltd.gungame.round.events.RoundStartEvent;
import at.ltd.gungame.round.vote.VoteScoreBoard;
import at.ltd.gungame.tablist.TabListener;
import at.ltd.gungame.tablist.TabManager;
import at.ltd.gungame.utils.GunGameUtils;

public class GunGame {

	private static RoundManager ROUND_MANAGER;
	private static boolean IS_ROUND = false;
	private static GhostManager ghostManager;

	public static void start() {
		try {
			GRankUtils.init();
		} catch (Exception e) {
			e.printStackTrace();
		}

		ghostManager = new GhostManager();

		GunRegister.initGuns();
		GLevelUtils.init();
		GunGameUtils.init();

		Bukkit.getServer().getPluginManager().registerEvents(new ChatEvent(), Main.getPlugin());
		Bukkit.getServer().getPluginManager().registerEvents(new KillEvent(), Main.getPlugin());
		Bukkit.getServer().getPluginManager().registerEvents(new QuitEvent(), Main.getPlugin());
		Bukkit.getServer().getPluginManager().registerEvents(new HungerEvent(), Main.getPlugin());
		Bukkit.getServer().getPluginManager().registerEvents(new ItemDropEvent(), Main.getPlugin());
		Bukkit.getServer().getPluginManager().registerEvents(new MoveEvent(), Main.getPlugin());
		Bukkit.getServer().getPluginManager().registerEvents(new GunGameWatcher(), Main.getPlugin());
		Bukkit.getServer().getPluginManager().registerEvents(new TabListener(), Main.getPlugin());
		Bukkit.getServer().getPluginManager().registerEvents(new RoundStartEvent(), Main.getPlugin());
		Bukkit.getServer().getPluginManager().registerEvents(new GunGameBroadcastEvent(), Main.getPlugin());
		Bukkit.getServer().getPluginManager().registerEvents(new HealthEvent(), Main.getPlugin());
		Bukkit.getServer().getPluginManager().registerEvents(new HealthEvent(), Main.getPlugin());
		Bukkit.getServer().getPluginManager().registerEvents(new BrawlEvent(), Main.getPlugin());
		Bukkit.getServer().getPluginManager().registerEvents(new MagmaBlockDamageEvent(), Main.getPlugin());

		Main.registerListener(new SpawnProtectionEvent());
		Main.registerListener(new RoundEndEvent());
		

		Bukkit.getServer().getPluginManager().registerEvents(new GameCountdown(), Main.getPlugin());
		Bukkit.getServer().getPluginManager().registerEvents(new GameVoteCountdown(), Main.getPlugin());
		Bukkit.getServer().getPluginManager().registerEvents(new GameMapVote(), Main.getPlugin());

		TabManager.start();
		ROUND_MANAGER = new RoundManager();
		ROUND_MANAGER.start();

		VoteScoreBoard.init();

		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), () -> {
			AsyncThreadWorkers.getOnlinePlayers().forEach(p -> p.setLevel(GamePlayer.getGamePlayer(p).getGunGameLevel()));
		}, 40, 5);

	}

	public static RoundManager getRoundManager() {
		return ROUND_MANAGER;
	}

	public static boolean isRound() {
		return IS_ROUND;
	}

	public static void setRoundStatus(Boolean isrunning) {
		IS_ROUND = isrunning;
	}

	public static GhostManager getGhostManager() {
		return ghostManager;
	}

}
