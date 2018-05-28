package at.ltd.gungame.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class MagmaBlockDamageEvent implements Listener {

	@EventHandler
	public void on(EntityDamageEvent e) {
		if (e.getCause() == DamageCause.HOT_FLOOR) {
			e.setCancelled(true);
		}
	}

}
