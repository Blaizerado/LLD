package at.ltd.gungame.maps.deathmatch;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import at.ltd.Main;
import at.ltd.adds.utils.LocUtils;
import at.ltd.adds.utils.config.ConfigManager;
import at.ltd.gungame.maps.maputils.GameMap;
import at.ltd.gungame.utils.airdrop.AirdropSpawn;

public class GAME_01 implements GameMap, Listener, AirdropSpawn {

	private static ArrayList<Location> spawns = new ArrayList<>();
	private static ArrayList<Location> airdrop_spawns = new ArrayList<Location>() {
		{
			add(LocUtils.locationByString("45.0*150.0*42.0*0.0*0.0*GAME_01"));
			add(LocUtils.locationByString("7.0*150.0*54.0*0.0*0.0*GAME_01"));
			add(LocUtils.locationByString("-28.0*150.0*34.0*0.0*0.0*GAME_01"));
			add(LocUtils.locationByString("-19.0*150.0*81.0*0.0*0.0*GAME_01"));
			add(LocUtils.locationByString("6.0*150.0*85.0*0.0*0.0*GAME_01"));
		}
	};

	@Override
	public void onRegister() {
		spawns.clear();
		World world = Bukkit.getWorld("GAME_01");
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
		return "Mirage CS:GO";
	}

	@Override
	public ArrayList<Location> getSpawns() {
		return spawns;
	}

	@Override
	public ItemStack getIcon() {
		return new ItemStack(Material.DEAD_BUSH);
	}

	@Override
	public String toString() {
		return getMapName();
	}

	@Override
	public ArrayList<Location> getAirdropSpawns() {
		return airdrop_spawns;
	}

}
