package at.ltd.gungame.utils.melee;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

import at.ltd.adds.utils.serializer.ItemSerializer;
import at.ltd.gungame.guns.utils.GunInterface;
import at.ltd.gungame.guns.utils.GunRegister;

public class MeleWeaponRegister {

	public static HashMap<String, Double> WEAPONS = new HashMap<>();

	public static void register() {
		WEAPONS.put(createWeaponString(ItemSerializer.deserialize("[V=002:DIAMOND_SWORD/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]")), 20.00D);
		WEAPONS.put(createWeaponString(ItemSerializer.deserialize("[V=002:STONE_SWORD/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]")), 20.00D);
		WEAPONS.put(createWeaponString(ItemSerializer.deserialize("[V=002:WOOD_SWORD/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]")), 20.00D);
		WEAPONS.put(createWeaponString(ItemSerializer.deserialize("[V=002:IRON_SWORD/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]")), 20.00D);
		WEAPONS.put(createWeaponString(ItemSerializer.deserialize("[V=002:WOOD_AXE/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]")), 20.00D);
		WEAPONS.put(createWeaponString(ItemSerializer.deserialize("[V=002:LEATHER/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]")), 20.00D);
		WEAPONS.put(createWeaponString(ItemSerializer.deserialize("[V=002:PAPER/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]")), 20.00D);
		WEAPONS.put(createWeaponString(ItemSerializer.deserialize("[V=002:INK_SACK/³1/³8/³/³/³/³/³/³/³/³/³/³/³/³/³/³]")), 20.00D);
		WEAPONS.put(createWeaponString(ItemSerializer.deserialize("[V=002:STRING/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]")), 20.00D);
		WEAPONS.put(createWeaponString(ItemSerializer.deserialize("[V=002:CLAY_BALL/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]")), 20.00D);
		WEAPONS.put(createWeaponString(ItemSerializer.deserialize("[V=002:SUGAR/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]")), 20.00D);
		WEAPONS.put(createWeaponString(ItemSerializer.deserialize("[V=002:WOOD_PICKAXE/³1/³0/³&cSpitzhacke/³/³/²&c&a/³/³/³/³/³/³/³/³/³/³/³]")), 20.00D);

		
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
