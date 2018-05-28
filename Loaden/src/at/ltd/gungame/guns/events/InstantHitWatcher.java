package at.ltd.gungame.guns.events;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.adds.utils.visual.Particles;
import at.ltd.gungame.guns.events.custom.EventBulletLaunchAsync;
import at.ltd.gungame.guns.events.custom.EventGunShoot;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;

public class InstantHitWatcher implements Listener {

	@EventHandler
	public void on(EventGunShoot e) {
		if (e.getGun().getGunInterface().isInstantHit()) {
			e.setBulletSpeed(300F);
		}
	}

	@EventHandler
	public void on(EventBulletLaunchAsync e) {
		if (e.getGun().getGunInterface().isInstantHit()) {
			int id = e.getSnowball().getEntityId();
			for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
				PacketPlayOutEntityDestroy pack = new PacketPlayOutEntityDestroy(id);
				((CraftPlayer) p).getHandle().playerConnection.sendPacket(pack);
			}

			Location loc = e.getBullet().getLocFromPlayerEye();
			Vector v = e.getBullet().getBulletVector();
			displayParticles(loc, v);

			AsyncThreadWorkers.submitRepeatingTickWork(new Runnable() {
				@Override
				public void run() {
					displayParticles(loc, v);
				}
			}, 7);

		}
	}

	public void displayParticles(Location playerloc, Vector v) {
		Location start = playerloc.clone();
		Vector dir = v.clone();
		for (double i = 1.50001; i < 1000; i += 0.7) {
			dir.multiply(i);
			start.add(dir);
			if (start.getBlock().getType().isSolid()) {
				break;
			}
			Particles.BULLET_TRACE(start);
			start.subtract(dir);
			dir.normalize();
		}

	}

}
