package at.ltd.events.custom;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import at.ltd.adds.game.player.GamePlayer;
import at.ltd.lobby.LobbyUtils;

public class EventTeleportSpawn extends Event {
	private Player player;
	private GamePlayer ggp;
	private Boolean cancelled = false;
	private static final HandlerList handlers = new HandlerList();

	public EventTeleportSpawn(Player p, GamePlayer ggp) {
		this.player = p;
		this.ggp = ggp;
		EventTeleport et = new EventTeleport(p, LobbyUtils.Lobby_Spawn_Location, "SPAWN");
		Bukkit.getPluginManager().callEvent(et);
		cancelled = et.isCancelled();
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

}
