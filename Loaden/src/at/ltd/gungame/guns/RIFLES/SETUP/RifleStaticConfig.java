package at.ltd.gungame.guns.RIFLES.SETUP;

import java.util.HashMap;

public class RifleStaticConfig {

	public static final HashMap<String, String> ststicConfig;

	static {
		HashMap<String, String> hm = new HashMap<>();
		hm.put("magazinesize", "30");
		hm.put("damage", "5");
		hm.put("item", "[V=002:DIAMOND/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]");
		hm.put("akimbo", "false");
		hm.put("special", "false");
		hm.put("ammo", "BULLET");
		hm.put("guntype", "ASSAULT_RIFLE");
		hm.put("sound", "BLOCK_SAND_HIT");
		hm.put("gunvolume", "2.00F");
		hm.put("reloadsound", "ENTITY_BAT_LOOP");
		hm.put("firespeed", "150");
		hm.put("bulletflytime", "2000");
		hm.put("reloadtime", "2000");
		hm.put("bulletspertick", "1");
		hm.put("bulletaccuracy", "0.15F");
		hm.put("bulletspeed", "3.00F");
		hm.put("instanthit", "false");
		ststicConfig = hm;
	}

}
