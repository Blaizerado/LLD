package at.ltd.gungame.events.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import at.ltd.adds.game.player.GamePlayer;

public class EventLeaveGame extends Event {
	private Player player;
	private GamePlayer ggp;
	private Boolean cancelled = false;
	private Boolean handled = false;
	private static final HandlerList handlers = new HandlerList();

	public EventLeaveGame(Player p, GamePlayer ggp) {
		this.player = p;
		this.ggp = ggp;

	}

	public boolean isCancelled() {
		return cancelled;
	}

	public Player getPlayer() {
		return player;
	}

	public GamePlayer getGunGamePlayer() {
		return ggp;
	}

	public void setCancelled(boolean c) {
		cancelled = c;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;

	}

	public Boolean isHandled() {
		return handled;
	}

	public void setHandled(Boolean handled) {
		this.handled = handled;
	}

}
