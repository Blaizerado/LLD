package at.ltd.adds.sql;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SQLPlayer {

	// DBVALUES
	private volatile String UUID;
	private volatile String NAME;
	private volatile int COIN;
	private volatile int PLAYTIME;
	private volatile Date LASTJOIN;
	private volatile Date FIRSTJOIN;
	private volatile String IP;
	private volatile int KILLS;
	private volatile int DEATHS;
	private volatile int ROUNDPLAYED;
	private volatile String RANK;
	private volatile float COINMULTIPLIER;
	private volatile boolean MUTED;
	private volatile boolean BANNED;
	private volatile long TIMEBANTIME;
	private volatile Timestamp LASTUPDATE;
	private volatile int SKILLPOINT;

	// VALUES
	private String MCUUID;
	private SQLConnectionHandler connectionHandler;

	public SQLPlayer(String UUID, SQLConnectionHandler connectionHandler) {
		synchronized (this) {
			this.MCUUID = UUID;
			this.connectionHandler = connectionHandler;
		}
	}

	public Boolean load() throws ClassNotFoundException, SQLException {
		SQLQuery query = new SQLQuery(connectionHandler);
		ResultSet rs = query.querySQL("SELECT * FROM `MC` WHERE `UUID`='" + MCUUID + "'");
		if (!rs.next()) {
			return false;
		}
		setUUID(rs.getString("UUID"));
		setName(rs.getString("NAME"));
		setCoins(rs.getInt("COIN"));
		setPlaytime(rs.getInt("PLAYTIME"));
		setLastjoin(rs.getDate("LASTJOIN"));
		setFirstjoin(rs.getDate("FIRSTJOIN"));
		setIP(rs.getString("IP"));
		setKills(rs.getInt("KILLS"));
		setDeaths(rs.getInt("DEATHS"));
		setRoundsPlayed(rs.getInt("ROUNDPLAYED"));
		setRank(rs.getString("RANK"));
		setCoinmultiplier(rs.getFloat("COINMULTIPLIER"));
		setMuted(rs.getBoolean("MUTED"));
		setBanned(rs.getBoolean("BANNED"));
		setTimebantime(rs.getLong("TIMEBANTIME"));
		setLastupdate(rs.getTimestamp("LASTUPDATE"));
		setSkillPoints(rs.getInt("SKILLPOINT"));
		return true;
	}

	public void save() throws SQLException {
		SQLQuery query = new SQLQuery(connectionHandler);
		query.executePreparedStatement(new SQLPreparedStatement() {

			@Override
			public void execution(PreparedStatement prepareStatement) {
				try {
					prepareStatement.setString(1, getUUID());
					prepareStatement.setString(2, getName());
					prepareStatement.setInt(3, getCoins());
					prepareStatement.setInt(4, getPlaytime());
					prepareStatement.setDate(5, getLastjoin());
					prepareStatement.setDate(6, getFirstjoin());
					prepareStatement.setString(7, getIP());
					prepareStatement.setInt(8, getKills());
					prepareStatement.setInt(9, getDeaths());
					prepareStatement.setInt(10, getRoundsPlayed());
					prepareStatement.setString(11, getRank());
					prepareStatement.setFloat(12, getCoinmultiplier());
					prepareStatement.setBoolean(13, isMuted());
					prepareStatement.setBoolean(14, isBanned());
					prepareStatement.setLong(15, getTimebantime());
					prepareStatement.setTimestamp(16, getLastupdate());
					prepareStatement.setInt(17, getSkillPoints());
					prepareStatement.executeUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}, "UPDATE `MC` SET `UUID` = ?, `NAME` = ?, `COIN` = ?, `PLAYTIME` = ?, `LASTJOIN` = ?, `FIRSTJOIN` = ?, `IP` = ?, `KILLS` = ?, `DEATHS` = ?, `ROUNDPLAYED` = ?, `RANK` = ?, `COINMULTIPLIER` = ?, `MUTED` = ?, `BANNED` = ?, `TIMEBANTIME` = ?, `LASTUPDATE` = ?, `SKILLPOINT` = ? WHERE `MC`.`UUID` = '" + MCUUID + "'");
	}

	public String getUUID() {
		synchronized (this) {
			return UUID;
		}

	}

	public void setUUID(String uuid) {
		synchronized (this) {
			UUID = uuid;
		}

	}

	public String getName() {
		synchronized (this) {
			return NAME;
		}

	}

	public void setName(String name) {
		synchronized (this) {
			NAME = name;
		}

	}

	public int getCoins() {
		synchronized (this) {
			return COIN;
		}

	}

	public void setCoins(int coins) {
		synchronized (this) {
			COIN = coins;
		}

	}

	public int getPlaytime() {
		synchronized (this) {
			return PLAYTIME;
		}

	}

	public void setPlaytime(int playtime) {
		synchronized (this) {
			PLAYTIME = playtime;
		}

	}

	public Date getLastjoin() {
		synchronized (this) {
			return LASTJOIN;
		}

	}

	public void setLastjoin(Date lastjoin) {
		synchronized (this) {
			LASTJOIN = lastjoin;
		}

	}

	public Date getFirstjoin() {
		synchronized (this) {
			return FIRSTJOIN;
		}

	}

	public void setFirstjoin(Date firstjoin) {
		synchronized (this) {
			FIRSTJOIN = firstjoin;
		}

	}

	public String getIP() {
		synchronized (this) {
			return IP;
		}

	}

	public void setIP(String iP) {
		synchronized (this) {
			IP = iP;
		}

	}

	public int getKills() {
		synchronized (this) {
			return KILLS;
		}

	}

	public void setKills(int kILLS) {
		synchronized (this) {
			KILLS = kILLS;
		}

	}

	public int getDeaths() {
		synchronized (this) {
			return DEATHS;
		}

	}

	public void setDeaths(int dEATHS) {
		synchronized (this) {
			DEATHS = dEATHS;
		}

	}

	public int getRoundsPlayed() {
		synchronized (this) {
			return ROUNDPLAYED;
		}

	}

	public void setSkillPoints(int points) {
		synchronized (this) {
			SKILLPOINT = points;
		}

	}

	public int getSkillPoints() {
		synchronized (this) {
			return SKILLPOINT;
		}

	}

	public void setRoundsPlayed(int roundsPlayed) {
		synchronized (this) {
			ROUNDPLAYED = roundsPlayed;
		}

	}

	public String getRank() {
		synchronized (this) {
			return RANK;
		}

	}

	public void setRank(String rank) {
		synchronized (this) {
			RANK = rank;
		}

	}

	public float getCoinmultiplier() {
		synchronized (this) {
			return COINMULTIPLIER;
		}

	}

	public void setCoinmultiplier(float coinmultiplier) {
		synchronized (this) {
			COINMULTIPLIER = coinmultiplier;
		}

	}

	public boolean isMuted() {
		synchronized (this) {
			return MUTED;
		}

	}

	public void setMuted(boolean muted) {
		synchronized (this) {
			MUTED = muted;
		}

	}

	public boolean isBanned() {
		synchronized (this) {
			return BANNED;
		}

	}

	public void setBanned(boolean banned) {
		synchronized (this) {
			BANNED = banned;
		}

	}

	public long getTimebantime() {
		synchronized (this) {
			return TIMEBANTIME;
		}

	}

	public void setTimebantime(long timebantime) {
		synchronized (this) {
			TIMEBANTIME = timebantime;
		}

	}

	public Timestamp getLastupdate() {
		synchronized (this) {
			return LASTUPDATE;
		}

	}

	public void setLastupdate(Timestamp lastupdate) {
		synchronized (this) {
			LASTUPDATE = lastupdate;
		}

	}

}
