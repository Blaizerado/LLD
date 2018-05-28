package at.ltd.gungame.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HungerEvent implements Listener {

	@EventHandler
	public void on(FoodLevelChangeEvent e) {
		if (e.getEntity() instanceof Player) {
			e.setFoodLevel(20);
			e.setCancelled(true);
		}

	}

}
