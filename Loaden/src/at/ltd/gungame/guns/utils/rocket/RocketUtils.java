package at.ltd.gungame.guns.utils.rocket;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class RocketUtils {

	public static final Material ROCKET_MATERIAL = Material.COAL;
	public static final String ROCKET_NAME = "§cRackete";

	public static boolean validateItemStack(ItemStack is) {
		if (is.getType() == ROCKET_MATERIAL) {
			if (is.hasItemMeta()) {
				if (is.getItemMeta().hasDisplayName()) {
					if (is.getItemMeta().getDisplayName().equals(ROCKET_NAME)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static int getRocketCount(PlayerInventory pi) {
		ItemStack[] inv = pi.getContents();
		int cuantity = 0;
		for (int i = 0; i < inv.length; i++) {
			if (inv[i] != null) {
				if (inv[i].getType() == ROCKET_MATERIAL) {
					if (validateItemStack(inv[i])) {
						int cant = inv[i].getAmount();
						cuantity = cuantity + cant;
					}
				}
			}
		}
		return cuantity;
	}

	public static void removeRockets(PlayerInventory pi, int ammount) {
		ItemStack[] inv = pi.getContents();
		ItemStack[] remove = new ItemStack[inv.length];

		int removepos = 0;

		for (int i = 0; i < inv.length; i++) {

			ItemStack is = inv[i];

			if (ammount == 0 | ammount < 0) {
				break;
			}

			if (is != null) {
				if (is.getType() == ROCKET_MATERIAL) {
					if (validateItemStack(is)) {
						int size = is.getAmount();
						ammount = ammount - size;

						if (ammount < 0) {
							int dif = (ammount < 0 ? -ammount : ammount);
							ItemStack newis = is.clone();
							newis.setAmount(size - dif);
							remove[removepos] = newis;
						} else {
							remove[removepos] = is;
						}
						removepos++;
					}
				}
			}
		}
		for (int i = 0; i < remove.length; i++) {
			ItemStack is = remove[i];
			if (is == null) {
				break;
			}
			pi.removeItem(is);
		}
	}

	public static ItemStack createRocket() {
		ItemStack is = new ItemStack(ROCKET_MATERIAL);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ROCKET_NAME);
		is.setItemMeta(im);
		return is;
	}

	public static ItemStack createRocket(int size) {
		ItemStack is = new ItemStack(ROCKET_MATERIAL);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ROCKET_NAME);
		is.setItemMeta(im);
		is.setAmount(size);
		return is;
	}

	public static void init() {
		RocketLauncherManager.init();
	}

}
