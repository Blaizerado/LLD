package at.ltd.lobby.shop.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.entity.Player;

import at.ltd.adds.utils.LocUtils;
import at.ltd.adds.utils.config.ConfigManager;
import at.ltd.lobby.shop.LobbyShopItemSerializer;
import at.ltd.lobby.shop.reflections.LobbyShopMobs;

public class LobbyShopData {

	public static ConcurrentHashMap<LobbyShopUnit, CopyOnWriteArrayList<LobbyShopItem>> SHOP_ITEMS = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<Integer, LobbyShopUnit> SHOPS = new ConcurrentHashMap<>();

	public static ConcurrentHashMap<Integer, LobbyShopItem> ITEMS_BY_ID = new ConcurrentHashMap<>();

	public static ConcurrentHashMap<Integer, LobbyShopMobData> SHOPS_MOBS_BY_ENTITY_ID = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<Player, CopyOnWriteArrayList<LobbyShopMobData>> SHOPS_MOBS_BY_PLAYER = new ConcurrentHashMap<>();

	public static ArrayList<String> INV_NAMES = new ArrayList<>();

	public static CopyOnWriteArrayList<LobbyShopItem> getItems(LobbyShopUnit lsu) {
		return SHOP_ITEMS.get(lsu);
	}

	public static void readShops() {
		SHOP_ITEMS.clear();
		SHOPS.clear();
		ITEMS_BY_ID.clear();
		INV_NAMES.clear();
		File folder = new File("plugins/LTD/Shop/");
		for (File file : folder.listFiles()) {
			HashMap<String, String> hm = ConfigManager.readConfig(file.getAbsolutePath());
			LobbyShopUnit unit = new LobbyShopUnit(LocUtils.locationByString(hm.get("POS")), Integer.valueOf(hm.get("INVSIZE")), hm.get("SHOPNAME"), hm.get("SHOPMOBNAME"), LobbyShopMobs.valueOf(hm.get("CREATURE")));
			for (String s : hm.keySet()) {
				if (isNumeric(s)) {
					String value = hm.get(s);
					try {
						unit.addShopItem(LobbyShopItemSerializer.stringToLobbyItem(value, Integer.valueOf(s), unit));
					} catch (Exception e) {
						System.out.println("ERROR ON ITEM: " + s + " FILE: " + file.getAbsolutePath() + " VALUE: " + value);
						String[] dataset = value.split("##");
						System.out.println("ARRAY: " + Arrays.toString(dataset));
						e.printStackTrace();
					}

				}
			}
		}
	}

	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?");
	}
}
