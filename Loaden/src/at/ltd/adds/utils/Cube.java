package at.ltd.adds.utils;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import at.ltd.Main;

public class Cube {

	private Location location1;
	private Location location2;
	private int scheID;
	private HashMap<String, Long> cooldowns;

	@SuppressWarnings("deprecation")
	public Cube(Location location1, Location location2, boolean managed) {
		if (location1.getWorld().getName() != location1.getWorld().getName()) {
			return;
		}
		this.location1 = location1;
		this.location2 = location2;

		if (managed) {
			cooldowns = new HashMap<String, Long>();
			scheID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(Main.getPlugin(), new Runnable() {

				@Override
				public void run() {
					cooldowns.clear();
				}
			}, 0, 480 * 20);
		}

	}

	public boolean isLocationInCube(Location location) {
		boolean x = location.getX() > Math.min(location1.getX(), location2.getX()) && location.getX() < Math.max(location1.getX(), location2.getX());
		boolean y = location.getY() > Math.min(location1.getY(), location2.getY()) && location.getY() < Math.max(location1.getY(), location2.getY());
		boolean z = location.getZ() > Math.min(location1.getZ(), location2.getZ()) && location.getZ() < Math.max(location1.getZ(), location2.getZ());
		return x && y && z;
	}

	public boolean cooldown(Player p, int cooldownTime) {
		if (cooldowns.containsKey(p.getName())) {
			long secondsLeft = ((cooldowns.get(p.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
			if (secondsLeft > 0) {
				return false;
			} else {
				cooldowns.remove(p.getName());
			}
		}
		cooldowns.put(p.getName(), System.currentTimeMillis());
		return true;

	}

	public void stop() {
		Bukkit.getScheduler().cancelTask(scheID);
		cooldowns.clear();
		cooldowns = null;
	}

}
