package at.ltd.gungame.level;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GLevelItem {
	private static final String MARK = "§c§a";

	private ItemStack itemStack;
	private int slot;

	public GLevelItem(ItemStack is, int slot) {
		itemStack = is;
		this.slot = slot;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	public Integer getSlot() {
		return slot;
	}

	public void setSlot(Integer slot) {
		this.slot = slot;
	}

	public static ItemStack markItem(ItemStack is) {
		ItemMeta im = is.getItemMeta();
		List<String> lore;
		if (im.hasLore()) {
			lore = im.getLore();
		} else {
			lore = new ArrayList<>();
		}
		int size = lore.size() - 1;
		if (size == -1) {
			lore.add(MARK);
		} else {
			if (!lore.get(lore.size() - 1).equals(MARK)) {
				lore.add(MARK);
			}
		}
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}

	public static boolean isMarked(ItemStack is) {
		if (is != null && is.hasItemMeta()) {
			ItemMeta im = is.getItemMeta();
			if (im.hasLore()) {
				if (im.getLore().get(im.getLore().size() - 1).equals(MARK)) {
					return true;
				}
			}
		}
		return false;
	}

	public static ItemStack removeMark(ItemStack is) {
		ItemMeta im = is.getItemMeta();
		List<String> lore = im.getLore();
		lore.remove(lore.size() - 1);
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}

}
