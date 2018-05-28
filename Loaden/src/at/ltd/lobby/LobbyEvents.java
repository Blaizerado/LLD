package at.ltd.lobby;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import at.ltd.adds.Lg;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.adds.utils.time.TimeCounter;
import at.ltd.gungame.events.custom.EventGameRoundStop;
import at.ltd.lobby.ranking.RankingLastVoted;
import at.ltd.lobby.ranking.RankingTopKiller;

public class LobbyEvents implements Listener {

	@EventHandler
	public void on(WeatherChangeEvent e) {
		if (e.getWorld().getName().equals(LobbyUtils.Lobby_Name_String)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void on(EventGameRoundStop e) {
		AsyncThreadWorkers.submitWork(() -> {
			int i = 1;
			if (i == 1) {
				return;
			}
			Lg.lgMSG("UPDATING LOBBY STATS");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			TimeCounter.start(Lobby.rankDB);
			try {
				RankingTopKiller.update();
				RankingLastVoted.update();
			} catch (Exception e1) {
				TimeCounter.stop(Lobby.rankDB);
				e1.printStackTrace();
			}
			TimeCounter.stop(Lobby.rankDB);
		});

	}

	@EventHandler
	public void on(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (LobbyUtils.isInLobby(p)) {
				e.setCancelled(true);
			}
		}
	}

}
