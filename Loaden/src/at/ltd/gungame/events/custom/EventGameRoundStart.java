package at.ltd.gungame.events.custom;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import at.ltd.adds.game.player.GamePlayer;
import at.ltd.gungame.maps.maputils.GameMap;
import at.ltd.gungame.round.GameRound;

public class EventGameRoundStart extends Event {

	private GameRound gameRound;
	private static final HandlerList handlers = new HandlerList();

	public EventGameRoundStart(GameRound gameRound) {
		this.gameRound = gameRound;
	}

	public List<GamePlayer> getGunGamePlayers() {
		return gameRound.getPlayersInRound();
	}

	public ArrayList<Player> getPlayers() {
		ArrayList<Player> ar = new ArrayList<>();
		for (GamePlayer ggp : gameRound.getPlayersInRound()) {
			ar.add(ggp.getPlayer());
		}
		return ar;
	}
	
	public GameMap getGameMap(){
		return gameRound.getGameMap();
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;

	}



}
