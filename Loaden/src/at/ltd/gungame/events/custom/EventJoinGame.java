package at.ltd.gungame.events.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import at.ltd.adds.game.player.GamePlayer;
import at.ltd.gungame.GunGame;
import at.ltd.gungame.maps.maputils.GameMap;

public class EventJoinGame extends Event {
	private Player player;
	private GamePlayer ggp;
	private Boolean cancelled = false;
	private GameMap gameMap;
	private static final HandlerList handlers = new HandlerList();

	public EventJoinGame(Player p, GamePlayer ggp) {
		this.player = p;
		this.ggp = ggp;

		GameMap gm = null;
		if (GunGame.getRoundManager() != null) {
			gm = GunGame.getRoundManager().getGameMap();
		}
		this.gameMap = gm;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public Player getPlayer() {
		return player;
	}

	public GameMap getGameMap() {
		return gameMap;
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

}
