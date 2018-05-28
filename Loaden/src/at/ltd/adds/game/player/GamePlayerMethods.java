package at.ltd.adds.game.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * All methods are Threadsafe.
 * 
 * @author NyroForce
 * @since 06.12.2017
 * @version 1.0.1
 */
public class GamePlayerMethods {

	private final static HashMap<Player, GamePlayer> GAME_PLAYER_LIST = new HashMap<>();
	private final static ReentrantLock G_HM_LOCK = new ReentrantLock();

	/**
	 * Method is thread safe.
	 * 
	 * @param uuid
	 * @param ggp
	 * @return
	 */
	public static GamePlayer addGamePlayer(Player player, GamePlayer ggp) {
		G_HM_LOCK.lock();
		try {
			GAME_PLAYER_LIST.put(player, ggp);
			return ggp;
		} finally {
			G_HM_LOCK.unlock();
		}
	}

	/**
	 * Method is thread safe.
	 * 
	 * @param uuid
	 * @return
	 */
	public static GamePlayer getGamePlayer(String uuid) {
		G_HM_LOCK.lock();
		try {
			return GAME_PLAYER_LIST.get(Bukkit.getPlayer(uuid));
		} finally {
			G_HM_LOCK.unlock();
		}
	}
	/**
	 * Method is thread safe.
	 * 
	 * @param p
	 * @return
	 */
	public static GamePlayer getGamePlayer(Player p) {
		G_HM_LOCK.lock();
		try {
			return GAME_PLAYER_LIST.get(p);
		} finally {
			G_HM_LOCK.unlock();
		}
	}
	/**
	 * Method is thread safe.
	 * 
	 * @param p
	 */
	public static void removeGamePlayer(Player p) {
		G_HM_LOCK.lock();
		try {
			GAME_PLAYER_LIST.remove(p);
		} finally {
			G_HM_LOCK.unlock();
		}
	}
	/**
	 * Method is thread safe.
	 * 
	 * @param uuid
	 */
	public static void removeGamePlayer(String uuid) {
		G_HM_LOCK.lock();
		try {
			GAME_PLAYER_LIST.remove(Bukkit.getPlayer(uuid));
		} finally {
			G_HM_LOCK.unlock();
		}
	}
	/**
	 * Method is thread safe.
	 * 
	 * @return
	 */
	public static HashMap<Player, GamePlayer> getGamePlayers() {
		HashMap<Player, GamePlayer> hm = new HashMap<>();
		G_HM_LOCK.lock();
		try {
			for (Player p : GAME_PLAYER_LIST.keySet()) {
				hm.put(p, GAME_PLAYER_LIST.get(p));
			}
		} finally {
			G_HM_LOCK.unlock();
		}
		return hm;
	}
	/**
	 * Method is thread safe.
	 * 
	 * @param p
	 * @return
	 */
	public static boolean isInRound(Player p) {
		G_HM_LOCK.lock();
		try {
			return GAME_PLAYER_LIST.get(p).isInRound();
		} finally {
			G_HM_LOCK.unlock();
		}
	}
	
	/**
	 * Method is thread safe.
	 * 
	 * @param p
	 * @return
	 */
	public static boolean isDead(Player p) {
		G_HM_LOCK.lock();
		try {
			return GAME_PLAYER_LIST.get(p).isDead();
		} finally {
			G_HM_LOCK.unlock();
		}
	}

	/**
	 * Method is thread safe.
	 * 
	 * @param p
	 * @return
	 */
	public static List<GamePlayer> getGunGamePlayersInRound() {
		ArrayList<GamePlayer> list = new ArrayList<>();
		G_HM_LOCK.lock();
		try {
			for (GamePlayer ggp : GAME_PLAYER_LIST.values()) {
				if (ggp.isInRound()) {
					list.add(ggp);
				}
			}
		} finally {
			G_HM_LOCK.unlock();
		}
		return list;
	}

}
