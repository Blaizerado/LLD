package at.ltd.adds.utils;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import at.ltd.adds.utils.serializer.ItemSerializer;

public class ItemUtils {

	public static ItemStack generateItemStack(Material mat, String name, String... lore) {
		ItemStack is = new ItemStack(mat);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		im.setLore(Arrays.asList(lore));
		is.setItemMeta(im);
		return is;
	}
	public static ItemStack generateItemStack(ItemStack is, String name, String... lore) {
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		im.setLore(Arrays.asList(lore));
		is.setItemMeta(im);
		return is;
	}

	public static ItemStack generateItemStack(Material mat, String name, List<String> lore) {
		ItemStack is = new ItemStack(mat);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}

	public static ItemStack generateItemStack(Material mat, int amount, String name, String... lore) {
		ItemStack is = generateItemStack(mat, name, lore);
		is.setAmount(amount);
		return is;
	}

	public static String getItemName(ItemStack is) {
		if (is != null) {
			if (is.hasItemMeta()) {
				ItemMeta im = is.getItemMeta();
				if (im.hasDisplayName()) {
					return im.getDisplayName();
				}
			}
		}

		return "";
	}

	public static boolean hasItemName(ItemStack is) {
		if (is != null) {
			if (is.hasItemMeta()) {
				ItemMeta im = is.getItemMeta();
				if (im.hasDisplayName()) {
					return true;
				}
			}
		}
		return false;
	}

	public static ItemStack removeAttributes(ItemStack is) {
		ItemMeta im = is.getItemMeta();
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		is.setItemMeta(im);
		return is;
	}

	public static ItemStack getPlayerHead(Player player) {
		ItemStack skull = ItemSerializer.deserialize("[V=002:SKULL_ITEM/³1/³3/³/³/³/³/³/³/³/³/³/³/³/³/³/³]");
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setOwner(player.getName());
		skull.setItemMeta(meta);
		return skull;
	}

}
