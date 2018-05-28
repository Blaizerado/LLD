package at.ltd.events.custom;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventPlayerMoveAsync extends Event {

	private final static HandlerList handlers = new HandlerList();

	private Location from;
	private Location to;
	private Block blockStandingOn;
	private Player player;

	public EventPlayerMoveAsync(Location from, Location to, Block standingon, Player p) {
		this.from = from;
		this.to = to;
		this.blockStandingOn = standingon;
		this.player = p;
	}

	public Location getFrom() {
		return from;
	}

	public Location getTo() {
		return to;
	}

	public Block getBlockStandingOn() {
		return blockStandingOn;
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
