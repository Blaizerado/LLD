package at.ltd.gungame.guns.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import at.ltd.gungame.guns.events.custom.EventGunShoot;

public class GunShootTimingsEvent implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void on(EventGunShoot e) {
		int GUN_TIME = e.getFirespeed();
		int TIME_GONE = Integer.valueOf((System.currentTimeMillis() - e.getGun().getLastShootTime()) + "");
		if (TIME_GONE < GUN_TIME) {
			e.setCancelled(true);
		} else {
			e.getGun().setLastShootTime(System.currentTimeMillis());
		}

	}

}
