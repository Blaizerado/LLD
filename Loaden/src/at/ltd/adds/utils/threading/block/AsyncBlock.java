package at.ltd.adds.utils.threading.block;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;

public class AsyncBlock {

	private final int x, y, z;
	private final World world;
	protected volatile Material mat;
	protected volatile BlockState blockState;

	public AsyncBlock(Location loc, Material mat) {
		this.x = loc.getBlockX();
		this.y = loc.getBlockY();
		this.z = loc.getBlockZ();
		this.world = loc.getWorld();
		this.mat = mat;
	}
	public AsyncBlock(Location loc, Material mat, BlockState blockState) {
		this.x = loc.getBlockX();
		this.y = loc.getBlockY();
		this.z = loc.getBlockZ();
		this.world = loc.getWorld();
		this.mat = mat;
		this.blockState = blockState;
	}

	private boolean UPDATE;
	public AsyncBlock requestUpdate() {
		synchronized (this) {
			UPDATE = false;
			Location loc = new Location(world, x, y, z);
			AsyncThreadWorkers.submitSyncWork(() -> {
				Block block = loc.getBlock();
				if (AsyncBlockHandler.hasBlockState(block.getType())) {
					blockState = block.getState();
					mat = block.getType();
				} else {
					mat = block.getType();
				}
				UPDATE = true;
			});
			while (!UPDATE) {
				try {
					Thread.sleep(0, 90);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return this;

	}

	public Material getMaterial() {
		synchronized (this) {
			return mat;
		}
	}

	public Location getLocation() {
		return new Location(world, x, y, z);
	}

	public World getWorld() {
		synchronized (this) {
			return world;
		}
	}

	public BlockState getBlockState() {
		synchronized (this) {
			return blockState;
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public static AsyncBlock getBlockAt(Location loc) {
		synchronized (AsyncBlockHandler.BLOCKS) {
			String s = AsyncBlockHandler.genString(loc);
			return AsyncBlockHandler.BLOCKS.get(loc.getWorld()).get(s);
		}
	}

}
