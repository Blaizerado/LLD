package at.ltd.events.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventNewJoin extends Event {

	private final static HandlerList handlers = new HandlerList();

	private Player player;

	public EventNewJoin(Player p) {
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
