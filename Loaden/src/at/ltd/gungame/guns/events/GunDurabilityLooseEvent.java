package at.ltd.gungame.guns.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class GunDurabilityLooseEvent implements Listener {

	@EventHandler
	public void onklick(PlayerItemDamageEvent e) {
		e.setCancelled(true);
	}

}
