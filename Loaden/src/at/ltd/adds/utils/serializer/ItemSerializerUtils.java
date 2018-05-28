package at.ltd.adds.utils.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ItemSerializerUtils {

	public static List<Material> leatherarmor = new ArrayList<Material>() {
		{
			add(Material.LEATHER_BOOTS);
			add(Material.LEATHER_CHESTPLATE);
			add(Material.LEATHER_HELMET);
			add(Material.LEATHER_LEGGINGS);
		}
	};

	public static String getEnchants(ItemStack i) {
		List<String> e = new ArrayList<String>();
		Map<Enchantment, Integer> en = i.getEnchantments();
		for (Enchantment t : en.keySet()) {
			e.add(t.getName() + ":" + en.get(t));
		}
		return StringUtils.join(e, ",");
	}

	public static String listToString(List<String> list) {
		String fi = "";
		for (String s : list) {
			fi += s + "/²";
		}
		fi = fi.substring(0, fi.length() - 2);
		return fi;
	}

	public static List<String> stringToList(String item) {
		List<String> list = Arrays.asList(item.split("/²"));
		return list;
	}

	public static String leatherArmorToString(ItemStack is) {
		String ser = "";
		ItemMeta meta = is.getItemMeta();
		if (meta instanceof LeatherArmorMeta) {
			LeatherArmorMeta laMeta = (LeatherArmorMeta) meta;
			ser = "" + laMeta.getColor().asRGB();
		}
		
		return ser;
	}
	
	public static Object objectFromString(String s) {
		try {

			byte[] data = Base64.getDecoder().decode(s);
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
			Object o = ois.readObject();
			ois.close();
			return o;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String objectToString(Object o) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
			oos.close();
			return Base64.getEncoder().encodeToString(baos.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
