package at.ltd.gungame.events.custom;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import at.ltd.adds.game.player.GamePlayer;

public class EventGunGameSpawn extends Event {

	private final static HandlerList handlers = new HandlerList();
	private Location spawnLoc;
	private Player player;
	private int gungamelevel;
	private GamePlayer gunGamePlayer;
	public EventGunGameSpawn(Location loc, Player p, GamePlayer ggp) {
		this.spawnLoc = loc;
		this.player = p;
		this.gunGamePlayer = ggp;
		this.gungamelevel = ggp.getGunGameLevel();
	}

	public Location getSpawnLocation() {
		return spawnLoc;
	}

	public void setSpawnLocation(Location spawnLoc) {
		this.spawnLoc = spawnLoc;
	}

	public GamePlayer getGunGamePlayer() {
		return gunGamePlayer;
	}

	public Player getPlayer() {
		return player;
	}

	public int getGunGameLevel() {
		return gungamelevel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;

	}

}
