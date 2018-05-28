package at.ltd.gungame.guns.events;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import at.ltd.Main;
import at.ltd.adds.utils.visual.ActionBar;
import at.ltd.gungame.events.custom.EventGameRoundStop;
import at.ltd.gungame.events.custom.EventGunGameLevelSet;
import at.ltd.gungame.guns.events.custom.EventGunReloadAsync;
import at.ltd.gungame.guns.events.custom.EventGunShoot;
import at.ltd.gungame.guns.events.custom.EventGunShootAsync;
import at.ltd.gungame.guns.utils.Gun;
import at.ltd.gungame.guns.utils.GunUtils;
import at.ltd.gungame.guns.utils.bullet.BulletUtils;
import at.ltd.gungame.guns.utils.enums.AmmoType;
import at.ltd.gungame.guns.utils.rocket.RocketUtils;

public class ShootReloadEvent implements Listener {

	public static ConcurrentHashMap<Gun, Long> RELOAD_GUNS = new ConcurrentHashMap<>();

	@EventHandler
	public void onAmmo(EventGunShoot e) {
		Gun gun = e.getGun();
		if (gun.isReloading()) {
			e.setCancelled(true);
		}

		if (gun.getAmmoLeft() == 0) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onAmmoReload(EventGunShootAsync e) {
		Gun gun = e.getGun();
		Player p = e.getPlayer();
		if (gun.getAmmoLeft() == 0) {
			if (!RELOAD_GUNS.containsKey(gun)) {
				if (gun.getGunInterface().getAmmoType() == AmmoType.ROCKET) {
					int INV_ROCK = RocketUtils.getRocketCount(p.getInventory());
					if (INV_ROCK != 0) {
						int[] result = getAmmo(gun.getGunInterface().getMagazineSize(), gun.getAmmoLeft(), INV_ROCK);
						int MAG_ROCK = result[0];
						int INV_REMOVE = result[1];
						RocketUtils.removeRockets(p.getInventory(), INV_REMOVE);
						gun.setAmmoLeft(MAG_ROCK);
						gun.setReloadingStatus(true);
						callEvent(gun);
						RELOAD_GUNS.put(gun, System.currentTimeMillis());
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {

							@Override
							public void run() {
								if (p.isOnline()) {
									p.getWorld().playSound(p.getLocation(), gun.getGunInterface().getReloadSound(), 1, 10);
								}
							}
						}, 5);

					}
				} else {
					int INV_BULL = BulletUtils.getBulletCount(p.getInventory());
					if (INV_BULL != 0) {
						int[] result = getAmmo(gun.getGunInterface().getMagazineSize(), gun.getAmmoLeft(), INV_BULL);
						int MAG_BULL = result[0];
						int INV_REMOVE = result[1];
						BulletUtils.removeBullets(p.getInventory(), INV_REMOVE);
						gun.setAmmoLeft(MAG_BULL);
						gun.setReloadingStatus(true);
						callEvent(gun);
						RELOAD_GUNS.put(gun, System.currentTimeMillis());
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {

							@Override
							public void run() {
								if (p.isOnline()) {
									p.getWorld().playSound(p.getLocation(), gun.getGunInterface().getReloadSound(), 1, 10);
								}
							}
						}, 5);

					}
				}

			}
		}
	}

	@EventHandler
	public void on(EventGameRoundStop e) {
		for (Gun gun : Gun.getGunList().values()) {
			gun.setAmmoLeft(gun.getGunInterface().getMagazineSize());
		}
	}

	@EventHandler
	public void on(PlayerSwapHandItemsEvent e) {
		e.setCancelled(true);
		ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
		if (GunUtils.isGun(is)) {
			Gun gun = GunUtils.getGunMemory(is);
			if (gun != null) {
				if (!gun.isReloading()) {
					if (gun.getGunInterface().getAmmoType() == AmmoType.ROCKET) {
						Player p = e.getPlayer();
						int INV_ROCK = RocketUtils.getRocketCount(p.getInventory());
						if (INV_ROCK == 0 | gun.getAmmoLeft() == gun.getGunInterface().getMagazineSize()) {
							return;
						}
						int[] result = getAmmo(gun.getGunInterface().getMagazineSize(), gun.getAmmoLeft(), INV_ROCK);
						int MAG_ROCK = result[0];
						int INV_REMOVE = result[1];
						RocketUtils.removeRockets(p.getInventory(), INV_REMOVE);
						gun.setAmmoLeft(MAG_ROCK);
						callEvent(gun);
						gun.setReloadingStatus(true);
						RELOAD_GUNS.put(gun, System.currentTimeMillis());
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {

							@Override
							public void run() {
								if (p.isOnline()) {
									p.getWorld().playSound(p.getLocation(), gun.getGunInterface().getReloadSound(), 1, 10);
								}
							}
						}, 5);
					} else {
						Player p = e.getPlayer();
						int INV_BULL = BulletUtils.getBulletCount(p.getInventory());
						if (INV_BULL == 0 | gun.getAmmoLeft() == gun.getGunInterface().getMagazineSize()) {
							return;
						}
						int[] result = getAmmo(gun.getGunInterface().getMagazineSize(), gun.getAmmoLeft(), INV_BULL);
						int MAG_BULL = result[0];
						int INV_REMOVE = result[1];
						BulletUtils.removeBullets(p.getInventory(), INV_REMOVE);
						gun.setAmmoLeft(MAG_BULL);
						callEvent(gun);
						gun.setReloadingStatus(true);
						RELOAD_GUNS.put(gun, System.currentTimeMillis());
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {

							@Override
							public void run() {
								if (p.isOnline()) {
									p.getWorld().playSound(p.getLocation(), gun.getGunInterface().getReloadSound(), 1, 10);
								}
							}
						}, 5);
					}

				}
			}
		}
	}

	public ShootReloadEvent init() {
		new BukkitRunnable() {
			public void run() {
				ArrayList<Gun> remove = new ArrayList<>();
				for (Gun gun : RELOAD_GUNS.keySet()) {
					Player p = gun.getPlayer();
					if (!p.isOnline()) {
						remove.add(gun);
						continue;
					}
					if (isDone(gun)) {
						gun.setReloadTime(gun.getGunInterface().getReloadTime());
						gun.setReloadingStatus(false);
						gun.updateSerialNumberData();
						remove.add(gun);
						continue;
					} else {
						if (isGun(p.getInventory().getItemInMainHand(), gun)) {
							String time = convert((RELOAD_GUNS.get(gun) - System.currentTimeMillis()) + gun.getReloadTime()).replace(",", "§7,§6");
							ActionBar.sendActionBar(p, "§cRELOADING §7» §6" + time + " Sekunden");

						}
					}
				}

				for (Gun gun : remove) {
					RELOAD_GUNS.remove(gun);
				}
			}
		}.runTaskTimerAsynchronously(Main.getPlugin(), 0, 0);
		return this;
	}

	private static DecimalFormat df = new DecimalFormat("##0.00");
	public static String convert(double milis) {
		double seconds = milis / 1000.0;
		return df.format(seconds).replace(".", ",");
	}

	public static boolean isGun(ItemStack is, Gun gun) {
		if (is != null) {
			if (GunUtils.isGun(is)) {
				Gun g = GunUtils.getGunMemory(is);
				if (g != null) {
					if (g.getUUID().equals(gun.getUUID())) {
						return true;
					}
				}

			}
		}
		return false;
	}

	public boolean isDone(Gun gun) {
		return ((System.currentTimeMillis() - RELOAD_GUNS.get(gun)) > gun.getReloadTime());
	}

	public static int[] getAmmo(int magsize, int ammoleft, int totalammo) {
		int[] val = new int[2];
		if (ammoleft == magsize) {
			val[0] = magsize;
			val[1] = 0;
			return val;
		}

		if (ammoleft == 0) {
			val[0] = ammoleft;
			val[1] = 0;
		}

		int left = magsize - ammoleft;
		int fillammo = 0;
		if (totalammo > left) {
			fillammo = totalammo - left;
			val[0] = magsize;
			val[1] = left;
			return val;
		} else if (totalammo == left) {
			val[0] = magsize;
			val[1] = left;
			return val;
		} else if (totalammo < left) {
			fillammo = ammoleft + totalammo;
		}
		val[0] = fillammo;
		val[1] = fillammo;
		return val;
	}

	@EventHandler
	public void on(EventGunGameLevelSet e) {
		for (ItemStack is : e.getPlayer().getInventory()) {
			if (is != null) {
				if (is.getType() != Material.AIR) {
					if (GunUtils.isGun(is)) {
						Gun gun = GunUtils.getGunMemory(is);
						if (gun != null) {
							gun.setReloadingStatus(false);
							RELOAD_GUNS.remove(gun);
						}
					}
				}
			}
		}
	}

	public int callEvent(Gun gun) {
		EventGunReloadAsync reload = new EventGunReloadAsync(gun);
		Main.callEvent(reload);
		return reload.getReloadTime();
	}

}
