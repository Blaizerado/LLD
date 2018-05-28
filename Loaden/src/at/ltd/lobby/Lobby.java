package at.ltd.lobby;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import at.ltd.Main;
import at.ltd.adds.Lg;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.adds.utils.time.TimeCounter;
import at.ltd.adds.utils.time.TimeUnit;
import at.ltd.lobby.dailyreward.DailyReward;
import at.ltd.lobby.infoitem.LobbyInfoItem;
import at.ltd.lobby.ranking.RankingLastVoted;
import at.ltd.lobby.ranking.RankingTopKiller;
import at.ltd.lobby.ranking.RankingTopPlayer;
import at.ltd.lobby.ranking.stats.StatsManager;
import at.ltd.lobby.shop.LobbyShop;
import at.ltd.lobby.shop.listener.LobbyJumpPad;

public class Lobby {
	public static TimeUnit rankDB = new TimeUnit(true, 40, "Update Ranking + DB call (Async)");
	public static void start() {

		Bukkit.getServer().getPluginManager().registerEvents(new LobbyEvents(), Main.getPlugin());
		Main.registerListener(new LobbyJumpPad());
		
		
		LobbyUtils.init();

		StatsManager.init();
		RankingTopPlayer.init();

		PortalJoin.start();
		LobbyShop.init();
		PressurePlateGame.init();
		LobbyNightVision.init();
		LobbyEffects.init();
		LobbyBroadcast.init();
		DailyReward.init();
		LobbyInfoItem.init();
		
		AsyncThreadWorkers.submitSchedulerWorkTick(() -> {
			for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
				if (AsyncThreadWorkers.getPlayerLocation(p).getWorld().getName().equals("World")) {
					AsyncThreadWorkers.submitSyncWork(() -> LobbyUtils.tpPlayerToLobby(p));
				}
			}
		}, 10, 10);

	}

	public static void startStats() {
		// RankingLastVoted.start();
		// RankingTopKiller.start();
		// RankingTopPlayers.start();
		AsyncThreadWorkers.submitSchedulerWorkTick(() -> {
			Lg.lgMSG("UPDATING LOBBY STATS");
			TimeCounter.start(rankDB);
			try {
				RankingTopKiller.update();
				RankingLastVoted.update();
			} catch (Exception e) {
				TimeCounter.stop(rankDB);
				e.printStackTrace();
			}
			TimeCounter.stop(rankDB);
		}, 10, 3 * 20 * 60);
	}

}
