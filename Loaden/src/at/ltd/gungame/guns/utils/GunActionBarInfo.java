package at.ltd.gungame.guns.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import at.ltd.Main;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.adds.utils.visual.ActionBar;
import at.ltd.gungame.guns.utils.bullet.BulletUtils;
import at.ltd.gungame.guns.utils.enums.AmmoType;
import at.ltd.gungame.guns.utils.rocket.RocketUtils;

public class GunActionBarInfo implements Listener {

	public static void init() {
		new BukkitRunnable() {

			@Override
			public void run() {
				for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
					ItemStack is = p.getInventory().getItemInMainHand();
					Gun gun = GunUtils.getGunMemoryManaged(is);
					if (gun == null) {
						ActionBar.sendActionBar(p, "");
						continue;
					}
					GunInterface gi = gun.getGunInterface();
					if (!gun.isReloading()) {
						if (gun.getAmmoLeft() == 0) {
							if (gi.getAmmoType() == AmmoType.ROCKET) {
								ActionBar.sendActionBar(p, "§7[§6 " + gun.getGunInterface().getName() + " §7] » §cKeine Raketen");
							} else {
								ActionBar.sendActionBar(p, "§7[§6 " + gun.getGunInterface().getName() + " §7] » §cKeine Munition");
							}

						} else {
							if (gi.getAmmoType() == AmmoType.ROCKET) {
								int ammo = RocketUtils.getRocketCount(p.getInventory());
								ActionBar.sendActionBar(p, "§7[ §6" + gi.getName() + " §7] » §a" + gun.getAmmoLeft() + "§7/§8" + gi.getMagazineSize() + " §7- (§a" + ammo + "§7)");
							} else {
								int ammo = BulletUtils.getBulletCount(p.getInventory());
								ActionBar.sendActionBar(p, "§7[ §6" + gi.getName() + " §7] » §a" + gun.getAmmoLeft() + "§7/§8" + gi.getMagazineSize() + " §7- (§a" + ammo + "§7)");
							}

						}

					}

				}
			}
		}.runTaskTimerAsynchronously(Main.getPlugin(), 4, 4);
		Bukkit.getPluginManager().registerEvents(new GunActionBarInfo(), Main.getPlugin());
	}

	@EventHandler
	public void on(PlayerItemHeldEvent e) {
		Player p = e.getPlayer();
		ItemStack is = e.getPlayer().getInventory().getItem(e.getNewSlot());
		Gun gun = GunUtils.getGunMemoryManaged(is);
		if (gun == null) {
			ActionBar.sendActionBar(p, "");
			return;
		}
		GunInterface gi = gun.getGunInterface();
		if (!gun.isReloading()) {
			if (gun.getAmmoLeft() == 0) {
				if (gi.getAmmoType() == AmmoType.ROCKET) {
					ActionBar.sendActionBar(p, "§7[§6 " + gun.getGunInterface().getName() + " §7] » §cKeine Raketen");
				} else {
					ActionBar.sendActionBar(p, "§7[§6 " + gun.getGunInterface().getName() + " §7] » §cKeine Munition");
				}

			} else {
				if (gi.getAmmoType() == AmmoType.ROCKET) {
					int ammo = RocketUtils.getRocketCount(p.getInventory());
					ActionBar.sendActionBar(p, "§7[ §6" + gi.getName() + " §7] » §a" + gun.getAmmoLeft() + "§7/§8" + gi.getMagazineSize() + " §7- (§a" + ammo + "§7)");
				} else {
					int ammo = BulletUtils.getBulletCount(p.getInventory());
					ActionBar.sendActionBar(p, "§7[ §6" + gi.getName() + " §7] » §a" + gun.getAmmoLeft() + "§7/§8" + gi.getMagazineSize() + " §7- (§a" + ammo + "§7)");
				}
			}

		}

	}

}
