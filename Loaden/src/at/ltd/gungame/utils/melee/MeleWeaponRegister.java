package at.ltd.gungame.utils.melee;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

import at.ltd.adds.utils.serializer.ItemSerializer;
import at.ltd.gungame.guns.utils.GunInterface;
import at.ltd.gungame.guns.utils.GunRegister;

public class MeleWeaponRegister {

	public static HashMap<String, Double> WEAPONS = new HashMap<>();

	public static void register() {
		WEAPONS.put(createWeaponString(ItemSerializer.deserialize("[V=002:DIAMOND_SWORD/�1/�0/�/�/�/�/�/�/�/�/�/�/�/�/�/�]")), 20.00D);
		WEAPONS.put(createWeaponString(ItemSerializer.deserialize("[V=002:STONE_SWORD/�1/�0/�/�/�/�/�/�/�/�/�/�/�/�/�/�]")), 20.00D);
		WEAPONS.put(createWeaponString(ItemSerializer.deserialize("[V=002:WOOD_SWORD/�1/�0/�/�/�/�/�/�/�/�/�/�/�/�/�/�]")), 20.00D);
		WEAPONS.put(createWeaponString(ItemSerializer.deserialize("[V=002:IRON_SWORD/�1/�0/�/�/�/�/�/�/�/�/�/�/�/�/�/�]")), 20.00D);
		WEAPONS.put(createWeaponString(ItemSerializer.deserialize("[V=002:WOOD_AXE/�1/�0/�/�/�/�/�/�/�/�/�/�/�/�/�/�]")), 20.00D);
		WEAPONS.put(createWeaponString(ItemSerializer.deserialize("[V=002:LEATHER/�1/�0/�/�/�/�/�/�/�/�/�/�/�/�/�/�]")), 20.00D);
		WEAPONS.put(createWeaponString(ItemSerializer.deserialize("[V=002:PAPER/�1/�0/�/�/�/�/�/�/�/�/�/�/�/�/�/�]")), 20.00D);
		WEAPONS.put(createWeaponString(ItemSerializer.deserialize("[V=002:INK_SACK/�1/�8/�/�/�/�/�/�/�/�/�/�/�/�/�/�]")), 20.00D);
		WEAPONS.put(createWeaponString(ItemSerializer.deserialize("[V=002:STRING/�1/�0/�/�/�/�/�/�/�/�/�/�/�/�/�/�]")), 20.00D);
		WEAPONS.put(createWeaponString(ItemSerializer.deserialize("[V=002:CLAY_BALL/�1/�0/�/�/�/�/�/�/�/�/�/�/�/�/�/�]")), 20.00D);
		WEAPONS.put(createWeaponString(ItemSerializer.deserialize("[V=002:SUGAR/�1/�0/�/�/�/�/�/�/�/�/�/�/�/�/�/�]")), 20.00D);
		WEAPONS.put(createWeaponString(ItemSerializer.deserialize("[V=002:WOOD_PICKAXE/�1/�0/�&cSpitzhacke/�/�/�&c&a/�/�/�/�/�/�/�/�/�/�/�]")), 20.00D);

		
		for(GunInterface gi : GunRegister.GUN_LIST.values()) {
			WEAPONS.put(createWeaponString(gi.getItem()), 5.00D);
		}
		
		// WEAPONS.put(createWeaponString(ItemSerializer.deserialize("")),
		// 10.00D);
	}

	public static synchronized String createWeaponString(ItemStack is) {
		return is.getType().name() + "-" + is.getDurability();
	}

}
