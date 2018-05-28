package at.ltd.gungame.utils.airdrop;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.inventory.ItemStack;

import at.ltd.gungame.guns.utils.GunInterface;
import at.ltd.gungame.guns.utils.GunUtils;

public class AirdropKit {

	public ArrayList<ItemStack> items = new ArrayList<>();
	public static ArrayList<AirdropKit> kits = new ArrayList<>();

	public AirdropKit() {
		kits.add(this);
	}

	public void addGun(GunInterface gi) {
		items.add(GunUtils.createGun(gi, null, 2).getItemStack());
	}

	public void addItem(ItemStack is) {
		items.add(is);
	}

	public static AirdropKit getRandomKit() {
		try {
			return kits.get((new Random()).nextInt(kits.size()));
		} catch (Throwable e) {
			return null;
		}
	}

}
