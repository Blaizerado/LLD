package at.ltd.adds.utils.serializer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;

public final class ItemSerializer {

	private static int version = 2;
	public static ConcurrentHashMap<String, ItemStack> memory = new ConcurrentHashMap<>();

	public static String serialize(ItemStack i) {
		String[] parts = new String[17];
		parts[0] = i.getType().name();
		parts[1] = Integer.toString(i.getAmount());
		parts[2] = String.valueOf(i.getDurability());
		parts[3] = i.getItemMeta().getDisplayName();
		parts[4] = "";
		if (i.getItemMeta().hasLore()) {
			parts[5] = ItemSerializerUtils.listToString(i.getItemMeta().getLore());
		}

		if (i.getItemMeta().getItemFlags().size() != 0) {
			List<String> l = new ArrayList<>();
			for (ItemFlag iflag : i.getItemMeta().getItemFlags()) {
				l.add(iflag.name().replace("_", "a"));
			}
			parts[6] = ItemSerializerUtils.listToString(l);
		}
		parts[7] = ItemSerializerUtils.getEnchants(i);

		if (ItemSerializerUtils.leatherarmor.contains(i.getType())) {
			parts[8] = ItemSerializerUtils.leatherArmorToString(i);
		}

		if (i.getType() == Material.FIREWORK) {
			ItemSerializerFirework isf = ItemSerializerFirework.setSettingsByItem(i);
			parts[9] = isf.getBASE64String();
		}

		if (i.getType() == Material.POTION || i.getType() == Material.SNOW_BALL || i.getType() == Material.POTATO) {
		}
		String re = StringUtils.join(parts, "/³").replace("§", "&");
		re = "[V=002:" + re + "]";
		re = re.replace(" ", "\\³");
		return re;

	}

	public static ItemStack deserialize(String p) {
		if (memory.containsKey(p)) {
			return memory.get(p).clone();
		}
		int stringversion = getVersion(p);
		if (stringversion != version) {
			System.out.println("[ITEMSERIALIZER] NOT THE SAME VERSION! " + p);
		}
		p = p.replace("\\³", " ");
		p = removeAdds(p);
		p = p.replace("&", "§");
		String[] a = p.split("/³", -1);
		ItemStack i = new ItemStack(Material.getMaterial(a[0]), Integer.parseInt(a[1]));
		i.setDurability((short) Integer.parseInt(a[2]));
		ItemMeta meta = i.getItemMeta();
		if (a[3] != null && !a[3].equals("")) {
			meta.setDisplayName(a[3]);
		}

		if (a.length > 4) {
			if (a[5] != "" || a[5] != null) {
				List<String> list = ItemSerializerUtils.stringToList(a[5]);
				if (!list.isEmpty()) {
					meta.setLore(list);
				}
			}
		}

		if (a.length > 5) {
			if (a[6] != "" || a[6] != null) {
				for (String s : ItemSerializerUtils.stringToList(a[6])) {
					s = s.replaceAll("[^A-Za-z]+", "");
					if (s != null) {
						if (!s.equals("")) {
							if (!s.equals(" ")) {
								meta.addItemFlags(ItemFlag.valueOf(s.replace("a", "_")));
							}
						}

					}

				}
			}
		}

		if (a.length > 6) {
			if (a[7] != "" || a[7] != null) {
				String[] parts = a[7].split(",");
				for (String s : parts) {
					if (!s.contains(":")) {
						continue;
					}
					String label = s.split(":")[0];
					String amplifier = s.split(":")[1];
					Enchantment type = Enchantment.getByName(label);
					if (type == null) {
						continue;
					}
					int f;
					try {
						f = Integer.parseInt(amplifier);
					} catch (Exception ex) {
						continue;
					}
					meta.addEnchant(type, f, true);
				}
			}
		}

		if (a.length > 7) {
			if (a[8] != "" || a[8] != null) {
				if (meta instanceof LeatherArmorMeta) {
					LeatherArmorMeta laMeta = (LeatherArmorMeta) meta;
					laMeta.setColor(Color.fromRGB(Integer.valueOf(a[8])));
					i.setItemMeta(laMeta);
				}
			}
		}

		if (a.length > 8) {
			if (a[9] != "" || a[9] != null) {
				if (meta instanceof FireworkMeta) {
					FireworkMeta metaF = (FireworkMeta) i.getItemMeta();
					ItemSerializerFirework isf = new ItemSerializerFirework().setSettingsViaBASE64(a[9]);
					metaF.setPower(isf.getPower());
					metaF.addEffects(isf.getEffects());
					i.setItemMeta(metaF);
				}

			}
		}
		if (stringversion > 1) {
			if (a[10] != "" || a[10] != null) {
				if (meta instanceof PotionMeta) {
					i.setItemMeta(meta);
				}

			}
		}

		i.setItemMeta(meta);
		memory.put(p, i.clone());
		return i;
	}

	public static int getVersion(String s) {
		return returnThreeDigitNo(Integer.valueOf(s.substring(4, 6)));
	}

	private static int returnThreeDigitNo(int number) {
		int threeDigitNo = 0;
		int length = String.valueOf(number).length();
		if (length == 1) {
			threeDigitNo = 00 + number;
		}
		if (length == 2) {
			threeDigitNo = 0 + number;
		}
		if (length == 3) {
			threeDigitNo = number;
		}
		return threeDigitNo;
	}

	private static String removeAdds(String s) {
		return s.substring(7, s.length() - 1);
	}

}