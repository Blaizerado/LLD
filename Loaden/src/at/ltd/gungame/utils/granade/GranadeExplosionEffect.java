package at.ltd.gungame.utils.granade;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.adds.utils.visual.Particles;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldEvent;

public class GranadeExplosionEffect {

	public static void makeEffect(Location loc) {

		List<Block> blocks = getNearbyBlocks(loc, 5);
		for (Block b : blocks) {
			if (b.getType() == Material.BARRIER) {
				continue;
			}
			for (Player p : getPlayersNearLocation(loc, 30)) {
				PacketPlayOutWorldEvent packet = new PacketPlayOutWorldEvent(2001, new BlockPosition(b.getX(), b.getY(), b.getZ()), b.getType().getId(), false);
				((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
			}
		}
		Particles.GRANADE_Explosion(loc);
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
