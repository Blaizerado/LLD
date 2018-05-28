package at.ltd.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.events.custom.EventPlayerMoveBlockEventAsync;
import at.ltd.lobby.LobbyUtils;

public class EventMoveDeath implements Listener {

	@EventHandler
	public void on(EventPlayerMoveBlockEventAsync e) {
		if (e.getTo().getBlockY() < -60) {
			AsyncThreadWorkers.submitSyncWork(() -> {
				LobbyUtils.InitPlayerJoin(e.getPlayer());
			});
		}
	}

}
