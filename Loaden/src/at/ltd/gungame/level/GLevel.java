package at.ltd.gungame.level;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.ltd.adds.utils.config.ConfigManager;
import at.ltd.adds.utils.serializer.ItemSerializer;
import at.ltd.gungame.guns.utils.GunUtils;
import at.ltd.gungame.guns.utils.bullet.BulletUtils;
import at.ltd.gungame.guns.utils.rocket.RocketUtils;

public class GLevel {
	private File ConfigFile;
	private Integer id;
	private ArrayList<GLevelItem> items = new ArrayList<>();
	private Integer coins;
	private String broadcast;

	public GLevel(File ConfigFile) {
		id = Integer.valueOf(ConfigFile.getName().replace(".txt", ""));
		this.ConfigFile = ConfigFile;
	}

	public void Read() {
		HashMap<String, String> hm = ConfigManager.readConfig(ConfigFile.getPath());
		if (hm.containsKey("COINS")) {
			coins = Integer.valueOf(hm.get("COINS"));
		} else {
			coins = 10;
		}
		if (hm.containsKey("BROADCASTMESSAGE")) {
			broadcast = hm.get("BROADCASTMESSAGE");
		}
		for (String itemvalue : hm.keySet()) {

			if (itemvalue.startsWith("GUN")) {
				int slot = Integer.valueOf(itemvalue.replace("GUN", ""));
				int id = Integer.valueOf(hm.get(itemvalue));
				GLevelGun gun = new GLevelGun(GunUtils.getGunInterfaceByID(id), slot);
				items.add(gun);
			}

			if (itemvalue.startsWith("AMMO")) {
				int slot = Integer.valueOf(itemvalue.replace("AMMO", ""));
				int ammount = Integer.valueOf(hm.get(itemvalue));
				ItemStack bul = BulletUtils.createBullet(ammount);
				bul = GLevelItem.markItem(bul);
				GLevelItem gli = new GLevelItem(bul, slot);
				items.add(gli);
			}

			if (itemvalue.startsWith("ROCKET")) {
				int slot = Integer.valueOf(itemvalue.replace("ROCKET", ""));
				int ammount = Integer.valueOf(hm.get(itemvalue));
				ItemStack rock = RocketUtils.createRocket(ammount);
				rock = GLevelItem.markItem(rock);
				GLevelItem gli = new GLevelItem(rock, slot);
				items.add(gli);
			}

			if (itemvalue.startsWith("ITEM_")) {
				ItemStack is = ItemSerializer.deserialize(hm.get(itemvalue).replace("/²&a&oLevelitem", ""));
				int slot = Integer.valueOf(itemvalue.replace("ITEM_", ""));
				if (!GLevelItem.isMarked(is)) {
					is = GLevelItem.markItem(is);
				}

				GLevelItem gli = new GLevelItem(is, slot);
				items.add(gli);
			}
		}

	}

	public static String isToString(ItemStack is) {
		String item = ItemSerializer.serialize(is);
		return item;
	}

	public static <E> Collection<E> makeCollection(Iterable<E> iter) {
		Collection<E> list = new ArrayList<E>();
		for (E item : iter) {
			list.add(item);
		}
		return list;
	}

	public String getBroadcast() {
		return broadcast;
	}

	public void setBroadcast(String broadcast) {
		this.broadcast = broadcast;
	}

	public File getConfigFile() {
		return ConfigFile;
	}

	public void setConfigFile(File configFile) {
		ConfigFile = configFile;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ArrayList<GLevelItem> getItems() {
		return items;
	}

	public void setItems(ArrayList<GLevelItem> items) {
		this.items = items;
	}

	public Integer getCoins() {
		return coins;
	}

	public void setCoins(Integer coins) {
		this.coins = coins;
	}

	public ItemStack getItem(ItemStack is, List<String> lor) {
		ItemMeta im = is.getItemMeta();
		if (lor != null) {
			im.setLore(lor);
		}
		is.setItemMeta(im);
		return is;

	}

}
