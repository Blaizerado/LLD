package at.ltd.gungame.guns.events.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventMousePressTick extends Event {

	private static final HandlerList handlers = new HandlerList();

	private Player player;
	public EventMousePressTick(Player p) {
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
