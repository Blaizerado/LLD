package at.ltd.lobby.shop.reflections;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import at.ltd.events.custom.EventPlayerJoinAsync;

public class PacketReaderEvent implements Listener {

	@EventHandler
	public void on(EventPlayerJoinAsync e) {
		new PacketReader(e.getPlayer()).inject();
	}

	@EventHandler
	public void on(PlayerQuitEvent e) {
		PacketReader.READERS.get(e.getPlayer()).uninject();
	}

}
