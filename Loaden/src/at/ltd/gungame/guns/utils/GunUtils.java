package at.ltd.gungame.guns.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.ltd.adds.Lg;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.gungame.guns.events.custom.EventGunCreate;

public class GunUtils {

	public static boolean isGun(ItemStack is) {
		if (is.hasItemMeta()) {
			ItemMeta im = is.getItemMeta();
			if (im.hasLore()) {
				List<String> lore = im.getLore();
				if (lore.size() > 2) {
					if (lore.get(1).startsWith("§7")) {
						return true;
					}
				}

			}
		}

		return false;
	}

	public static String getSerialID(ItemStack is) {
		ItemMeta im = is.getItemMeta();
		List<String> lore = im.getLore();
		return lore.get(1).replace("§7", "");
	}

	public static Gun getGunMemory(ItemStack is) {
		return GunSerialNumber.getGun(getSerialID(is));
	}

	public static Gun getGunMemoryManaged(ItemStack is) {
		if (is == null) {
			return null;
		}

		if (!isGun(is)) {
			return null;
		}

		return getGunMemory(is);
	}

	public static GunInterface getGunInterfaceByItem(ItemStack is) {
		return GunSerialNumber.getGunInterface(getSerialID(is));
	}
	public static GunInterface getGunInterfaceByItemManaged(ItemStack is) {
		if (is == null) {
			return null;
		}

		if (!isGun(is)) {
			return null;
		}

		return getGunInterfaceByItem(is);
	}

	public static boolean isRegistrated(ItemStack is) {
		return GunSerialNumber.getGun(getSerialID(is)) != null;
	}

	public static boolean isGunGameItem(ItemStack is) {
		return GunSerialNumber.isGunGameItem(getSerialID(is));
	}

	public static GunInterface searchGunByItem(ItemStack is) {
		String item = GunRegister.createWeaponString(is);
		GunInterface gun = GunRegister.GUN_LIST_STRING.get(item);
		return gun;
	}

	public static Gun createGun(GunInterface gi, Player p, int kills) {
		ItemStack ITEM_STACK = gi.getItem();

		ItemMeta ITEM_META = ITEM_STACK.getItemMeta();
		ITEM_META.setDisplayName(gi.getName());

		Gun FINAL_GUN = new Gun(gi, ITEM_STACK, kills, gi.getMagazineSize(), p);
		ITEM_STACK.setItemMeta(ITEM_META);

		ITEM_STACK = setLore(ITEM_STACK, FINAL_GUN.getUUID());
		FINAL_GUN.setItemStack(ITEM_STACK);

		EventGunCreate eventGunCreate = new EventGunCreate(FINAL_GUN);
		Bukkit.getPluginManager().callEvent(eventGunCreate);

		FINAL_GUN.registerGun();
		return eventGunCreate.getGun();
	}

	public static Gun loadGunIntoMemory(ItemStack is, Player p) {
		GunInterface gi = searchGunByItem(is);
		if (gi == null) {
			Lg.lgError("CAN'T LOAD GUN INTO MEMORY.");
			throw new NullPointerException("ITEM IS NO GUN " + is.getType().name());
		}

		String oldSerialNumber = getSerialID(is);
		Gun FINAL_GUN = new Gun(gi, is, GunSerialNumber.getKillsLeft(oldSerialNumber), GunSerialNumber.getBullets(oldSerialNumber), p);

		ItemStack finalitem = FINAL_GUN.getItemStack();
		ItemMeta finalitemmeta = finalitem.getItemMeta();

		finalitemmeta.setDisplayName(gi.getName());

		finalitem.setItemMeta(finalitemmeta);
		setLore(finalitem, FINAL_GUN.getUUID());
		FINAL_GUN.setItemStack(finalitem);

		EventGunCreate eventGunCreate = new EventGunCreate(FINAL_GUN);
		Bukkit.getPluginManager().callEvent(eventGunCreate);

		FINAL_GUN.registerGun();
		return FINAL_GUN;

	}
	public static synchronized ItemStack setLore(ItemStack is, String serialID) {
		ArrayList<String> LORE = new ArrayList<>();
		LORE.add("§7===== §cWaffen-Daten §7=====");
		LORE.add("§7" + serialID);
		LORE.add("§7=======================");
		ItemMeta itemmeta = is.getItemMeta();
		itemmeta.setLore(LORE);
		is.setItemMeta(itemmeta);

		return is;
	}

	public static GunInterface getGunInterfaceByID(int id) {
		return GunRegister.GUN_LIST.get(id);
	}

	public static void removeGunFromInv(Gun gun) {
		Player p = gun.getPlayer();
		AsyncThreadWorkers.submitSyncWork(() -> p.getInventory().removeItem(gun.getItemStack()));

	}

	public static synchronized String createWeaponString(ItemStack is) {
		return is.getType().name() + "-" + is.getDurability();
	}

}
