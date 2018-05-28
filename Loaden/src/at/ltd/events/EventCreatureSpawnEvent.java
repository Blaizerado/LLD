package at.ltd.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class EventCreatureSpawnEvent implements Listener {

	@EventHandler
	public void on(CreatureSpawnEvent e) {
		e.setCancelled(true);
	}

}
