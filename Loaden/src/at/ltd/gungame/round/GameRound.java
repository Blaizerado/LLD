package at.ltd.gungame.round;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.utils.visual.Title;
import at.ltd.gungame.GunGame;
import at.ltd.gungame.events.custom.EventGameRoundStart;
import at.ltd.gungame.events.custom.EventGameRoundStop;
import at.ltd.gungame.events.custom.EventGameTick;
import at.ltd.gungame.maps.maputils.GameMap;

/**
 * Old version: http://haste.ltd-net.eu/qipip.java
 * 
 * @author NyroForce
 * @since 03.01.2018
 * @version 1.0.2
 */
public class GameRound {

	/**
	 * ROUND TIME IN MIN
	 */
	private static final int ROUND_TIME = 15;

	/*
	 * DATA
	 */
	private List<GamePlayer> PLAYER_LIST = new ArrayList<>();
	private GameMap CURRENT_GAME_MAP = null;
	private int SEC_LEFT = ROUND_TIME * 60;
	private int MIN_LEFT = ROUND_TIME;
	private boolean RUNNING = false;
	private GameRound INSTANCE = this;

	/*
	 * SCHEDULER IDs
	 */
	private int SCHEDULER_TICK_ID_1 = -123;
	private int SCHEDULER_TICK_ID_2 = -123;

	public synchronized void start(GameMap gameMap) {
		if (RUNNING) {
			throw new IllegalAccessError("Round is already running!");
		}
		// CHANCHING STATUS
		RUNNING = true;
		// SETTING DATA
		CURRENT_GAME_MAP = gameMap;
		GunGame.setRoundStatus(true);
		// LOADING MAP
		gameMap.onLoad();
		// CALLING EVENT
		EventGameRoundStart egrs = new EventGameRoundStart(this);
		Main.callEvent(egrs);
		// STARTING SCHEDULERS
		gameTickScheduler();

	}

	public synchronized void stop() {
		if (!RUNNING) {
			throw new IllegalAccessError("Round is already stopped!");
		}
		RUNNING = false;
		cancelSchedulers();

		GunGame.setRoundStatus(false);
		CURRENT_GAME_MAP.onUnload();

		EventGameRoundStop egrs = new EventGameRoundStop(INSTANCE);
		Main.callEvent(egrs);

		synchronized (PLAYER_LIST) {
			getPlayersInRound().forEach(ggp -> ggp.exitGunGame(true));
		}
	}

	private void gameTickScheduler() {
		SCHEDULER_TICK_ID_1 = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), () -> {
			SEC_LEFT = SEC_LEFT - 1;
			EventGameTick evt = new EventGameTick(SEC_LEFT, INSTANCE, getRoundTimeMin(), (getRoundTimeMin() * 60));
			Main.callEvent(evt);
		}, 20, 20);

		SCHEDULER_TICK_ID_2 = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), () -> {
			MIN_LEFT = MIN_LEFT - 1;
			if (MIN_LEFT == 2 || MIN_LEFT == 3 || MIN_LEFT == 5 || MIN_LEFT == 4) {
				synchronized (PLAYER_LIST) {
					PLAYER_LIST.forEach(ggp -> Title.sendFullTitle(ggp.getPlayer(), 10, 60, 10, "", Cf.rs(Cf.ROUND_END_1_TITLE, ggp.getPlayer(), "[TIME]", MIN_LEFT)));
				}

			} else if (MIN_LEFT == 1) {
				synchronized (PLAYER_LIST) {
					PLAYER_LIST.forEach(ggp -> Title.sendFullTitle(ggp.getPlayer(), 10, 60, 10, "", Cf.rs(Cf.ROUND_END_2_TITLE, ggp.getPlayer(), "[TIME]", MIN_LEFT)));
				}
			}

			if (MIN_LEFT == 0) {
				stop();
			}

		}, 1200, 1200);

	}

	public void cancelSchedulers() {
		Bukkit.getScheduler().cancelTask(SCHEDULER_TICK_ID_1);
		Bukkit.getScheduler().cancelTask(SCHEDULER_TICK_ID_2);
	}

	public void addPlayer(GamePlayer ggp) {
		synchronized (PLAYER_LIST) {
			if (!PLAYER_LIST.contains(ggp)) {
				PLAYER_LIST.add(ggp);
			}
		}
	}

	public void removePlayer(GamePlayer ggp) {
		synchronized (PLAYER_LIST) {
			if (PLAYER_LIST.contains(ggp)) {
				PLAYER_LIST.remove(ggp);
			}
		}
	}

	public List<GamePlayer> getPlayersInRound() {
		List<GamePlayer> NEW_PLAYER_LIST = new ArrayList<>();
		synchronized (PLAYER_LIST) {
			PLAYER_LIST.forEach(p -> NEW_PLAYER_LIST.add(p));
		}
		return NEW_PLAYER_LIST;
	}

	public void broadcastMessage(String msg) {
		synchronized (PLAYER_LIST) {
			PLAYER_LIST.forEach(p -> p.sendMessage(msg));
		}
	}

	public boolean isPlayerInRound(GamePlayer ggp) {
		synchronized (PLAYER_LIST) {
			return PLAYER_LIST.contains(ggp);
		}
	}

	public boolean isPlayerInRound(Player player) {
		synchronized (PLAYER_LIST) {
			return PLAYER_LIST.contains(GamePlayer.getGamePlayer(player));
		}
	}

	public int getRoundTimeMin() {
		return ROUND_TIME;
	}

	public int getRoundTimeLeftMin() {
		return MIN_LEFT;
	}

	public int getRoundTimeLeftSec() {
		return SEC_LEFT;
	}

	public GameMap getGameMap() {
		return CURRENT_GAME_MAP;
	}

}
