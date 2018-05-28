package at.ltd.gungame.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;

public class QuitEvent implements Listener {

	@EventHandler
	public void on(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		GamePlayer.getGamePlayer(p).exitGunGame(false);
		AsyncThreadWorkers.submitDelayedWorkSec(() -> GamePlayer.removeGamePlayer(p), 1);
	}

}
