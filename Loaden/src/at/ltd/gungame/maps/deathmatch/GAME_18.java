package at.ltd.gungame.maps.deathmatch;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import at.ltd.Main;
import at.ltd.adds.utils.LocUtils;
import at.ltd.adds.utils.config.ConfigManager;
import at.ltd.adds.utils.serializer.ItemSerializer;
import at.ltd.gungame.maps.maputils.GameMap;

public class GAME_18 implements GameMap, Listener {

	private static ArrayList<Location> spawns = new ArrayList<>();

	@Override
	public void onRegister() {
		spawns.clear();
		World world = Bukkit.getWorld("GAME_18");
		String saveLoc = "plugins/LTD/WorldSpawn/" + world.getName() + ".txt";
		HashMap<String, String> hm = ConfigManager.readConfig(saveLoc);
		for (String s : hm.keySet()) {
			spawns.add(LocUtils.locationByString(hm.get(s)));
		}
	}

	@Override
	public void onLoad() {
		Main.registerListener(this);
	}

	@Override
	public void onUnload() {
		HandlerList.unregisterAll(this);
	}

	@Override
	public String getMapName() {
		return "Shipment";
	}

	@Override
	public ItemStack getIcon() {
		return ItemSerializer.deserialize("[V=002:STORAGE_MINECART/�1/�0/�/�/�/�/�/�/�/�/�/�/�/�/�/�]");
	}

	@Override
	public ArrayList<Location> getSpawns() {
		return spawns;
	}

	@Override
	public String toString() {
		return getMapName();
	}

}
