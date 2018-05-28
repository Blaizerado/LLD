package at.ltd.lobby.shop.reflections;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventPlayerKlickOnEntity extends Event {

	private final static HandlerList handlers = new HandlerList();
	private Player player;
	private int id;
	public EventPlayerKlickOnEntity(Player player, int id) {
		this.player = player;
		this.id = id;
	}

	public Player getPlayer() {
		return player;
	}

	public int getEntityID() {
		return id;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;

	}

}
