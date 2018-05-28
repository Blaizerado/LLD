package at.ltd.events.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventPlayerJoinAsync extends Event {

	private final static HandlerList handlers = new HandlerList();

	private Player player;

	public EventPlayerJoinAsync(Player p) {
		this.player = p;
	}
	

	public Player getPlayer() {
		return player;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;

	}

}
