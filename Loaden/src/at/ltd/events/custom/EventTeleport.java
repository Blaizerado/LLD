package at.ltd.events.custom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import at.ltd.adds.game.player.GamePlayer;

public class EventTeleport extends Event {
	private Player player;
	private Boolean cancelled = false;
	private Location loc;
	private String reason;
	private String cancelReason;
	private static final HandlerList handlers = new HandlerList();

	public EventTeleport(Player p, Location loc, String reason) {
		if (!Bukkit.isPrimaryThread()) {
			throw new IllegalAccessError("Only on the Main thread!");
		}
		this.player = p;
		this.loc = loc;
		this.reason = reason;
	}

	public Location getLocation() {
		return loc;
	}

	public String getReason() {
		return reason;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public Player getPlayer() {
		return player;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public GamePlayer getGunGamePlayer() {
		return GamePlayer.getGamePlayer(player);
	}

	public void setCancelled(boolean c, String reason) {
		cancelled = c;
		this.cancelReason = reason;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;

	}
}
