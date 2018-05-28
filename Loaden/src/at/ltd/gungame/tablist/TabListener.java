package at.ltd.gungame.tablist;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class TabListener implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void on(PlayerJoinEvent e) {
		try {
			TabManager.setPrefix();
		} catch (Exception e2) {
		}

	}

	@EventHandler
	public void on(PlayerQuitEvent e) {
		if (TabManager.players.containsKey(e.getPlayer())) {
			TabManager.players.remove(e.getPlayer());
		}
	}

}
