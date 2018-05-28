package at.ltd.gungame.events.custom;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventVoteTick extends Event {

	private final static HandlerList handlers = new HandlerList();
	private int tick;
	public EventVoteTick(int tick) {
		this.tick = tick;
	}

	public int getTick() {
		return tick;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;

	}

}
