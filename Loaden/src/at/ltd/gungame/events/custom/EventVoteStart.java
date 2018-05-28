package at.ltd.gungame.events.custom;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventVoteStart extends Event {

	private final static HandlerList handlers = new HandlerList();
	public EventVoteStart() {

	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;

	}

}
