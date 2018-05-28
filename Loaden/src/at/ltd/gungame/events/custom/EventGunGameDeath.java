package at.ltd.gungame.events.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventGunGameDeath extends Event {

	private final Player killer;
	private final Player killed;
	private int levelsback = 0;
	private static final HandlerList handlers = new HandlerList();

	public EventGunGameDeath(Player killer, Player killed) {
		this.killer = killer;
		this.killed = killed;
	}

	public Player getKiller() {
		return killer;
	}

	public Player getKilled() {
		return killed;
	}

	public int getLevelsBack() {
		return levelsback;
	}

	public void setLevelsBack(int i) {
		levelsback = i;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;

	}

}
