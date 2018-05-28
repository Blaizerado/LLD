package at.ltd.adds.utils.config;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class ConfigMethods {

	public static Config getInstance(String name) {
		synchronized (Config.CONFIGS) {
			if (Config.CONFIGS.containsKey(name)) {
				return Config.CONFIGS.get(name);
			}
		}
		return new Config(name);
	}

	public static int getInteger(String config, String key) {
		Config extraconfig = getInstance(config);
		return extraconfig.getInteger(key);
	}
	public static String getString(String config, String key) {
		Config extraconfig = getInstance(config);
		return extraconfig.getString(key);
	}
	public static double getDoube(String config, String key) {
		Config extraconfig = getInstance(config);
		return extraconfig.getDoube(key);
	}
	public static float getFloat(String config, String key) {
		Config extraconfig = getInstance(config);
		return extraconfig.getFloat(key);
	}
	public static boolean getBoolean(String config, String key) {
		Config extraconfig = getInstance(config);
		return extraconfig.getBoolean(key);
	}
	public static ItemStack getItemStack(String config, String key) {
		Config extraconfig = getInstance(config);
		return extraconfig.getItemStack(key);
	}

	public static List<Object> getList(String config, String key) {
		Config extraconfig = getInstance(config);
		return extraconfig.getList(key);
	}
	public static Location getLocation(String config, String key) {
		Config extraconfig = getInstance(config);
		return extraconfig.getLocation(key);
	}
	public static HashMap<String, String> cloneData(String config) {
		Config extraconfig = getInstance(config);
		return extraconfig.cloneData();
	}

	
	
	public static Config loadMyClass(String config, Object obj) {
		Config extraconfig = getInstance(config);
		for (Field field : getAllModelFields(obj.getClass())) {
			if (field.isAnnotationPresent(ConfigAble.class)) {
				ConfigAble configAble = field.getAnnotation(ConfigAble.class);
				String value = null;
				String key = configAble.key();
				try {
					if ("".equals(key)) {
						key = field.getName();
					}
					if (extraconfig.contains(key)) {
						value = extraconfig.getRawValue(key);
					}
					if (key != null && configAble.value() != null) {
						if (field.getType().isAssignableFrom(List.class)) {
							extraconfig.putRaw(key, "[TYPE:LIST]" + configAble.value());
						} else {
							extraconfig.putRaw(key, "[TYPE:" + field.getType().getSimpleName().toUpperCase() + "]" + configAble.value());
						}

						field.setAccessible(true);
						try {
							if (field.getType().isAssignableFrom(List.class)) {
								field.set(obj, extraconfig.getList(key));
							} else {
								field.set(obj, extraconfig.getRawObject(key));
							}
						} catch (IllegalArgumentException | IllegalAccessException e) {
							e.printStackTrace();
						}

					} else {
						throw new IllegalStateException("Value or Key are null!");
					}
				} catch (Exception e) {
					System.out.println("|-----------------------------------------------|");
					System.out.println("Error on field: " + field.getName() + ", Type: " + field.getType().getSimpleName().toUpperCase());
					System.out.println("Key: " + key);
					System.out.println("DefaultValue: " + configAble.value());
					System.out.println("Value: " + value);
					System.out.println("|-----------------------------------------------|");
					e.printStackTrace();
				}

			}

		}
		return extraconfig;
	}

	public static Config loadMyClass(String config, Class<?> clazz) {
		Config extraconfig = getInstance(config);
		for (Field field : getAllModelFields(clazz)) {
			if (field.isAnnotationPresent(ConfigAble.class)) {
				ConfigAble configAble = field.getAnnotation(ConfigAble.class);
				String value = null;
				String key = configAble.key();
				try {
					if ("".equals(key)) {
						key = field.getName();
					}
					if (extraconfig.contains(key)) {
						value = extraconfig.getRawValue(key);
					}
					if (key != null && configAble.value() != null) {

						if (field.getType().isAssignableFrom(List.class)) {
							extraconfig.putRaw(key, "[TYPE:LIST]" + configAble.value());
						} else {
							extraconfig.putRaw(key, "[TYPE:" + field.getType().getSimpleName().toUpperCase() + "]" + configAble.value());
						}

						field.setAccessible(true);
						try {
							if (field.getType().isAssignableFrom(List.class)) {
								field.set(null, extraconfig.getList(key));
							} else {
								field.set(null, extraconfig.getRawObject(key));
							}
						} catch (IllegalArgumentException | IllegalAccessException e) {
							e.printStackTrace();
						}

					} else {
						throw new IllegalStateException("Value or Key are null!");
					}
				} catch (Exception e) {
					System.out.println("|-----------------------------------------------|");
					System.out.println("Error on field: " + field.getName() + ", Type: " + field.getType().getSimpleName().toUpperCase());
					System.out.println("Key: " + key);
					System.out.println("DefaultValue: " + configAble.value());
					System.out.println("Value: " + value);
					System.out.println("|-----------------------------------------------|");
					e.printStackTrace();
				}

			}

		}
		return extraconfig;
	}

	private static List<Field> getAllModelFields(Class<?> aClass) {
		List<Field> fields = new ArrayList<>();
		do {
			Collections.addAll(fields, aClass.getDeclaredFields());
			aClass = aClass.getSuperclass();
		} while (aClass != null);
		return fields;
	}

	public static Config reloadConfig(String name) {
		Config con = Config.getInstance(name);
		con.load();
		return con;
	}

}
