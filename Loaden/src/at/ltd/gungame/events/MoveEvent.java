package at.ltd.gungame.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import at.ltd.adds.Cf;
import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.events.custom.EventPlayerMoveBlockEventAsync;
import at.ltd.gungame.GunGame;

public class MoveEvent implements Listener {

	@EventHandler
	public void onPlayerMove(EventPlayerMoveBlockEventAsync e) {
		GamePlayer ggp = GamePlayer.getGamePlayer(e.getPlayer());
		if (ggp.isInRound()) {
			if (GunGame.isRound()) {
				if (!GunGame.getRoundManager().getGameMap().getSpawns().get(0).getWorld().getName().equals(e.getTo().getWorld().getName())) {
					ggp.sendMessage(Cf.rs(Cf.LEAVE_GAME_NOT_PROPERLY, e.getPlayer()));
					AsyncThreadWorkers.submitSyncWork(() -> ggp.exitGunGame(false));
				}
			}

		}

	}

}
