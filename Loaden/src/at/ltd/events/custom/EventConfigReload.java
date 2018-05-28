package at.ltd.events.custom;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventConfigReload extends Event {

	private final static HandlerList handlers = new HandlerList();
	public EventConfigReload() {
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;

	}

}
