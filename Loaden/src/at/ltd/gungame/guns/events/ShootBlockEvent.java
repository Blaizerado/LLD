package at.ltd.gungame.guns.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import at.ltd.Main;
import at.ltd.adds.superuser.SuperUser;
import at.ltd.gungame.guns.events.custom.EventGunShoot;

public class ShootBlockEvent implements Listener {

	@EventHandler
	public void on(EventGunShoot e) {
		Player p = e.getPlayer();
		if (!SuperUser.isSuperUser(p)) {
			if (!Main.isInRound(p)) {
				e.setCancelled(true);
			}
		}

	}
}
