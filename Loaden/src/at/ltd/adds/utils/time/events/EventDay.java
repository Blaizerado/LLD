package at.ltd.adds.utils.time.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventDay extends Event {

	private final static HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;

	}

}
