package at.ltd.gungame.round.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.sql.sqlutils.SQLCollection;
import at.ltd.adds.utils.GameMath;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.gungame.events.custom.EventGameRoundStop;
import at.ltd.gungame.events.custom.EventGunGameCoinsAsync;

public class RoundEndEvent implements Listener {

	@EventHandler
	public void onDB(EventGameRoundStop e) {
		for (GamePlayer gg : e.getGunGamePlayers()) {
			SQLCollection.getPlayer(gg.getPlayer()).setRoundsPlayed(SQLCollection.getPlayer(gg.getPlayer()).getRoundsPlayed() + 1);
		}
	}

	private static int schedulerKillsID = 0;

	@EventHandler
	public void on(EventGameRoundStop e) {
		if (e.getGunGamePlayers().size() == 0) {
			return;
		}
		ArrayList<GamePlayer> ggpl = (ArrayList<GamePlayer>) e.getGunGamePlayers();
		sort(ggpl);

		giveCoinsToPlayers(e);

		ArrayList<String> kills = new ArrayList<>();
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {

			@Override
			public void run() {
				int i = 1;
				for (GamePlayer ggp : ggpl) {
					kills.add(Cf.rs(Cf.ROUND_RANKING_2, "[PLATZ]", i, "[KILLS]", ggp.getKills(), "[NAME]", ggp.getPlayer().getName()));
					i++;
				}
				schedulerKillsID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
					int i = -1;
					int size = kills.size();
					@Override
					public void run() {
						i++;
						try {
							Bukkit.broadcastMessage(kills.get(i));
						} catch (Exception e) {
						}

						if (i == size) {
							try {
								Bukkit.getScheduler().cancelTask(schedulerKillsID);
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					}
				}, 5, 5);
			}
		}, 65);

	}

	private static void sort(ArrayList<GamePlayer> ggps) {
		Collections.sort(ggps, new Comparator<GamePlayer>() {
			@Override
			public int compare(GamePlayer sec, GamePlayer first) {
				Integer ki1 = first.getKills();
				Integer ki2 = sec.getKills();
				return ki1.compareTo(ki2);
			}
		});
	}

	private static void giveCoinsToPlayers(EventGameRoundStop e) {
		HashMap<Player, Integer> hm = new HashMap<>();
		ArrayList<GamePlayer> ggpl = (ArrayList<GamePlayer>) e.getGunGamePlayers();
		for (GamePlayer ggp : ggpl) {
			Player p = ggp.getPlayer();
			if (SQLCollection.getPlayer(p).getCoinmultiplier() == 0) {
				int coins = GameMath.getReward(ggp.getKills(), ggp.getDeaths(), ggp.getGunGameLevel(), 0);
				Cf.rsS(Cf.ROUND_CASH_OUT_1, ggp.getPlayer(), "[ADDCOINS]", coins);
				if (coins != 0 && coins > 0) {
					hm.put(ggp.getPlayer(), coins);
				}
			} else {
				float multif = SQLCollection.getPlayer(p).getCoinmultiplier();
				int coins = GameMath.getReward(ggp.getKills(), ggp.getDeaths(), ggp.getGunGameLevel(), 0);
				Cf.rsS(Cf.ROUND_CASH_OUT_1, ggp.getPlayer(), "[ADDCOINS]", coins);
				if (coins != 0 && coins > 0) {
					@SuppressWarnings("unused")
					int plus = GameMath.getMultiPlus(ggp.getKills(), ggp.getDeaths(), ggp.getGunGameLevel(), multif);
					coins = GameMath.getReward(ggp.getKills(), ggp.getDeaths(), ggp.getGunGameLevel(), multif);
					Cf.rsS(Cf.ROUND_CASH_OUT_2, ggp.getPlayer(), "[PLUSCOINS]", coins, "[MULTICOINFACTOR]", multif);
					hm.put(ggp.getPlayer(), coins);
				}
			}

		}

		for (Player p : hm.keySet()) {
			int coins = hm.get(p);
			Main.getTransaction().add("END REWARD", coins, p);
			AsyncThreadWorkers.submitWork(new Runnable() {

				@Override
				public void run() {
					EventGunGameCoinsAsync event = new EventGunGameCoinsAsync(p, coins);
					Bukkit.getPluginManager().callEvent(event);
				}
			});

		}

	}

}
