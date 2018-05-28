package at.ltd.gungame.events.custom;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import at.ltd.gungame.round.GameRound;

public class EventGameTick extends Event {

	private final static HandlerList handlers = new HandlerList();
	private int tick;
	private int roundTimeMin;
	private int roundTimeSec;
	private GameRound gr;
	public EventGameTick(int tick, GameRound gameRound, int roundTimeMin, int roundTimeSec) {
		this.tick = tick;
		this.gr = gameRound;
		this.roundTimeMin = roundTimeMin;
		this.roundTimeSec = roundTimeSec;
	}

	public int getTick() {
		return tick;
	}

	public GameRound getGameRound() {
		return gr;
	}
	

	public int getRoundTimeMin() {
		return roundTimeMin;
	}

	public int getRoundTimeSec() {
		return roundTimeSec;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;

	}

}
