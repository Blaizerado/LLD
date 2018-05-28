package at.ltd.gungame.guns.RIFLES.SETUP;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import at.ltd.Main;
import at.ltd.adds.superuser.SuperUser;
import at.ltd.adds.utils.config.ConfigManager;
import at.ltd.adds.utils.serializer.ItemSerializer;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.events.custom.EventPlayerJoinAsync;
import at.ltd.gungame.guns.utils.GunInterface;
import at.ltd.gungame.guns.utils.enums.AmmoType;
import at.ltd.gungame.guns.utils.enums.GunType;

public class RifleConfigReader implements Listener {

	private static String folder = "plugins/LTD/";
	private static ArrayList<String> GUN_ERRORS = new ArrayList<>();

	public static void start() {
		GUN_ERRORS.clear();
		Main.addReloadHandler(() -> {
			RifleConfigReader.reloadConfig();
			if (GUN_ERRORS.size() != 0) {
				AsyncThreadWorkers.submitDelayedWorkSec(() -> {
					for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
						if (SuperUser.isSuperUser(p)) {
							p.sendMessage(Main.getPrefix() + "Errors on loading gun(s): §c"
									+ GUN_ERRORS.toString().replace("[", "").replace("]", ""));
						}
					}
				}, 2);
			}
		});
		Main.registerListener(new RifleConfigReader());
		RifleConfigMemory.DATA.clear();
		for (Object ob : RifleSetUp.guns) {
			String name = ob.getClass().getSimpleName();
			try {
				GunInterface gi = (GunInterface) ob;
				setConfig(name, gi.getGunID());
				readConfig(name, ob, gi);
			} catch (Exception e) {
				GUN_ERRORS.add(name);
				System.out.println("ERROR: GUNNAME: " + name);
				e.printStackTrace();
			}

		}
	}

	@EventHandler
	public void on(EventPlayerJoinAsync e) {
		Player p = e.getPlayer();
		AsyncThreadWorkers.submitDelayedTickWork(() -> {
			if (p.isOnline()) {
				if (SuperUser.isSuperUser(p)) {
					if (GUN_ERRORS.size() != 0) {
						p.sendMessage(Main.getPrefix() + "Errors on loading gun(s): §c"
								+ GUN_ERRORS.toString().replace("[", "").replace("]", ""));
					}
				}
			}
		}, 20 * 5);
	}

	public static void reloadConfig() {
		GUN_ERRORS.clear();
		RifleConfigMemory.DATA.clear();
		for (Object ob : RifleSetUp.guns) {
			String name = ob.getClass().getSimpleName();
			try {
				GunInterface gi = (GunInterface) ob;
				setConfig(name, gi.getGunID());
				readConfig(name, ob, gi);
			} catch (Exception e) {
				GUN_ERRORS.add(name);
				System.out.println("ERROR ON RELOAD: GUNNAME: " + name);
				e.printStackTrace();
			}

		}
	}

	public static void setConfig(String name, int id) {
		String gunfile = folder + "Guns/" + name + ".txt";
		HashMap<String, String> hm = ConfigManager.readConfig(gunfile);
		if (hm.isEmpty()) {
			HashMap<String, String> newconfig = (HashMap<String, String>) RifleStaticConfig.ststicConfig.clone();
			newconfig.put("name", name);
			newconfig.put("gunid", "" + id);
			ConfigManager.setConfig(newconfig, gunfile);
		}

		boolean set = false;
		for (String key : RifleStaticConfig.ststicConfig.keySet()) {
			if (!hm.containsKey(key)) {
				set = true;
				hm.put(key, RifleStaticConfig.ststicConfig.get(key));
			}
		}
		if (set) {
			ConfigManager.setConfig(hm, gunfile);
		}
		if (!hm.containsKey("name")) {
			hm.put("name", name);
			ConfigManager.setConfig(hm, gunfile);
		}
		if (!hm.containsKey("gunid")) {
			hm.put("gunid", "" + id);
			ConfigManager.setConfig(hm, gunfile);
		}

	}

	public static List<String> intValues = new ArrayList<String>() {
		{
			add("magazinesize");
			add("firespeed");
			add("reloadtime");
			add("bulletspertick");
			add("bulletflytime");
		}
	};
	public static List<String> boolValues = new ArrayList<String>() {
		{
			add("akimbo");
			add("special");
			add("instanthit");
		}
	};
	public static List<String> floatValues = new ArrayList<String>() {
		{
			add("bulletaccuracy");
			add("bulletspeed");
			add("gunvolume");
		}
	};

	public static void readConfig(String name, Object gun, GunInterface gi) {
		String gunfile = folder + "Guns/" + name + ".txt";
		HashMap<String, String> hm = ConfigManager.readConfig(gunfile);
		RifleConfigMemory.addGun(gi.getGunID(), hm);
		for (String key : hm.keySet()) {
			if (!"gunid".equals(key)) {
				Object value = null;
				if (intValues.contains(key)) {
					value = Integer.valueOf(hm.get(key));
				}

				if (boolValues.contains(key)) {
					value = Boolean.valueOf(hm.get(key));
				}
				if (floatValues.contains(key)) {
					value = Float.valueOf(hm.get(key));

				}
				if ("item".equals(key)) {
					value = ItemSerializer.deserialize(hm.get(key));
				}
				if ("ammo".equals(key)) {
					value = AmmoType.valueOf(hm.get(key));
				}
				if ("guntype".equals(key)) {
					value = GunType.valueOf(hm.get(key));
				}
				if ("reloadsound".equals(key)) {
					value = Sound.valueOf(hm.get(key));
				}
				if ("sound".equals(key)) {
					value = Sound.valueOf(hm.get(key));

				}
				if ("damage".equals(key)) {
					value = Double.valueOf(hm.get(key)) / 5;

				}
				if ("name".equals(key)) {
					value = "§6" + ChatColor.translateAlternateColorCodes('&', hm.get(key));
				}
				if (value == null) {
					System.out.println("ERROR(Or only a custom value): VALUE = NULL, GUN: " + name + ", KEY = " + key);
					continue;
				}
				set(gun, key, value);
			}
		}
	}

	private static boolean set(Object object, String fieldName, Object fieldValue) {
		Class<?> clazz = object.getClass();
		while (clazz != null) {
			try {
				Field field = clazz.getDeclaredField(fieldName);
				field.setAccessible(true);
				field.set(object, fieldValue);
				return true;
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}
		return false;
	}

}
