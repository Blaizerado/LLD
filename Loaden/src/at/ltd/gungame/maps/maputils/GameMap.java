package at.ltd.gungame.maps.maputils;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;


public interface GameMap {

	public void onRegister();

	public void onLoad();

	public void onUnload();

	public String getMapName();

	public ItemStack getIcon();

	public ArrayList<Location> getSpawns();
	

}
