package at.ltd.adds.utils.serializer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

public class ItemSerializerFirework implements Serializable {

	private static final long serialVersionUID = -8099190504164858715L;

	public Integer power;
	public List<FireworkEffect> effects = new ArrayList<>();

	public Map<String, Object> getSettingsAsList() {
		HashMap<String, Object> p = new HashMap<>();
		p.put("POWER", power);
		ArrayList<ItemSerializerFireworkEffect> list = new ArrayList<>();
		for (FireworkEffect fe : effects) {
			list.add(ItemSerializerFireworkEffect.serialize(fe));
		}
		p.put("EFFECTS", list);
		return p;
	}

	@SuppressWarnings("unchecked")
	public ItemSerializerFirework setSettingsViaMap(HashMap<String, Object> settings) {
		power = (int) settings.get("POWER");
		ArrayList<ItemSerializerFireworkEffect> list = (ArrayList<ItemSerializerFireworkEffect>) settings.get("EFFECTS");
		for (ItemSerializerFireworkEffect isfe : list) {
			effects.add(isfe.deserialize());
		}
		return this;
	}

	public FireworkMeta getFireworkMeta() {
		ItemStack is = new ItemStack(Material.FIREWORK);
		FireworkMeta meta = (FireworkMeta) is.getItemMeta();
		meta.setPower(power);
		meta.addEffects(effects);
		return meta;
	}

	public static ItemSerializerFirework setSettingsByItem(ItemStack is) {
		ItemSerializerFirework isf = new ItemSerializerFirework();
		FireworkMeta meta = (FireworkMeta) is.getItemMeta();
		isf.setPower(meta.getPower());
		isf.setEffects(meta.getEffects());
		return isf;
	}

	public String getBASE64String() {
		return ItemSerializerUtils.objectToString(getSettingsAsList());
	}

	@SuppressWarnings("unchecked")
	public ItemSerializerFirework setSettingsViaBASE64(String base) {
		setSettingsViaMap((HashMap<String, Object>) ItemSerializerUtils.objectFromString(base));
		return this;
	}

	public int getPower() {
		return power;
	}
	public void setPower(int power) {
		this.power = power;
	}

	public List<FireworkEffect> getEffects() {
		return effects;
	}

	public void setEffects(List<FireworkEffect> effects) {
		this.effects = effects;
	}

}
