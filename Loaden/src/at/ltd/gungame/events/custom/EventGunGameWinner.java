package at.ltd.gungame.events.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import at.ltd.adds.game.player.GamePlayer;

public class EventGunGameWinner extends Event {

	private final static HandlerList handlers = new HandlerList();
	private Player player;
	private int gungamelevel;
	private GamePlayer gunGamePlayer;
	
	public EventGunGameWinner(Player p, GamePlayer ggp) {
		this.player = p;
		this.gunGamePlayer = ggp;
		this.gungamelevel = ggp.getGunGameLevel();
	}

	public GamePlayer getGunGamePlayer() {
		return gunGamePlayer;
	}

	public Player getPlayer() {
		return player;
	}

	public int getGunGameLevel() {
		return gungamelevel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;

	}

}
