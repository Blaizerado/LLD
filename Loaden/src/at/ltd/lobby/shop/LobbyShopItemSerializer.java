package at.ltd.lobby.shop;

import org.bukkit.inventory.ItemStack;

import at.ltd.adds.utils.serializer.ItemSerializer;
import at.ltd.gungame.guns.utils.GunInterface;
import at.ltd.gungame.guns.utils.GunUtils;
import at.ltd.lobby.shop.data.LobbyShopItem;
import at.ltd.lobby.shop.data.LobbyShopItem.ShopItemType;
import at.ltd.lobby.shop.data.LobbyShopUnit;

public class LobbyShopItemSerializer {

	public static String FORMAT = "[TYPE]##[COST]##[ITEM]##[KILLS]";

	public static String toString(ItemStack is, int cost) {
		String serItem = ItemSerializer.serialize(is);
		String form = new String(FORMAT);
		form = form.replace("[TYPE]", "ITEM").replace("[COST]", cost + "").replace("[ITEM]", serItem).replace("[KILLS]", "0");
		return form;
	}

	public static String toString(GunInterface is, int cost, int kills) {
		String form = new String(FORMAT);
		form = form.replace("[TYPE]", "GUN").replace("[COST]", cost + "").replace("[ITEM]", is.getGunID() + "").replace("[KILLS]", kills + "");
		return form;
	}

	public static String toString(LobbyShopItem.ShopItemType type) {
		String form = new String(FORMAT);
		if (type == ShopItemType.CLOSE) {
			form = form.replace("[TYPE]", "CLOSE").replace("[COST]", "0").replace("[ITEM]", "0").replace("[KILLS]", "0");
		} else if (type == ShopItemType.MONEY) {
			form = form.replace("[TYPE]", "MONEY").replace("[COST]", "0").replace("[ITEM]", "0").replace("[KILLS]", "0");
		} else if (type == ShopItemType.NOTHING) {
			form = form.replace("[TYPE]", "NOTHING").replace("[COST]", "0").replace("[ITEM]", "0").replace("[KILLS]", "0");
		}
		return form;
	}

	public static LobbyShopItem stringToLobbyItem(String data, int slot, LobbyShopUnit lsu) {
		String[] dataset = data.split("##");
		LobbyShopItem lsi = null;
		ShopItemType type = LobbyShopItem.ShopItemType.valueOf(dataset[0]);
		if (type == ShopItemType.GUN) {
			lsi = new LobbyShopItem(ShopItemType.GUN, slot, lsu).setAsGun(GunUtils.getGunInterfaceByID(Integer.valueOf(dataset[2])), Integer.valueOf(dataset[3]), Integer.valueOf(dataset[1]));
		} else if (type == ShopItemType.ITEM) {
			lsi = new LobbyShopItem(type, slot, lsu).setAsItem(ItemSerializer.deserialize(dataset[2]), Integer.valueOf(dataset[1]));
		} else if (type == ShopItemType.CLOSE) {
			lsi = new LobbyShopItem(type, slot, lsu);
			if (hasItem(dataset)) {
				lsi.setItemStack(ItemSerializer.deserialize(dataset[2]));
			}
		} else if (type == ShopItemType.MONEY) {
			lsi = new LobbyShopItem(type, slot, lsu);
			if (hasItem(dataset)) {
				lsi.setItemStack(ItemSerializer.deserialize(dataset[2]));
			}
		} else if (type == ShopItemType.NOTHING) {
			lsi = new LobbyShopItem(type, slot, lsu);
			if (hasItem(dataset)) {
				lsi.setItemStack(ItemSerializer.deserialize(dataset[2]));
			}
		}

		return lsi;
	}

	public static boolean hasItem(String[] dataset) {
		String item = dataset[2];
		if(!isNumeric(item)) {
			return true;
		}
		return false;
	}
	
	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?");
	}

}
