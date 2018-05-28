package at.ltd.gungame.guns.utils.bullet;

import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import at.ltd.Main;
import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.game.player.GamePlayerDamage;
import at.ltd.adds.game.player.fac.BeforeDamageHandler;
import at.ltd.gungame.events.custom.EventGunGameDamage;
import at.ltd.gungame.events.custom.EventGunGameDamage.DamageType;
import at.ltd.gungame.guns.events.custom.EventBulletHitEntity;

public class BulletWatcher implements Listener {

	@EventHandler
	public void on(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Snowball) {
			Snowball s = (Snowball) e.getDamager();
			if (s.getShooter() instanceof Player) {
				if (Bullet.isBulletFlying(s.getUniqueId().toString())) {
					Bullet bullet = Bullet.getBullet(s.getUniqueId().toString());
					if (e.getEntity() instanceof Player) {
						Player p = (Player) e.getEntity();
						if (GamePlayer.getGamePlayer(p).isInRound()) {
							double damage = bullet.hit(p);
							if (damage == 0) {
								e.setCancelled(true);
							} else {
								e.setCancelled(true);
								GamePlayerDamage.damagePlayer(p, bullet.getShooter(), damage, DamageType.GUN_HIT,
										new BeforeDamageHandler() {

											@Override
											public void handle(GamePlayer player, boolean dead) {
												if (dead) {
													bullet.getGun().gotKill(p);
												}
											}
										});

							}
						}
					} else {
						EventBulletHitEntity event = new EventBulletHitEntity((Player) s.getShooter(), e.getEntity(),
								bullet);
						Main.callEvent(event);
					}
				}
			}
		}

	}

	@EventHandler
	public void on(PlayerJoinEvent e) {
		e.getPlayer().setMaximumNoDamageTicks(0);
	}

	@EventHandler
	public void on(EventGunGameDamage e) {
		if (e.getDamagee().equals(e.getDamager())) {
			if (e.getDamageType() == DamageType.GUN_HIT) {
				e.setCancelled(true);
			}
		}
	}

}
