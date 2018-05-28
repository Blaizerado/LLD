package at.ltd.gungame.maps.deathmatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import at.ltd.Main;
import at.ltd.adds.utils.LocUtils;
import at.ltd.adds.utils.config.ConfigManager;
import at.ltd.adds.utils.serializer.ItemSerializer;
import at.ltd.gungame.events.custom.EventGunGameSpawn;
import at.ltd.gungame.maps.maputils.GameMap;

public class GAME_04 implements GameMap, Listener {

	private static ArrayList<Location> spawns = new ArrayList<>();
	private static Location loc1;
	private static Location loc2;
	private static Location loc3;
	private static Location loc4;
	private static HashMap<Integer, Integer> LOC_SPAWNS = new HashMap<>();

	@Override
	public void onRegister() {
		spawns.clear();
		World world = Bukkit.getWorld("GAME_04");
		String saveLoc = "plugins/LTD/WorldSpawn/" + world.getName() + ".txt";
		HashMap<String, String> hm = ConfigManager.readConfig(saveLoc);
		for (String s : hm.keySet()) {
			spawns.add(LocUtils.locationByString(hm.get(s)));
		}
		loc1 = LocUtils.locationByString("185.0*158.0*93.0*0.0*0.0*GAME_04");
		loc2 = LocUtils.locationByString("186.0*157.0*133.0*0.0*0.0*GAME_04");
		loc3 = LocUtils.locationByString("166.0*157.0*115.0*0.0*0.0*GAME_04");
		loc4 = LocUtils.locationByString("208.0*158.0*115.0*0.0*0.0*GAME_04");
	}

	@Override
	public void onLoad() {
		Main.registerListener(this);
		LOC_SPAWNS.clear();
		LOC_SPAWNS.put(1, 0);
		LOC_SPAWNS.put(2, 0);
		LOC_SPAWNS.put(3, 0);
		LOC_SPAWNS.put(4, 0);
	}

	@Override
	public void onUnload() {
		HandlerList.unregisterAll(this);
	}

	@EventHandler
	public void on(EventGunGameSpawn e) {
		if (e.getGunGameLevel() == 0) {
			List<Entry<Integer, Integer>> list = entriesSortedByValues(LOC_SPAWNS);
			Integer LocInt = list.get(3).getKey();
			LOC_SPAWNS.replace(LocInt, list.get(3).getValue() + 1);

			if (LocInt == 1) {
				e.setSpawnLocation(loc1);
			} else if (LocInt == 2) {
				e.setSpawnLocation(loc2);
			} else if (LocInt == 3) {
				e.setSpawnLocation(loc3);
			} else if (LocInt == 4) {
				e.setSpawnLocation(loc4);
			}

		}
	}

	public static <K, V extends Comparable<? super V>> List<Entry<K, V>> entriesSortedByValues(Map<K, V> map) {

		List<Entry<K, V>> sortedEntries = new ArrayList<Entry<K, V>>(map.entrySet());

		Collections.sort(sortedEntries, new Comparator<Entry<K, V>>() {
			@Override
			public int compare(Entry<K, V> e1, Entry<K, V> e2) {
				return e2.getValue().compareTo(e1.getValue());
			}
		});

		return sortedEntries;
	}

	@Override
	public String getMapName() {
		return "Airdrop";
	}

	@Override
	public ArrayList<Location> getSpawns() {
		return spawns;
	}

	@Override
	public ItemStack getIcon() {
		return ItemSerializer.deserialize("[V=002:LADDER/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]");
	}

	@Override
	public String toString() {
		return getMapName();
	}

}
