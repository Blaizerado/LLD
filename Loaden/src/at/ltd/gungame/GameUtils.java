package at.ltd.gungame;

import org.bukkit.entity.Player;

import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.sql.sqlutils.SQLCollection;
import at.ltd.gungame.maps.maputils.GameMap;
import at.ltd.gungame.round.vote.RoundVote;

public class GameUtils {

	public static GamePlayer getGP(Player p) {
		return GamePlayer.getGamePlayer(p);
	}

	public static GameMap getMap() {
		GameMap gm = null;
		if (getServerStatus() == GameStatus.MATCH) {
			gm = GunGame.getRoundManager().getGameMap();
		}
		return gm;
	}

	public static boolean isInMatch(Player p) {
		return getGP(p).isInRound();
	}

	public static int getCoins(Player p) {

		return SQLCollection.getPlayer(p).getCoins();
	}

	public static enum GameStatus {
		NOTHING, MATCH, VOTE

	};
	public static GameStatus getServerStatus() {
		GameStatus gm = null;
		if (RoundVote.isVoting) {
			gm = GameStatus.VOTE;
		}
		if (GunGame.isRound()) {
			gm = GameStatus.MATCH;
		} else {
			if (gm == null) {
				gm = GameStatus.NOTHING;
			}
		}
		return gm;

	}

	public static enum PlayerStatus {
		SHOP, MATCH, VOTE, NOTHING
	};
	public static PlayerStatus getPlayerStatus(Player p) {
		PlayerStatus gm = null;
		synchronized (RoundVote.playerUUIDs) {
			if (RoundVote.playerUUIDs.containsKey(p.getUniqueId().toString())) {
				gm = PlayerStatus.VOTE;
			}
		}
		if (getGP(p).isInRound()) {
			gm = PlayerStatus.MATCH;
		} else {
			if (gm == null) {
				gm = PlayerStatus.NOTHING;
			}
		}
		return gm;

	}

	public static String getPlayerStatusBar(Player p) {
		String status = null;
		PlayerStatus ps = getPlayerStatus(p);
		if (ps == PlayerStatus.NOTHING) {
			status = "§4┃";
		}
		if (ps == PlayerStatus.VOTE) {
			status = "§e┃";
		}
		if (ps == PlayerStatus.MATCH) {
			status = "§a┃";
		}
		if (ps == PlayerStatus.SHOP) {
			status = "§6┃";
		}
		return status;
	}

	public static String getPlayerStatusBar(PlayerStatus ps) {
		String status = null;
		if (ps == PlayerStatus.NOTHING) {
			status = "§4┃";
		}
		if (ps == PlayerStatus.VOTE) {
			status = "§e┃";
		}
		if (ps == PlayerStatus.MATCH) {
			status = "§a┃";
		}
		if (ps == PlayerStatus.SHOP) {
			status = "§6┃";
		}
		return status;
	}

}
