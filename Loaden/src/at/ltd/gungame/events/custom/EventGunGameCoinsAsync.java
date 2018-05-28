package at.ltd.gungame.events.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventGunGameCoinsAsync extends Event {

	private final static HandlerList handlers = new HandlerList();
	private int coins;
	private Player player;
	public EventGunGameCoinsAsync(Player player, int coins) {
		this.player = player;
		this.coins = coins;
	}

	public int getCoins() {
		return coins;
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
