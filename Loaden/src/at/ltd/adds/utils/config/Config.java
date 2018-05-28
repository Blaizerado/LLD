package at.ltd.adds.utils.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import at.ltd.Main;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.events.custom.EventConfigReload;

/**
 * All methods are thread safe.
 * 
 * @author NyroForce
 * @since 09.12.2017
 * @version 1.0.1
 */
public class Config extends ConfigMethods implements Listener {

	private static final String SAVE_LOC = "plugins/LTD/Configs/";
	public static final HashMap<String, Config> CONFIGS = new HashMap<>();
	private static boolean listener = false;

	private String CONFIG_LOC = null;
	private HashMap<String, String> DATA = null;
	
	private boolean reloadAble = false;
	private boolean reloadAsyncAble = false;
	private Runnable reloadhandler;
	
	private String name = null;

	public Config(String name) {
		if (!listener) {
			listener = true;
			Main.registerListener(this);
		}

		this.CONFIG_LOC = SAVE_LOC + name + ".config";
		load();
		AsyncThreadWorkers.submitDelayedWorkSec(() -> save(), 5);
		this.name = name;
		synchronized (CONFIGS) {
			CONFIGS.put(name, this);
		}

	}

	public String getConfigName() {
		return name;
	}

	public Config put(String key, Object value) {
		if (value instanceof List<?>) {
			throw new IllegalAccessError("List has to be saved with putList().");
		}
		if (!DATA.containsKey(key)) {
			modifyPut(key, ConfigUtils.convertToString(value));
		}
		return this;
	}
	public boolean contains(String key) {
		return modifyContains(key);
	}
	public Config remove(String key) {
		modifyRemove(key);
		return this;
	}

	public Config putList(String key, List<?> value, Class<?> type) {
		modifyPut(key, ConfigUtils.convertList((List<Object>) value, type));
		return this;
	}

	public String getRawValue(String key) {
		return modifyGet(key);
	}

	public int getInteger(String key) {
		return (int) ConfigUtils.convertToObject(modifyGet(key));
	}
	public String getString(String key) {
		return (String) ConfigUtils.convertToObject(modifyGet(key));
	}
	public double getDoube(String key) {
		return (double) ConfigUtils.convertToObject(modifyGet(key));
	}
	public float getFloat(String key) {
		return (float) ConfigUtils.convertToObject(modifyGet(key));
	}
	public boolean getBoolean(String key) {
		return (boolean) ConfigUtils.convertToObject(modifyGet(key));
	}
	public ItemStack getItemStack(String key) {
		return (ItemStack) ConfigUtils.convertToObject(modifyGet(key));
	}
	public List<Object> getList(String key) {
		String data = modifyGet(key);
		return ConfigUtils.convertList(data);
	}
	public Location getLocation(String key) {
		return (Location) ConfigUtils.convertToObject(modifyGet(key));
	}
	public Object getRawObject(String key) {
		return ConfigUtils.convertToObject(modifyGet(key));
	}
	public HashMap<String, String> cloneData() {
		return modifyClone();
	}

	public Config putRaw(String key, String value) {
		if (!modifyContains(key)) {
			modifyPut(key, value);
		}
		return this;
	}

	public Config save() {
		synchronized (this) {
			Map<String, String> sorted = new TreeMap<>(DATA);
			ConfigManager.setConfig(sorted, CONFIG_LOC);
			return this;
		}
	}
	public Config load() {
		synchronized (this) {
			DATA = ConfigManager.readConfig(CONFIG_LOC);
			return this;
		}
	}

	private void modifyPut(String key, String value) {
		synchronized (this) {
			DATA.put(key, value);
		}
	}
	private void modifyRemove(String key) {
		synchronized (this) {
			DATA.remove(key);
		}
	}

	private String modifyGet(String key) {
		synchronized (this) {
			return DATA.get(key);
		}
	}
	private boolean modifyContains(String key) {
		synchronized (this) {
			return DATA.containsKey(key);
		}
	}
	private HashMap<String, String> modifyClone() {
		synchronized (this) {
			return (HashMap<String, String>) DATA.clone();
		}
	}

	/*
	 * Reload
	 */

	@EventHandler
	public void on(EventConfigReload e) {
		ArrayList<Config> configs = new ArrayList<>();
		synchronized (CONFIGS) {
			for (Config config : CONFIGS.values()) {
				configs.add(config);
			}
		}
		for (Config config : configs) {
			if (config.isReloadAble()) {
				config.load();
				if (config.reloadhandler != null) {
					try {
						if (config.isAsyncReloadAble()) {
							AsyncThreadWorkers.submitWork(() -> config.reloadhandler.run());
						}else {
							config.reloadhandler.run();
						}
						
					} catch (Exception e2) {
						System.out.println("[LTD] ERROR ON RELOAD CONFIG HANDLER CONFIGNAME: " + config.getConfigName());
						e2.printStackTrace();
					}

				}
			}
		}
	}

	public boolean isReloadAble() {
		synchronized (this) {
			return reloadAble;
		}
	}

	public boolean isAsyncReloadAble() {
		synchronized (this) {
			return reloadAsyncAble;
		}
	}

	public Config setReloadHandler(Runnable run) {
		synchronized (this) {
			reloadhandler = run;
			return this;
		}
	}

	public Config setReloadAble(boolean reloadable) {
		synchronized (this) {
			this.reloadAble = reloadable;
			return this;
		}
	}
	/**
	 * Only changes the calling thread.
	 * 
	 * @param asyncreloadable
	 * @return
	 */
	public Config setAsyncReloadAble(boolean asyncreloadable) {
		synchronized (this) {
			this.reloadAsyncAble = asyncreloadable;
			if (reloadAble == false) {
				if (asyncreloadable = true) {
					System.out.println("[LTD | CONFIG] setAsyncReloadAble = true but Reloadable is false?");
				}
			}
			return this;
		}

	}
}
