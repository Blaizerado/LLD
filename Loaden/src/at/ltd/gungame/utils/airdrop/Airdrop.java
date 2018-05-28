package at.ltd.gungame.utils.airdrop;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import at.ltd.Main;

public class Airdrop {

	private Location startLoc;
	private ArrayList<ItemStack> airdropItems = new ArrayList<>();
	private int yoffset = 0;
	private int scheID;

	public Airdrop(Location loc, ArrayList<ItemStack> items) {
		this.startLoc = loc;
		if (items == null) {
			AirdropKit kit = AirdropKit.getRandomKit();
			for (ItemStack is : kit.items) {
				if (is != null) {
					airdropItems.add(is.clone());
				}
			}
		} else {
			this.airdropItems = items;
		}
	}

	public void start() {
		scheID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {

			@Override
			public void run() {
				if (check()) {
					setNewPos();
				} else {
					finish();
					Bukkit.getScheduler().cancelTask(scheID);
				}
			}
		}, 0, 8);
	}

	public boolean check() {
		Location loc = startLoc.clone();
		loc = loc.add(0, 0 - (yoffset + 1), 0);
		return loc.getWorld().getBlockAt(loc).getType() == Material.AIR;

	}

	public void finish() {
		Location loc = startLoc.clone();
		loc = loc.add(0, 0 - yoffset, 0);
		Block block = loc.getWorld().getBlockAt(loc);
		if (block.getType() == Material.CHEST) {
			Chest chest = (Chest) block.getState();
			int size = chest.getBlockInventory().getSize();
			boolean[] chosen = new boolean[size];
			final Random random = new Random();
			for (int i = 0; i < airdropItems.size(); i++) {

				int slot;

				do {
					slot = random.nextInt(size);
				} while (chosen[slot]);

				chosen[slot] = true;
				chest.getBlockInventory().setItem(slot, airdropItems.get(i));
			}

		}

		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {

			@Override
			public void run() {
				Location loc = startLoc.clone();
				loc = loc.add(0, 0 - yoffset, 0);
				Block block = loc.getWorld().getBlockAt(loc);
				if (block.getType() == Material.CHEST) {
					Chest chest = (Chest) block.getState();
					chest.getBlockInventory().clear();
				}

				for (AirdropBlock ab : AirdropManager.blocks) {
					AirdropManager.removeBlock(ab, startLoc, yoffset);
				}
			}
		}, 20 * 60);
	}

	public void setNewPos() {
		for (AirdropBlock ab : AirdropManager.blocks) {
			AirdropManager.removeBlock(ab, startLoc, yoffset);
		}
		yoffset++;
		for (AirdropBlock ab : AirdropManager.blocks) {
			AirdropManager.spawnBlock(ab, startLoc, yoffset);
		}
	}

}
