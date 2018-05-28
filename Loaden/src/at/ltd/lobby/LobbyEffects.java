package at.ltd.lobby;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import at.ltd.Main;
import at.ltd.adds.utils.LocUtils;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.adds.utils.visual.Particles;
import at.ltd.gungame.guns.events.custom.EventProjectileHitAsync;
import at.ltd.gungame.utils.hellfire.HellfireFirePlan;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldEvent;

public class LobbyEffects implements Listener {
	public static ArrayList<String> HELLFIRE = new ArrayList<>();
	public static ArrayList<Location> ROCKETS = new ArrayList<>();
	static {
		HELLFIRE.add("-2086.0*110.0*1370.0*0.0*0.0*GG_Lobby>>-2028.0*28.0*1387.0*0.0*0.0*GG_Lobby>>30");
		HELLFIRE.add("-2082.0*120.0*1374.0*0.0*0.0*GG_Lobby>>-2039.0*28.0*1373.0*0.0*0.0*GG_Lobby>>30");
		HELLFIRE.add("-2080.0*109.0*1394.0*0.0*0.0*GG_Lobby>>-2031.0*27.0*1358.0*0.0*0.0*GG_Lobby>>30");
		HELLFIRE.add("-2080.0*101.0*1388.0*0.0*0.0*GG_Lobby>>-2015.0*30.0*1352.0*0.0*0.0*GG_Lobby>>30");
		HELLFIRE.add("-2074.0*108.0*1387.0*0.0*0.0*GG_Lobby>>-2022.0*27.0*1419.0*0.0*0.0*GG_Lobby>>20");
		HELLFIRE.add("-2064.0*83.0*1376.0*0.0*0.0*GG_Lobby>>-1978.0*29.0*1371.0*0.0*0.0*GG_Lobby>>20");
		HELLFIRE.add("-2058.0*83.0*1384.0*0.0*0.0*GG_Lobby>>-1964.0*48.0*1388.0*0.0*0.0*GG_Lobby>>20");
		HELLFIRE.add("-2062.0*84.0*1375.0*0.0*0.0*GG_Lobby>>-1972.0*50.0*1344.0*0.0*0.0*GG_Lobby>>20");
		HELLFIRE.add("-2062.0*87.0*1381.0*0.0*0.0*GG_Lobby>>-1972.0*50.0*1354.0*0.0*0.0*GG_Lobby>>20");
		HELLFIRE.add("-2045.0*77.0*1369.0*0.0*0.0*GG_Lobby>>-1959.0*29.0*1298.0*0.0*0.0*GG_Lobby>>20");
		HELLFIRE.add("-2052.0*78.0*1363.0*0.0*0.0*GG_Lobby>>-1940.0*40.0*1388.0*0.0*0.0*GG_Lobby>>20");
		HELLFIRE.add("-2063.0*88.0*1391.0*0.0*0.0*GG_Lobby>>-1979.0*29.0*1457.0*0.0*0.0*GG_Lobby>>20");
		HELLFIRE.add("-2058.0*81.0*1385.0*0.0*0.0*GG_Lobby>>-1965.0*48.0*1454.0*0.0*0.0*GG_Lobby>>30");
		HELLFIRE.add("-2060.0*87.0*1390.0*0.0*0.0*GG_Lobby>>-1972.0*29.0*1452.0*0.0*0.0*GG_Lobby>>30");
		HELLFIRE.add("-2066.0*86.0*1368.0*0.0*0.0*GG_Lobby>>-1981.0*29.0*1433.0*0.0*0.0*GG_Lobby>>30");
		HELLFIRE.add("-2055.0*88.0*1379.0*0.0*0.0*GG_Lobby>>-2005.0*24.0*1386.0*0.0*0.0*GG_Lobby>>30");

		HELLFIRE.add("-2058.0*85.0*1385.0*0.0*0.0*GG_Lobby>>-1990.0*24.0*1430.0*0.0*0.0*GG_Lobby>>30");
		HELLFIRE.add("-2058.0*89.0*1380.0*0.0*0.0*GG_Lobby>>-1987.0*24.0*1412.0*0.0*0.0*GG_Lobby>>30");
		HELLFIRE.add("-2065.0*85.0*1373.0*0.0*0.0*GG_Lobby>>-1987.0*24.0*1397.0*0.0*0.0*GG_Lobby>>30");
		HELLFIRE.add("-2066.0*89.0*1375.0*0.0*0.0*GG_Lobby>>-1987.0*24.0*1373.0*0.0*0.0*GG_Lobby>>30");
		HELLFIRE.add("-2064.0*87.0*1372.0*0.0*0.0*GG_Lobby>>-1985.0*24.0*1357.0*0.0*0.0*GG_Lobby>>30");
		HELLFIRE.add("-2061.0*85.0*1377.0*0.0*0.0*GG_Lobby>>-1986.0*26.0*1344.0*0.0*0.0*GG_Lobby>>30");
		HELLFIRE.add("-2053.0*82.0*1373.0*0.0*0.0*GG_Lobby>>-1987.0*26.0*1320.0*0.0*0.0*GG_Lobby>>30");
		HELLFIRE.add("-2050.0*82.0*1363.0*0.0*0.0*GG_Lobby>>-1993.0*24.0*1304.0*0.0*0.0*GG_Lobby>>30");
		HELLFIRE.add("-2043.0*73.0*1352.0*0.0*0.0*GG_Lobby>>-1989.0*24.0*1281.0*0.0*0.0*GG_Lobby>>30");
	}

	public static void init() {
		ROCKETS.add(LocUtils.locationByString("-1978.7730610808726*38.23152385719166*1360.794786450393*-272.82053*-13.949967*GG_Lobby"));
		ROCKETS.add(LocUtils.locationByString("-1978.4702222233902*38.23152385719166*1390.437076853624*-254.97156*-12.449993*GG_Lobby"));
		Main.registerListener(new LobbyEffects());
		AsyncThreadWorkers.submitSchedulerWorkTick(() -> {
			for (String s : (CopyOnWriteArrayList<String>) ROCKET_UUIDS.clone()) {
				Location loc = AsyncThreadWorkers.getEntityLocation(s);
				if (loc != null) {
					Particles.ROCKET_TRACE(loc);
				}
			}
		}, 20, 1);
	}

	public static void playEffect() {
		launchHellfire();
		launchRocket();
	}

	public static void launchHellfire() {
		AsyncThreadWorkers.submitWork(() -> {
			for (String s : (ArrayList<String>) HELLFIRE.clone()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				AsyncThreadWorkers.submitSyncWork(() -> HellfireFirePlan.fireRocket(s).setFireOnHit(false).setDamage(15.00D));
			}
		});

	}
	public static CopyOnWriteArrayList<String> ROCKET_UUIDS = new CopyOnWriteArrayList<>();
	public static void launchRocket() {
		for (Location loc : ROCKETS) {
			Snowball s = loc.getWorld().spawn(loc, Snowball.class);
			s.setGravity(false);
			s.setVelocity(loc.getDirection().multiply(2D));
			ROCKET_UUIDS.add(s.getUniqueId().toString());
		}
	}

	@EventHandler
	public void on(EventProjectileHitAsync e) {
		if (ROCKET_UUIDS.contains(e.getUUID())) {
			ROCKET_UUIDS.remove(e.getUUID());
			makeEffect(e.getLocation());
		}
	}

	public static void makeEffect(Location loc) {

		List<Block> blocks = getNearbyBlocks(loc, 10);
		for (Block b : blocks) {
			if (b.getType() == Material.BARRIER) {
				continue;
			}
			for (Player p : getPlayersNearLocation(loc, 30)) {
				PacketPlayOutWorldEvent packet = new PacketPlayOutWorldEvent(2001, new BlockPosition(b.getX(), b.getY(), b.getZ()), b.getType().getId(), false);
				((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
			}
		}
		Particles.HELLFIRE_Explosion(loc);
		loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 10, 10);

	}

	public static List<Player> getPlayersNearLocation(Location loc, int distance) {
		List<Player> res = new ArrayList<Player>();
		int d2 = distance * distance;
		for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
			if (p.getWorld() == loc.getWorld() && p.getLocation().distanceSquared(loc) <= d2) {
				res.add(p);
			}
		}
		return res;
	}

	public static List<Block> getNearbyBlocks(Location location, int radius) {
		radius = Math.round(radius / 3F);
		List<Block> blocks = new ArrayList<Block>();
		for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
			for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
				for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
					blocks.add(location.getWorld().getBlockAt(x, y, z));
				}
			}
		}
		return blocks;
	}

}
