package at.ltd.gungame.utils;

import at.ltd.gungame.utils.airdrop.AirdropManager;
import at.ltd.gungame.utils.granade.Granade;
import at.ltd.gungame.utils.hellfire.HellfireManager;
import at.ltd.gungame.utils.medkit.Medkit;
import at.ltd.gungame.utils.melee.MeleWeapons;

public class GunGameUtils {

	public static void init() {
		AirdropManager.init();
		HellfireManager.init();
		MeleWeapons.init();
		Granade.init();
		
		Medkit.init();
	}

}
