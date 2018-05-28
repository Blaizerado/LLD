package at.ltd.adds.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;

/**
 * All methods are thread safe
 * 
 * @author NyroForce
 * @since 06.12.2017
 * @version 1.0.1
 */
public class LocUtils {

	/**
	 * Turns a String to a Location. Input string format = X*Y*Z*YAW*PITCH
	 * 
	 * @param input
	 * @return {@link Location}
	 */
	public static Location locationByString(String input) {
		String[] parts = input.split("\\*");
		Double x = Double.parseDouble(parts[0]);
		Double y = Double.parseDouble(parts[1]);
		Double z = Double.parseDouble(parts[2]);
		Float yaw = Float.valueOf(parts[3]);
		Float pitch = Float.valueOf(parts[4]);
		String world = parts[5];
		return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
	}

	/**
	 * Turns a Location to a Strung. Output string format = X*Y*Z*YAW*PITCH
	 * 
	 * @param loc
	 * @return {@link String}
	 */
	public static String locationToString(Location loc) {
		String s = loc.getX() + "*" + loc.getY() + "*" + loc.getZ() + "*" + loc.getYaw() + "*" + loc.getPitch() + "*" + loc.getWorld().getName();
		return s;

	}

	/**
	 * Turns a string of locstrings to arraylist Splitted by: |
	 * 
	 * @param data
	 * @return
	 */
	public static ArrayList<Location> locationListStringToList(String data) {
		ArrayList<Location> locs = new ArrayList<>();
		for (String s : data.split(Pattern.quote("|"))) {
			locs.add(locationByString(s));
		}
		return locs;
	}

	/**
	 * Checks if a player(s) is near the location.
	 * 
	 * @param loc
	 * @param radius
	 * @return {@link Boolean}
	 */
	public static Boolean isPlayerNearLocation(Location loc, int radius) {
		List<Player> e = getPlayersNearLocation(loc, radius);
		if (e.isEmpty()) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * Returns a {@link Location} without a player next to it. In case no
	 * {@link Location} is free it returns a random {@link Location}.
	 * 
	 * @param locs
	 * @param radius
	 * @return {@link Location}
	 */
	public static Location getLocWithoutPlayers(ArrayList<Location> locs, int radius) {
		ArrayList<Location> free = new ArrayList<>();
		locs.forEach(loc -> {
			if (!isPlayerNearLocation(loc, radius)) {
				free.add(loc);
			}
		});

		if (!free.isEmpty()) {
			return getRandomLoc(free);
		}
		return getRandomLoc(locs);

	}

	/**
	 * Returns a random Location from a {@link Location} {@link ArrayList}.
	 * 
	 * @param list
	 * @return {@link ArrayList}
	 */
	public static Location getRandomLoc(ArrayList<Location> list) {
		Random random = new Random();
		int listSize = list.size();
		int randomIndex = random.nextInt(listSize);
		return list.get(randomIndex);
	}

	/**
	 * Checks if it is the same {@link Location}. Checks only XYZ.
	 * 
	 * @param loc1
	 * @param loc2
	 * @return {@link Boolean}
	 */
	public static boolean isSameLocationXYZ(Location loc1, Location loc2) {
		boolean b = true;
		if (b) {
			if (loc1.getX() != loc2.getX()) {
				b = false;
			}
		}
		if (b) {
			if (loc1.getY() != loc2.getY()) {
				b = false;
			}
		}
		if (b) {
			if (loc1.getZ() != loc2.getZ()) {
				b = false;
			}
		}
		return b;
	}

	/**
	 * Checks if both locations are in the same world.
	 * 
	 * @param loc1
	 * @param loc2
	 * @return
	 */
	public static boolean isSameWorld(Location loc1, Location loc2) {
		return loc1.getWorld().equals(loc2.getWorld());
	}

	/**
	 * Returns the {@link Location} with the furthest away player.
	 * 
	 * @param locs
	 * @return {@link Location}
	 */
	public static Location getBestLocationWithoutPlayer(ArrayList<Location> locs) {
		Location best = null;
		double dis = Double.MIN_VALUE;
		HashMap<Location, Double> hm = convertListToMapWithNearestPlayer(locs);
		for (Location loc : hm.keySet()) {
			double cur = hm.get(loc);
			if (cur > dis) {
				best = loc;
				dis = cur;
			}
		}

		if (dis == Double.MIN_VALUE) {
			return getRandomLoc(locs);
		}
		return best;
	}

	/**
	 * Returns a {@link List} with players near the {@link Location}.
	 * 
	 * @param loc
	 * @param radius
	 * @return {@link List}
	 */

	public static List<Player> getPlayersNearLocation(Location loc, double radius) {
		List<Player> res = new ArrayList<Player>();
		double d2 = radius * radius;
		AsyncThreadWorkers.getOnlinePlayers().forEach(p -> {
			Location locP = AsyncThreadWorkers.getPlayerLocation(p);
			if (locP != null) {
				if (locP.getWorld() == loc.getWorld() && locP.distanceSquared(loc) <= d2) {
					res.add(p);
				}
			}
		});

		return res;
	}

	/**
	 * Converts {@link ArrayList} of {@link Location} to {@link HashMap} with
	 * key {@link Location} and value {@link Double} by the nearest
	 * {@link Player}.
	 * 
	 * @param list
	 * @return {@link HashMap}
	 */
	public static HashMap<Location, Double> convertListToMapWithNearestPlayer(ArrayList<Location> list) {
		HashMap<Location, Double> hm = new HashMap<>();
		list.forEach(loc -> hm.put(loc, getDistanceToNearestPlayer(loc)));
		return hm;
	}

	/**
	 * Returns the distance to the nearest {@link Player}.
	 * 
	 * @param loc
	 * @return {@link Double}
	 */
	public static double getDistanceToNearestPlayer(Location loc) {
		double near = Double.MAX_VALUE;
		for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
			Location locP = AsyncThreadWorkers.getPlayerLocation(p);
			if (locP == null) {
				continue;
			}
			if (locP.getWorld() == loc.getWorld()) {
				double thisp = locP.distanceSquared(loc);
				if (thisp < near) {
					near = thisp;
				}
			}
		}
		return near;
	}

	/**
	 * Returns the Vector a {@link Entity} needs to fly to the next
	 * {@link Vector}.
	 * 
	 * @param from
	 * @param to
	 * @param heightGain
	 * @return {@link Vector}
	 */
	public static Vector calculateVelocity(Vector from, Vector to, int heightGain) {

		double gravity = 0.115;

		int endGain = to.getBlockY() - from.getBlockY();
		double horizDist = Math.sqrt(distanceSquaredVector(from, to));

		int gain = heightGain;

		double maxGain = gain > (endGain + gain) ? gain : (endGain + gain);

		double a = -horizDist * horizDist / (4 * maxGain);
		double b = horizDist;
		double c = -endGain;

		double slope = -b / (2 * a) - Math.sqrt(b * b - 4 * a * c) / (2 * a);

		double vy = Math.sqrt(maxGain * gravity);

		double vh = vy / slope;

		int dx = to.getBlockX() - from.getBlockX();
		int dz = to.getBlockZ() - from.getBlockZ();
		double mag = Math.sqrt(dx * dx + dz * dz);
		double dirx = dx / mag;
		double dirz = dz / mag;

		double vx = vh * dirx;
		double vz = vh * dirz;

		return new Vector(vx, vy, vz);
	}

	/**
	 * Returns the distance from two {@link Vector} in square.
	 * 
	 * @param from
	 * @param to
	 * @return {@link Double}
	 */
	public static double distanceSquaredVector(Vector from, Vector to) {
		double dx = to.getBlockX() - from.getBlockX();
		double dz = to.getBlockZ() - from.getBlockZ();

		return dx * dx + dz * dz;
	}

	/**
	 * Check if the two {@link Location} are in the same world.
	 * 
	 * @param loc1
	 * @param loc2
	 * @return {@link Boolean}
	 */
	public static boolean sameWorld(Location loc1, Location loc2) {
		return loc1.getWorld().equals(loc2.getWorld());
	}

	/**
	 * Distance between two {@link Location};
	 * 
	 * @param loc1
	 * @param loc2
	 * @return {@link Double}
	 */
	public static double distanceLocation(Location loc1, Location loc2) {
		return loc1.distance(loc2);
	}

	/**
	 * Distance between two {@link Location} in square;
	 * 
	 * @param loc1
	 * @param loc2
	 * @return {@link Double}
	 */
	public static double distanceSquaredLocation(Location loc1, Location loc2) {
		return loc1.distanceSquared(loc2);
	}

	/**
	 * Checks if two locations are equal
	 *
	 * @param l1
	 *            the first {@link Location}
	 * @param l2
	 *            the seconds {@link Location}
	 * @return are the two locations equal
	 */
	public static boolean isSameLocation(Location l1, Location l2) {
		return ((l1.getBlockX() == l2.getBlockX()) && (l1.getBlockY() == l2.getBlockY()) && (l1.getBlockZ() == l2.getBlockZ()));
	}

	/**
	 * Change the yaw & pitch of a {@link Location} to make it look at another
	 * {@link Location}
	 *
	 * @param loc
	 *            the {@link Location} which yaw & pitch should be modified
	 * @param lookat
	 *            the {@link Location} to look at
	 * @return the new modified {@link Location}
	 */
	public static Location lookAt(Location loc, Location lookat) {
		loc = loc.clone();
		double dx = lookat.getX() - loc.getX();
		double dy = lookat.getY() - loc.getY();
		double dz = lookat.getZ() - loc.getZ();

		if (dx != 0) {
			if (dx < 0) {
				loc.setYaw((float) (1.5 * Math.PI));
			} else {
				loc.setYaw((float) (0.5 * Math.PI));
			}
			loc.setYaw(loc.getYaw() - (float) Math.atan(dz / dx));
		} else if (dz < 0) {
			loc.setYaw((float) Math.PI);
		}

		double dxz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dz, 2));

		loc.setPitch((float) -Math.atan(dy / dxz));

		loc.setYaw(-loc.getYaw() * 180f / (float) Math.PI);
		loc.setPitch(loc.getPitch() * 180f / (float) Math.PI);

		return loc;
	}

	/**
	 * Checks if a {@link Location} is inside a square of two other locations
	 *
	 * @param loc
	 *            the {@link Location} to check for
	 * @param l1
	 *            the first corner of the square
	 * @param l2
	 *            the seconds corner of the square
	 * @return
	 */
	public static boolean isInside(Location loc, Location l1, Location l2) {
		int x1 = Math.min(l1.getBlockX(), l2.getBlockX());
		int y1 = Math.min(l1.getBlockY(), l2.getBlockY());
		int z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
		int x2 = Math.max(l1.getBlockX(), l2.getBlockX());
		int y2 = Math.max(l1.getBlockY(), l2.getBlockY());
		int z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());

		return loc.getX() >= x1 && loc.getX() <= x2 && loc.getY() >= y1 && loc.getY() <= y2 && loc.getZ() >= z1 && loc.getZ() <= z2;
	}

	/**
	 * Gets the {@link Location} behind a {@link Player}
	 *
	 * @param player
	 *            the {@link Player} to get its {@link Location}
	 * @param range
	 *            the distance to the player
	 * @return
	 */
	public static Location getLocationBehindPlayer(Player p, int range) {
		World world = p.getWorld();
		Location behind = AsyncThreadWorkers.getPlayerLocation(p);
		int direction = (int) behind.getYaw();

		if (direction < 0) {
			direction += 360;
			direction = (direction + 45) / 90;
		} else {
			direction = (direction + 45) / 90;
		}

		switch (direction) {
			case 1 :
				behind = new Location(world, behind.getX() + range, behind.getY(), behind.getZ(), behind.getYaw(), behind.getPitch());
				break;
			case 2 :
				behind = new Location(world, behind.getX(), behind.getY(), behind.getZ() + range, behind.getYaw(), behind.getPitch());
				break;
			case 3 :
				behind = new Location(world, behind.getX() - range, behind.getY(), behind.getZ(), behind.getYaw(), behind.getPitch());
				break;
			case 4 :
				behind = new Location(world, behind.getX(), behind.getY(), behind.getZ() - range, behind.getYaw(), behind.getPitch());
				break;
			case 0 :
				behind = new Location(world, behind.getX(), behind.getY(), behind.getZ() - range, behind.getYaw(), behind.getPitch());
				break;
			default :
				break;
		}

		return behind;
	}

	/**
	 * Generates a random {@link Location} with offset from X,Y,Z.
	 * 
	 * @param player
	 * @param Xminimum
	 * @param Xmaximum
	 * @param Zminimum
	 * @param Zmaximum
	 * @return {@link Location}
	 */
	public static Location getRandomLocation(Location player, int Xminimum, int Xmaximum, int Zminimum, int Zmaximum) {
		try {
			World world = player.getWorld();
			double x = Double.parseDouble(Integer.toString(Xminimum + ((int) (Math.random() * ((Xmaximum - Xminimum) + 1))))) + 0.5d;
			double z = Double.parseDouble(Integer.toString(Zminimum + ((int) (Math.random() * ((Zmaximum - Zminimum) + 1))))) + 0.5d;
			player.setY(200);
			return new Location(world, x, player.getY(), z);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Converts degrees to {@link EulerAngle}.
	 * 
	 * @param degrees
	 * @return {@link EulerAngle}
	 */
	public static EulerAngle angleToEulerAngle(int degrees) {
		return angleToEulerAngle(Math.toRadians(degrees));
	}

	/**
	 * Converts angle to {@link EulerAngle}.
	 * 
	 * @param radians
	 * @return {@link EulerAngle}
	 */
	public static EulerAngle angleToEulerAngle(double radians) {
		double x = Math.cos(radians);
		double z = Math.sin(radians);
		return new EulerAngle(x, 0, z);
	}

	/**
	 * Generates a {@link List} of {@link Location} around the {@link Location}.
	 * 
	 * @param loc
	 * @param radius
	 * @param gap
	 * @return {@link List}
	 */
	public static List<Location> generateCicleAroundLocation(Location loc, double radius, double gap) {
		List<Location> locs = new ArrayList<>();
		for (double i = 0; i < 360; i += gap) {
			Location cloc = loc.clone();
			cloc.setZ(cloc.getZ() + Math.cos(i) * radius);
			cloc.setX(cloc.getX() + Math.sin(i) * radius);
			loc.add(cloc);
		}
		return locs;
	}

}
