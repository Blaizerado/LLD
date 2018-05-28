package at.ltd.events.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import at.ltd.adds.sql.SQLPlayer;
import at.ltd.adds.sql.sqlutils.SQLCollection;

public class EventPlayerQuitAsync extends Event {

	private final static HandlerList handlers = new HandlerList();
	private Player player;
	private SQLPlayer sqlPlayer;
	public EventPlayerQuitAsync(Player player) {
		this.player = player;
		sqlPlayer = SQLCollection.getPlayer(player);
	}

	public Player getPlayer() {
		return player;
	}

	public SQLPlayer getSQLPlayer() {
		return sqlPlayer;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;

	}

}
