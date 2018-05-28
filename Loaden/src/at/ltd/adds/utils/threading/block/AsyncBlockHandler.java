package at.ltd.adds.utils.threading.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

import at.ltd.Main;

public class AsyncBlockHandler implements Listener {

	protected static HashMap<World, HashMap<String, AsyncBlock>> BLOCKS = new HashMap<>();

	public static void init() {
		System.out.println("[LTD] Loading AsyncWorlds!");
		Bukkit.getWorlds().forEach(w -> {
			loadWorld(w);
			System.out.println("[LTD] World " + w.getName() + " loaded.");
		});
		System.out.println("[LTD] AsyncWorld done!");
	}

	@EventHandler
	public void on(WorldLoadEvent e) {
		synchronized (BLOCKS) {
			if (BLOCKS.containsKey(e.getWorld())) {
				return;
			}
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () -> {
			loadWorld(e.getWorld());
		}, 20);

	}

	public static boolean containsBlockAt(Location loc) {
		synchronized (BLOCKS) {
			return BLOCKS.get(loc.getWorld()).containsKey(genString(loc));
		}
	}

	protected static boolean hasBlockState(Material mat) {
		if (mat == Material.SIGN) {
			return true;
		}
		if (mat == Material.WOOL) {
			return true;
		}
		if (mat == Material.SKULL) {
			return true;
		}
		return false;
	}

	protected static String genString(Location loc) {
		return loc.getBlockX() + "." + loc.getBlockY() + "." + loc.getBlockZ();
	}

	protected static List<Block> getNearbyBlocks(Location location, int radius) {
		List<Block> blocks = new ArrayList<Block>();
		for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
			for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
				for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
					Block block = location.getWorld().getBlockAt(x, y, z);
					if (block.getType() != Material.AIR) {
						blocks.add(location.getWorld().getBlockAt(x, y, z));
					}
				}
			}
		}
		return blocks;
	}

	protected static void loadWorld(World w) {
		HashMap<String, AsyncBlock> hm = new HashMap<>();
		List<Block> blocks = getNearbyBlocks(w.getSpawnLocation(), 100);
		System.out.println("Got blocks ");
		for (Block block : blocks) {
			Material mat = block.getType();
			String loc = genString(block.getLocation());
			if (hasBlockState(mat)) {
				hm.put(loc, new AsyncBlock(block.getLocation(), mat, block.getState()));
			} else {
				hm.put(loc, new AsyncBlock(block.getLocation(), mat));
			}
		}
		synchronized (BLOCKS) {
			BLOCKS.put(w, hm);
		}
	}

}
