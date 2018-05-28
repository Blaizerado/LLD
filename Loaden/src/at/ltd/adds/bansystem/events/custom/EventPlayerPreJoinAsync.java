package at.ltd.adds.bansystem.events.custom;

import java.util.UUID;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventPlayerPreJoinAsync extends Event {

	private static final HandlerList handlers = new HandlerList();

	private UUID uuid;
	public EventPlayerPreJoinAsync(UUID uuid) {
		this.uuid = uuid;
	}

	public UUID getUUID() {
		return uuid;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;

	}

}
