package at.ltd.gungame.guns.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.gungame.guns.events.custom.EventGunShoot;
import at.ltd.gungame.guns.events.custom.EventGunShootAsync;
import at.ltd.gungame.guns.events.custom.EventMousePressTick;
import at.ltd.gungame.guns.utils.Gun;
import at.ltd.gungame.guns.utils.GunUtils;

public class ShootEvent implements Listener {

	@EventHandler
	public void on(EventMousePressTick e) {
		Player p = e.getPlayer();
		ItemStack is = p.getInventory().getItemInMainHand();
		if (is == null) {
			return;
		}
		if (!GunUtils.isGun(is)) {
			return;
		}
		if (!GunUtils.isRegistrated(is)) {
			return;
		}
		Gun gun = GunUtils.getGunMemory(is);
		if (gun == null) {
			return;
		}
		gun.setItemStack(is);

		EventGunShoot event = new EventGunShoot(p, gun);
		Bukkit.getPluginManager().callEvent(event);
		AsyncThreadWorkers.submitWork(new Runnable() {

			@Override
			public void run() {
				EventGunShootAsync eventAsync = new EventGunShootAsync(p, event);
				Bukkit.getPluginManager().callEvent(eventAsync);
			}
		});
		if (event.isCancelled()) {
			return;
		}
		event.getGun().shoot(event.getBulletAccuracy(), event.getBulletSpeed(), event.getVolume(), event.getBulletsPerTick(), event.getShootSound(), event.getDamage(), event.getBulletFlyTime());

	}

}
