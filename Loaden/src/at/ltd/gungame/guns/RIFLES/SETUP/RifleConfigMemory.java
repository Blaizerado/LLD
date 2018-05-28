package at.ltd.gungame.guns.RIFLES.SETUP;

import java.util.HashMap;

import at.ltd.gungame.guns.utils.GunInterface;

public class RifleConfigMemory {

	public static HashMap<Integer, HashMap<String, String>> DATA = new HashMap<>();

	public static HashMap<String, String> getConfig(GunInterface gunInterface) {
		return DATA.get(gunInterface.getGunID());
	}

	public static void addGun(Integer id, HashMap<String, String> data) {
		DATA.put(id, data);
	}

}
