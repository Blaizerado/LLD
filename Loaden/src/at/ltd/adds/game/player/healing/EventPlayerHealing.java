package at.ltd.adds.game.player.healing;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventPlayerHealing extends Event {

	private final static HandlerList handlers = new HandlerList();
	private Player player;
	private double healing;
	private int lasthit;
	private int lasthealing;
	private boolean cancel;

	public EventPlayerHealing(Player p, double healing, int lasthit, int lasthealing, boolean cancel) {
		this.player = p;
		this.healing = healing;
		this.lasthit = lasthit;
		this.cancel = cancel;
		this.lasthealing = lasthealing;
	}

	public double getHealing() {
		return healing;
	}

	public void setHealing(double healing) {
		this.healing = healing;
	}

	public Player getPlayer() {
		return player;
	}

	public int getLastHitTime() {
		return lasthit;
	}

	public int getLastHealing() {
		return lasthealing;
	}

	public boolean isCanceled() {
		return cancel;
	}

	public void setCanceled(boolean cancel) {
		this.cancel = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;

	}

}
