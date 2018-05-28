package at.ltd.gungame.events.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import at.ltd.gungame.maps.maputils.GameMap;

public class EventVote extends Event {

	private final static HandlerList handlers = new HandlerList();
	
	private Boolean cancelled = false;
	private Player player;
	private GameMap gameMap;
	
	public EventVote(Player player, GameMap gameMap) {
		this.player = player;
		this.gameMap = gameMap;
	}
	
	public Player getVoter(){
		return player;
	}
	
	public GameMap getVotedMap(){
		return gameMap;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean c) {
		cancelled = c;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;

	}

}
