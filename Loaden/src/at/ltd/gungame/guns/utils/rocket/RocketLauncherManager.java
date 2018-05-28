package at.ltd.gungame.guns.utils.rocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import at.ltd.Main;
import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.game.player.GamePlayerDamage;
import at.ltd.adds.game.player.fac.BeforeDamageHandler;
import at.ltd.adds.utils.LocUtils;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.adds.utils.visual.Particles;
import at.ltd.gungame.events.custom.EventGameRoundStop;
import at.ltd.gungame.events.custom.EventGunGameDamage.DamageType;
import at.ltd.gungame.guns.RIFLES.SETUP.RifleConfigMemory;
import at.ltd.gungame.guns.events.custom.EventBulletHitPlayer;
import at.ltd.gungame.guns.events.custom.EventBulletLaunchAsync;
import at.ltd.gungame.guns.events.custom.EventProjectileHitAsync;
import at.ltd.gungame.guns.utils.Gun;
import at.ltd.gungame.guns.utils.GunInterface;
import at.ltd.gungame.guns.utils.bullet.Bullet;
import at.ltd.gungame.guns.utils.enums.AmmoType;
import at.ltd.gungame.guns.utils.enums.GunType;

public class RocketLauncherManager implements Listener {

	public static ConcurrentHashMap<String, Bullet> FLYING_ROCKETS = new ConcurrentHashMap<>();

	public static void init() {
		new BukkitRunnable() {

			@Override
			public void run() {
				ArrayList<String> remove = new ArrayList<>();
				for (Bullet rocket : FLYING_ROCKETS.values()) {
					Particles.ROCKET_TRACE(rocket.getCurrentBulletLocation());
					
					if (!rocket.isFlying()) {
						remove.add(rocket.getUUID());
					}
				}

				for (String uuid : remove) {
					FLYING_ROCKETS.remove(uuid);
				}

			}
		}.runTaskTimerAsynchronously(Main.getPlugin(), 20, 0);
	}

	@EventHandler
	public void on(EventGameRoundStop e) {
		FLYING_ROCKETS.clear();
	}

	@EventHandler
	public void on(EventBulletLaunchAsync e) {
		Gun gun = e.getGun();
		if (gun.getGunInterface().getGunType() == GunType.ROCKET_LAUNCHER) {
			AsyncThreadWorkers.submitSyncWork(() -> e.getSnowball().setGravity(true));
			FLYING_ROCKETS.put(e.getBullet().getUUID(), e.getBullet());
		}
	}

	@EventHandler
	public void on(EventProjectileHitAsync e) {
		String uuid = e.getUUID();
		if (FLYING_ROCKETS.containsKey(uuid)) {
			Bullet rocket = FLYING_ROCKETS.get(uuid);
			Gun gun = rocket.getGun();
			GunInterface gi = gun.getGunInterface();
			HashMap<String, String> data = RifleConfigMemory.getConfig(gi);
			double radius = 5.00D;
			if (data.containsKey("radius")) {
				radius = Double.valueOf(data.get("radius"));
			}
			Location loc = e.getLocation();
			RocketExplosionEffect.makeEffect(loc, radius, gi);
			List<Player> damaged = LocUtils.getPlayersNearLocation(loc, radius);
			AsyncThreadWorkers.submitSyncWork(() -> {
				for (Player p : damaged) {
					GamePlayerDamage.damagePlayer(p, gun.getPlayer(), gi.getDamage(), DamageType.GUN_HIT, new BeforeDamageHandler() {

						@Override
						public void handle(GamePlayer player, boolean dead) {
							player.setLastDamager(GamePlayer.getGamePlayer(gun.getPlayer()));
							player.setLastDamageWeaponName(gi.getName());
							player.setLastGunHit(gi);
							if (dead) {
								gun.gotKill(p);
							}
						}
					});
				}
			});

			FLYING_ROCKETS.remove(e.getEntity().getUniqueId().toString());
		}
	}

	@EventHandler
	public void on(EventBulletHitPlayer e) {
		if (e.getBullet().getGun().getGunInterface().getAmmoType() == AmmoType.ROCKET) {
			e.setCancelled(true);
		}
	}

}
