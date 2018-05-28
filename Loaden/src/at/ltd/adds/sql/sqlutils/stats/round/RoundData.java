package at.ltd.adds.sql.sqlutils.stats.round;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.entity.Player;

import at.ltd.Main;
import at.ltd.adds.sql.SQLQuery;

public class RoundData {

	private int roundID;
	private String mapName;
	private int playerCount = 0;
	private int coinsCashOut = 0;
	private int kills = 0;
	private int shoots = 0;
	private long roundend;
	private ArrayList<String> players = new ArrayList<>();

	public RoundData(int roundID, String mapName) {
		this.roundID = roundID;
		this.mapName = mapName;
	}

	public RoundData(int roundID) throws ClassNotFoundException, SQLException {
		SQLQuery query = Main.getSQLQuery();
		ResultSet rs = query.querySQL("SELECT * FROM `ROUND_STATS` WHERE `ROUNDID` = " + roundID);
		rs.next();
		this.roundID = roundID;
		this.mapName = rs.getString("MAPNAME");
		this.playerCount = rs.getInt("PLAYERCOUNT");
		this.coinsCashOut = rs.getInt("COINS");
		this.kills = rs.getInt("KILLS");
		this.shoots = rs.getInt("SHOOTS");
		this.roundend = rs.getLong("ROUNDEND");
		this.players = (ArrayList<String>) Arrays.asList(rs.getString("PLAYERS").split(","));
	}

	public void saveToDB() {
		SQLQuery query = Main.getSQLQuery();
		try {
			query.updateSQL("INSERT INTO `ROUND_STATS` (`ROUNDID`, `MAPNAME`, `PLAYERCOUNT`, `COINS`, `KILLS`, `SHOOTS`, `PLAYERS`, `ROUNDEND`) VALUES ('" + roundID + "', '" + mapName + "', '" + playerCount + "', '" + coinsCashOut + "', '" + kills + "', '" + shoots + "', '" + playersToString() + "', '" + roundend + "');");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String playersToString() {
		String[] array = players.toArray(new String[0]);
		return String.join(",", array);
	}

	public synchronized void addPlayer(Player player) {
		String s = player.getUniqueId().toString() + "*" + player.getName();
		if (!players.contains(s)) {
			players.add(s);
		}
	}

	public synchronized long getRoundend() {
		return roundend;
	}

	public synchronized void setRoundend(long roundend) {
		this.roundend = roundend;
	}

	public synchronized int getRoundID() {
		return roundID;
	}

	public synchronized void setRoundID(int roundID) {
		this.roundID = roundID;
	}

	public synchronized String getMapName() {
		return mapName;
	}

	public synchronized void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public synchronized int getPlayerCount() {
		return playerCount;
	}

	public synchronized void setPlayerCount(int playerCount) {
		this.playerCount = playerCount;
	}

	public synchronized int getCoinsCashOut() {
		return coinsCashOut;
	}

	public synchronized void setCoinsCashOut(int coinsCashOut) {
		this.coinsCashOut = coinsCashOut;
	}

	public synchronized int getKills() {
		return kills;
	}

	public synchronized void setKills(int kills) {
		this.kills = kills;
	}

	public synchronized int getShoots() {
		return shoots;
	}

	public synchronized void setShoots(int shoots) {
		this.shoots = shoots;
	}

	public synchronized ArrayList<String> getPlayers() {
		return players;
	}

	public synchronized void setPlayers(ArrayList<String> players) {
		this.players = players;
	}

}
