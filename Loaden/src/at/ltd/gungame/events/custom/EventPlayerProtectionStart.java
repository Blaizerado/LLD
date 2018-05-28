package at.ltd.gungame.events.custom;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import at.ltd.adds.game.player.GamePlayer;

public class EventPlayerProtectionStart extends Event{

	private final static HandlerList handlers = new HandlerList();
	
	private GamePlayer protectedPlayer;
	
	public EventPlayerProtectionStart(GamePlayer protectedPlayer) {
		this.protectedPlayer = protectedPlayer;
	}

	public GamePlayer getProtectedPlayer() {
		return protectedPlayer;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;

	}

}
