package at.ltd.gungame.events.custom;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import at.ltd.gungame.level.GLevel;
import at.ltd.gungame.level.GLevelItem;

public class EventGunGameLevelSet extends Event {

	private static final HandlerList handlers = new HandlerList();

	private Player p;
	private GLevel level;
	private ArrayList<GLevelItem> newItems;
	public EventGunGameLevelSet(Player p, GLevel level, ArrayList<GLevelItem> newItems) {
		this.newItems = newItems;
		this.p = p;
		this.level = level;
	}

	public ArrayList<GLevelItem> getNewItems() {
		return newItems;
	}

	public void setNewItems(ArrayList<GLevelItem> newItems) {
		this.newItems = newItems;
	}

	public Player getPlayer() {
		return p;
	}

	public GLevel getLevel() {
		return level;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;

	}

}
