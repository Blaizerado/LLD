package at.ltd.gungame.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class HealthEvent implements Listener {

	@EventHandler
	public void on(EntityRegainHealthEvent e) {
		Entity entity = e.getEntity();
		if (entity instanceof Player) {
			e.setCancelled(true);
			e.setAmount(0);
		}
	}

}
