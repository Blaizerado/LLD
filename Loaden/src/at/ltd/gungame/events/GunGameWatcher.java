package at.ltd.gungame.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import at.ltd.adds.Cf;
import at.ltd.events.custom.EventTeleport;
import at.ltd.events.custom.EventTeleportSpawn;
import at.ltd.gungame.GunGame;
import at.ltd.gungame.events.custom.EventJoinGame;
import at.ltd.gungame.events.custom.EventLeaveGame;

public class GunGameWatcher implements Listener {

	@EventHandler
	public void on(EventJoinGame e) {
		Player p = e.getPlayer();
		if (GunGame.isRound()) {
			if (GunGame.getRoundManager().getGameRound().isPlayerInRound(p)) {
				e.setCancelled(true);
				Cf.rsS(Cf.NOT_POSSIBLE, p);
			}

		} else {
			e.setCancelled(true);
			Cf.rsS(Cf.JOIN_GAME_NOT_STARTED, p);

		}
	}

	@EventHandler
	public void on(EventLeaveGame e) {
		if (!e.getGunGamePlayer().isInRound()) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void on(EventTeleportSpawn e) {
		if (e.getGunGamePlayer().isInRound()) {
			e.getGunGamePlayer().exitGunGame(false);
		}
	}

	@EventHandler
	public void on(EventTeleport e) {
		if (e.getGunGamePlayer().isInRound()) {
			e.getGunGamePlayer().exitGunGame(false);
		}
	}

}
