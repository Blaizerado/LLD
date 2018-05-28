package at.ltd.gungame.level;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import at.ltd.Main;
import at.ltd.adds.utils.config.ConfigManager;
import at.ltd.gungame.events.custom.EventGunGameLevelSet;
import at.ltd.gungame.guns.utils.Gun;
import at.ltd.gungame.guns.utils.GunInterface;
import at.ltd.gungame.guns.utils.GunUtils;
import at.ltd.gungame.guns.utils.bullet.BulletUtils;
import at.ltd.gungame.guns.utils.rocket.RocketUtils;

public class GLevelUtils {

	public static HashMap<Integer, GLevel> G_LEVELS = new HashMap<>();
	public static String FOLDER = "plugins/LTD/GLevel/";
	public static int MAX_LEVEL = 0;
	public static String lorText = "§a§oLevelitem";

	public static void init() {
		read();
		Main.addReloadHandler(() -> {
			read();
		});
	}

	public static void read() {
		File folder = new File(FOLDER);
		MAX_LEVEL = 0;
		synchronized (G_LEVELS) {
			G_LEVELS.clear();
			for (File f : folder.listFiles()) {
				int id = Integer.valueOf(f.getName().replace(".txt", ""));
				if (id > MAX_LEVEL || MAX_LEVEL == id) {
					MAX_LEVEL = id;
				}
				GLevel gl = new GLevel(f);
				try {
					gl.Read();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("ERROR WHILE READING GLevel : " + f);
				}

				G_LEVELS.put(gl.getId(), gl);
			}
		}

	}

	public static GLevel getGLevel(int i) {
		synchronized (G_LEVELS) {
			if (i > MAX_LEVEL || i == MAX_LEVEL) {
				return G_LEVELS.get(MAX_LEVEL);
			}
			return G_LEVELS.get(i);
		}

	}

	public static boolean containsGLevel(int i) {
		synchronized (G_LEVELS) {
			return G_LEVELS.containsKey(i);
		}
	}

	public static boolean isItem(ItemStack is) {
		return is != null;
	}

	public static boolean isSpace(Player p, Integer i) {
		PlayerInventory inv = p.getInventory();
		if (i == 103) {
			if (isItem(inv.getHelmet())) {
				return false;
			}
		}
		if (i == 102) {
			if (isItem(inv.getChestplate())) {
				return false;
			}
		}
		if (i == 101) {
			if (isItem(inv.getLeggings())) {
				return false;
			}
		}
		if (i == 100) {
			if (isItem(inv.getBoots())) {
				return false;
			}
		}
		if (isItem(inv.getItem(i))) {
			return false;
		}
		return true;
	}

	public static synchronized void setInventory(Player p, Integer i) {
		removeItems(p);
		GLevel gl = getGLevel(i);
		ArrayList<GLevelItem> nospace = new ArrayList<>();
		EventGunGameLevelSet ev = new EventGunGameLevelSet(p, gl, (ArrayList<GLevelItem>) gl.getItems().clone());
		Bukkit.getPluginManager().callEvent(ev);
		for (GLevelItem gli : ev.getNewItems()) {
			if (!isSpace(p, gli.getSlot())) {
				nospace.add(gli);
			} else {
				Boolean finish = false;
				if (gli.getSlot().intValue() == 103) {
					// player.getInventory().setHelmet(gli.getItemStack());
					finish = true;
				}
				if (gli.getSlot().intValue() == 102) {
					// player.getInventory().setChestplate(gli.getItemStack());
					finish = true;
				}
				if (gli.getSlot().intValue() == 101) {
					// player.getInventory().setLeggings(gli.getItemStack());
					finish = true;
				}
				if (gli.getSlot().intValue() == 100) {
					// player.getInventory().setBoots(gli.getItemStack());
					finish = true;
				}
				if (!finish) {
					if (gli instanceof GLevelGun) {
						GLevelGun gun = (GLevelGun) gli;
						p.getInventory().setItem(gun.getSlot(), gun.getGun(p).getItemStack());
					} else {
						p.getInventory().setItem(gli.getSlot(), gli.getItemStack());
					}

				}
			}
		}
		for (GLevelItem gli : nospace) {
			if (gli instanceof GLevelGun) {
				GLevelGun gun = (GLevelGun) gli;
				p.getInventory().addItem(gun.getGun(p).getItemStack());
			} else {
				p.getInventory().addItem(gli.getItemStack());
			}

		}
	}

	public static void removeItems(Player p) {
		try {
			ArrayList<ItemStack> ar = new ArrayList<>();
			if (GLevelItem.isMarked(p.getInventory().getHelmet())) {
				p.getInventory().setHelmet(null);
			}
			if (GLevelItem.isMarked(p.getInventory().getChestplate())) {
				p.getInventory().setChestplate(null);
			}
			if (GLevelItem.isMarked(p.getInventory().getLeggings())) {
				p.getInventory().setLeggings(null);
			}
			if (GLevelItem.isMarked(p.getInventory().getBoots())) {
				p.getInventory().setBoots(null);
			}
			for (ItemStack is : p.getInventory().getContents().clone()) {
				if (GLevelItem.isMarked(is)) {
					ar.add(is);
				}
			}

			for (ItemStack is : p.getInventory().getContents()) {
				if (is == null) {
					continue;
				}
				Gun gun = GunUtils.getGunMemoryManaged(is);
				if (gun != null) {
					if (gun.isGunGameItem()) {
						ar.add(is);
						gun.removeGunFromMemory();
						continue;
					}
				}

				if (GLevelItem.isMarked(is)) {
					ar.add(is);
				}
			}

			for (ItemStack is : ar) {
				p.getInventory().removeItem(is);
			}
		} catch (Exception e) {
			System.out.println("Exception is catched!");
			e.printStackTrace();
		}
	}

	public static List<Integer> getArmorSlots() {
		int[] ints = {103, 102, 101, 100};
		List<Integer> intList = new ArrayList<Integer>();
		for (int index = 0; index < ints.length; index++) {
			intList.add(ints[index]);
		}
		return intList;
	}

	public static void setLevel(Player p, int level) {
		HashMap<String, String> hm = new HashMap<>();
		Integer slot = 0;
		Integer pos = 0;
		while (slot < 36) {
			if (p.getInventory().getItem(slot) != null) {
				ItemStack is = p.getInventory().getItem(slot);
				if (GunUtils.isGun(is)) {
					GunInterface gi = GunUtils.searchGunByItem(is);
					hm.put("GUN" + slot, gi.getGunID() + "");
				} else if (BulletUtils.validateItemStack(is)) {
					hm.put("AMMO" + slot, is.getAmount() + "");

				} else if (RocketUtils.validateItemStack(is)) {
					hm.put("ROCKET" + slot, is.getAmount() + "");
				} else {
					if (GLevelItem.isMarked(is)) {
						is = GLevelItem.removeMark(is);
					}
					hm.put("ITEM_" + slot, GLevel.isToString(is).replace("/²&a&oLevelitem", ""));
				}
				pos++;
			}
			slot++;
		}

		if (p.getInventory().getHelmet() != null) {
			hm.put("ITEM_103", GLevel.isToString(p.getInventory().getHelmet()).replace("/²&a&oLevelitem", ""));
		}
		if (p.getInventory().getChestplate() != null) {
			hm.put("ITEM_102", GLevel.isToString(p.getInventory().getChestplate()).replace("/²&a&oLevelitem", ""));
		}
		if (p.getInventory().getLeggings() != null) {
			hm.put("ITEM_101", GLevel.isToString(p.getInventory().getLeggings()).replace("/²&a&oLevelitem", ""));
		}
		if (p.getInventory().getBoots() != null) {
			hm.put("ITEM_100", GLevel.isToString(p.getInventory().getBoots()).replace("/²&a&oLevelitem", ""));
		}

		HashMap<String, String> items2 = ConfigManager.readConfig(FOLDER + level + ".txt");
		HashMap<String, String> items = (HashMap<String, String>) items2.clone();
		for (String s : items2.keySet()) {
			if (s.startsWith("ITEM_")) {
				items.remove(s);
			}
			if (s.startsWith("GUN")) {
				items.remove(s);
			}
			if (s.startsWith("AMMO")) {
				items.remove(s);
			}
			if (s.startsWith("ROCKET")) {
				items.remove(s);
			}
		}

		for (String s : hm.keySet()) {
			items.put(s, hm.get(s));
		}
		ConfigManager.setConfig(items, FOLDER + level + ".txt");

	}

}
