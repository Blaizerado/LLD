package at.ltd.adds.utils.config;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import at.ltd.adds.utils.LocUtils;
import at.ltd.adds.utils.serializer.ItemSerializer;

public class ConfigCoderRegister {

	public static void add(String name, ConfigSerializer ser) {
		ConfigUtils.TYPE_CODER.put(name.toUpperCase(), ser);
	}

	public static void register() {
		add("integer", new ConfigSerializer() {

			@Override
			public String serialize(Object data) {
				return data.toString();
			}

			@Override
			public Object deserialize(String data) {
				return Integer.valueOf(data);
			}
		});
		add("int", new ConfigSerializer() {

			@Override
			public String serialize(Object data) {
				return data.toString();
			}

			@Override
			public Object deserialize(String data) {
				return Integer.valueOf(data);
			}
		});
		add("double", new ConfigSerializer() {

			@Override
			public String serialize(Object data) {
				return Double.toString((double) data);
			}

			@Override
			public Object deserialize(String data) {
				return Double.valueOf(data);
			}
		});
		add("long", new ConfigSerializer() {

			@Override
			public String serialize(Object data) {
				return Long.toString((long) data);
			}

			@Override
			public Object deserialize(String data) {
				return Long.valueOf(data);
			}
		});
		add("float", new ConfigSerializer() {

			@Override
			public String serialize(Object data) {
				return Float.toString((float) data);
			}

			@Override
			public Object deserialize(String data) {
				return Float.valueOf(data);
			}
		});
		add("boolean", new ConfigSerializer() {

			@Override
			public String serialize(Object data) {
				return Boolean.toString((Boolean) data);
			}

			@Override
			public Object deserialize(String data) {
				return Boolean.valueOf(data);
			}
		});
		add("string", new ConfigSerializer() {

			@Override
			public String serialize(Object data) {
				return data.toString();
			}

			@Override
			public Object deserialize(String data) {
				return data;
			}
		});
		add("itemstack", new ConfigSerializer() {

			@Override
			public String serialize(Object data) {
				return ItemSerializer.serialize((ItemStack) data);
			}

			@Override
			public Object deserialize(String data) {
				return ItemSerializer.deserialize(data);
			}
		});
		add("location", new ConfigSerializer() {

			@Override
			public String serialize(Object data) {
				return LocUtils.locationToString((Location) data);
			}

			@Override
			public Object deserialize(String data) {
				return LocUtils.locationByString(data);
			}
		});

	}

}
