package at.ltd.gungame.guns.events;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import at.ltd.adds.utils.LocUtils;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.adds.utils.visual.DamageIndicator;
import at.ltd.adds.utils.visual.Hologram;
import at.ltd.gungame.guns.events.custom.EventBulletHitPlayerAsync;
import at.ltd.gungame.guns.events.custom.EventProjectileHitAsync;
import at.ltd.gungame.guns.utils.bullet.Bullet;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldEvent;

public class BulletEffectEvent implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onProjectileHit(EventProjectileHitAsync e) {
		Projectile pr = e.getEntity();
		if (pr.getType() == EntityType.SNOWBALL) {
			Block b = e.getHitBlock();
			if (b != null) {
				if (!b.isLiquid()) {
					if (b.getType() == Material.BARRIER) {
						return;
					}
					Bullet bullet = Bullet.getBullet(e.getUUID());
					if (bullet != null) {
						if (bullet.getGun().getGunInterface().getGunID() != 38) {
							for (Player p : LocUtils.getPlayersNearLocation(e.getLocation(), 30)) {
								PacketPlayOutWorldEvent packet = new PacketPlayOutWorldEvent(2001, new BlockPosition(b.getX(), b.getY(), b.getZ()), b.getType().getId(), false);
								((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
							}
						}
					}

				}
			}
		}

	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(EventBulletHitPlayerAsync e) {
		if (e.wasCancelled()) {
			return;
		}

		Player enemy = e.getEnemy();

		Player shooter = e.getShooter();
		double damage = e.getDamage();
		DamageIndicator.show(enemy, shooter, damage);
	}


}
