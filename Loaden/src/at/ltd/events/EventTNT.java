package at.ltd.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.material.Sign;
import org.bukkit.material.Torch;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import at.ltd.Main;

public class EventTNT implements Listener {
	static ArrayList<List<BlockState>> exp = new ArrayList<>();

	@EventHandler
	public void onEntityExplode(EntityExplodeEvent e) {

		if (!e.blockList().isEmpty()) {
			Plugin plugin = Bukkit.getPluginManager().getPlugin("JoooooEzGG");
			final List<BlockState> blocks = new ArrayList<BlockState>();

			ArrayList<Location> l = new ArrayList<>();
			ArrayList<Block> blocklist = new ArrayList<>();
			List<Block> iter = e.blockList();
			for (Block b : iter) {
				if (b.getType() == Material.WALL_SIGN || b.getType() == Material.SIGN_POST) {
					Sign s = (Sign) b.getState().getData();
					l.add(b.getRelative(s.getAttachedFace()).getLocation());
					blocklist.add(b);
					blocklist.add(b.getRelative(s.getAttachedFace()));
				}
				if (b.getType() == Material.TORCH) {
					Torch s = (Torch) b.getState().getData();
					l.add(b.getRelative(s.getAttachedFace()).getLocation());
					blocklist.add(b);
					blocklist.add(b.getRelative(s.getAttachedFace()));
				}

				if (b.getType() == Material.ITEM_FRAME) {
					ItemFrame s = (ItemFrame) b.getState().getData();
					l.add(b.getRelative(s.getAttachedFace()).getLocation());
					blocklist.add(b);
					blocklist.add(b.getRelative(s.getAttachedFace()));
				}
				if (b.getType() == Material.RAILS) {
					l.add(b.getRelative(BlockFace.DOWN).getLocation());
					blocklist.add(b);
					blocklist.add(b.getRelative(BlockFace.DOWN));
				}

				if (b.getType() == Material.MINECART) {
					l.add(b.getRelative(BlockFace.DOWN).getLocation());
					blocklist.add(b);
					blocklist.add(b.getRelative(BlockFace.DOWN));
				}

				if (b.getType() == Material.GOLD_PLATE) {
					l.add(b.getRelative(BlockFace.DOWN).getLocation());
					blocklist.add(b);
					blocklist.add(b.getRelative(BlockFace.DOWN));
				}

			}

			for (Block b : blocklist) {
				e.blockList().remove(b);
			}

			for (Block b : e.blockList()) {
				if (b.getType() != Material.AIR) {
					if (!blocks.contains(b.getState())) {

						blocks.add(b.getState());
						// b.setType(Material.AIR);

					}
				}
			}
			exp.add(blocks);
			new BukkitRunnable() {
				int i = 17;

				public void run() {
					if (i > 0) {
						i--;
					} else {
						regen(blocks, true, 15);

						this.cancel();

					}
				}
			}.runTaskTimer(plugin, 3, 4);
		}
	}

	protected void regen(final List<BlockState> blocks, boolean effect, int speed) {
		Plugin plugin = Main.getPlugin();
		new BukkitRunnable() {
			int i = -1;

			@SuppressWarnings("deprecation")
			public void run() {

				if (i != blocks.size() - 1) {
					i++;
					BlockState bs = blocks.get(i);
					bs.getBlock().setType(bs.getType());
					bs.getBlock().setData(bs.getData().getData());

					if (bs.getBlock().getWorld().getName().equals("skypvp3") || bs.getBlock().getWorld().getName().equals("Farmwelt")) {
						return;
					}
					bs.getBlock().getWorld().playEffect(bs.getLocation(), Effect.STEP_SOUND, bs.getBlock().getType());
					bs.getBlock().getWorld().playEffect(bs.getLocation(), Effect.STEP_SOUND, bs.getBlock().getData());
				} else {
					this.cancel();
					regenFast(blocks, true);
					exp.remove(blocks);
				}

			}

		}.runTaskTimer(plugin, 0, 5);

	}

	protected void regenFast(final List<BlockState> blocks, boolean effect) {
		Plugin plugin = Bukkit.getPluginManager().getPlugin("JoooooEzGG");
		new BukkitRunnable() {
			int i = -1;

			@SuppressWarnings("deprecation")
			public void run() {

				if (i != blocks.size() - 1) {
					i++;
					BlockState bs = blocks.get(i);
					bs.getBlock().setType(bs.getType());
					bs.getBlock().setData(bs.getData().getData());

				} else {
					this.cancel();
					exp.remove(blocks);
				}

			}

		}.runTaskTimer(plugin, 0, 0);

	}

	public static Block getBlockSignAttachedTo(Block block) {
		if (block.getType().equals(Material.WALL_SIGN))
			switch (block.getData()) {
				case 2 :
					return block.getRelative(BlockFace.WEST);
				case 3 :
					return block.getRelative(BlockFace.EAST);
				case 4 :
					return block.getRelative(BlockFace.SOUTH);
				case 5 :
					return block.getRelative(BlockFace.NORTH);
			}
		return null;
	}

	public static void shutdown() {
		for (List<BlockState> bb : exp) {
			for (BlockState bs : bb) {
				bs.getBlock().setType(bs.getType());
				bs.getBlock().setData(bs.getData().getData());

			}
		}
	}

	@EventHandler
	public void onBlockPhysicsEvent(BlockPhysicsEvent event) {
		if (event.isCancelled()) {
			return;
		}
		Block block = event.getBlock();
		if (block.getWorld().getName().equals("skypvp3")) {
			if (isLava(block) || isWater(block)) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBlockFromTo(BlockFromToEvent event) {
		if (event.isCancelled()) {
			return;
		}
		Block block = event.getBlock();
		if (block.getWorld().getName().equals("skypvp3")) {
			if (isLava(block) || isWater(block)) {

				event.setCancelled(true);
			}
		}
	}

	private boolean isLava(Block block) {
		return (block.getType() == Material.LAVA) || (block.getType() == Material.STATIONARY_LAVA);
	}

	private boolean isWater(Block block) {
		return (block.getType() == Material.WATER) || (block.getType() == Material.STATIONARY_WATER);
	}

}
