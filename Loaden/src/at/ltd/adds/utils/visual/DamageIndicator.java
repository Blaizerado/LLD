package at.ltd.adds.utils.visual;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;

public class DamageIndicator {

	public static void show(Player enemy, Player shooter, double damage) {
		damage = damage * 5;
		Location loc = getRandom(AsyncThreadWorkers.getPlayerLocation(enemy), 0.5D);
		Hologram holo = new Hologram(loc, new ItemStack(Material.SNOW_BALL), "§c-" + damage);
		holo.send(shooter);
		AsyncThreadWorkers.submitDelayedWorkSec(() -> holo.destroy(shooter), 3);
	}

	public static Location getRandom(Location center, double radius) {
		World world = center.getWorld();
		int am = ThreadLocalRandom.current().nextInt(0, 100);
		double increment = (2 * Math.PI) / am;
		double y = center.getY();
		int i = ThreadLocalRandom.current().nextInt(0, 360);
		y = ThreadLocalRandom.current().nextDouble(y - 1.4D, y + 0.3D);
		double angle = i * increment;
		double x = center.getX() + (radius * Math.cos(angle));
		double z = center.getZ() + (radius * Math.sin(angle));
		return new Location(world, x, y, z);
	}

}
