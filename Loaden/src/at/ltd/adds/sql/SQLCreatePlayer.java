package at.ltd.adds.sql;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

public class SQLCreatePlayer {

	public SQLCreatePlayer(SQLConnectionHandler connectionHandler) {
		this.connectionHandler = connectionHandler;
		Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		LASTJOIN = date;
		FIRSTJOIN = date;
	}

	// DBVALUES
	private String UUID;
	private String NAME;
	private int COIN = 0;
	private int PLAYTIME = 0;
	private Date LASTJOIN;
	private Date FIRSTJOIN;
	private String IP;
	private int KILLS = 0;
	private int DEATHS = 0;
	private int ROUNDPLAYED = 0;
	private String RANK;
	private float COINMULTIPLIER = (float) 0.00;
	private boolean MUTED = false;
	private boolean BANNED = false;
	private long TIMEBANTIME = 0;
	private Timestamp LASTUPDATE;

	private SQLConnectionHandler connectionHandler;

	public void createPlayer() throws SQLException {
		SQLQuery query = new SQLQuery(connectionHandler);
		query.executePreparedStatement(new SQLPreparedStatement() {

			@Override
			public void execution(PreparedStatement prepareStatement) {
				try {
					prepareStatement.setString(1, UUID);
					prepareStatement.setString(2, NAME);
					prepareStatement.setInt(3, COIN);
					prepareStatement.setInt(4, PLAYTIME);
					prepareStatement.setDate(5, LASTJOIN);
					prepareStatement.setDate(6, FIRSTJOIN);
					prepareStatement.setString(7, IP);
					prepareStatement.setInt(8, KILLS);
					prepareStatement.setInt(9, DEATHS);
					prepareStatement.setInt(10, ROUNDPLAYED);
					prepareStatement.setString(11, RANK);
					prepareStatement.setFloat(12, COINMULTIPLIER);
					prepareStatement.setBoolean(13, MUTED);
					prepareStatement.setBoolean(14, BANNED);
					prepareStatement.setLong(15, TIMEBANTIME);
					prepareStatement.setTimestamp(16, LASTUPDATE);
					prepareStatement.executeUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}, "INSERT INTO `MC` (`UUID`, `NAME`, `COIN`, `PLAYTIME`, `LASTJOIN`, `FIRSTJOIN`, `IP`, `KILLS`, `DEATHS`, `ROUNDPLAYED`, `RANK`, `COINMULTIPLIER`, `MUTED`, `BANNED`, `TIMEBANTIME`, `LASTUPDATE`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

	}

	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public int getCOIN() {
		return COIN;
	}
	public void setCOIN(int cOIN) {
		COIN = cOIN;
	}
	public int getPLAYTIME() {
		return PLAYTIME;
	}
	public void setPLAYTIME(int pLAYTIME) {
		PLAYTIME = pLAYTIME;
	}
	public Date getLASTJOIN() {
		return LASTJOIN;
	}
	public void setLASTJOIN(Date lASTJOIN) {
		LASTJOIN = lASTJOIN;
	}
	public Date getFIRSTJOIN() {
		return FIRSTJOIN;
	}
	public void setFIRSTJOIN(Date fIRSTJOIN) {
		FIRSTJOIN = fIRSTJOIN;
	}
	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
	}
	public int getKILLS() {
		return KILLS;
	}
	public void setKILLS(int kILLS) {
		KILLS = kILLS;
	}
	public int getDEATHS() {
		return DEATHS;
	}
	public void setDEATHS(int dEATHS) {
		DEATHS = dEATHS;
	}
	public int getROUNDPLAYED() {
		return ROUNDPLAYED;
	}
	public void setROUNDPLAYED(int rOUNDPLAYED) {
		ROUNDPLAYED = rOUNDPLAYED;
	}
	public String getRANK() {
		return RANK;
	}
	public void setRANK(String rANK) {
		RANK = rANK;
	}
	public float getCOINMULTIPLIER() {
		return COINMULTIPLIER;
	}
	public void setCOINMULTIPLIER(float cOINMULTIPLIER) {
		COINMULTIPLIER = cOINMULTIPLIER;
	}
	public boolean isMUTED() {
		return MUTED;
	}
	public void setMUTED(boolean mUTED) {
		MUTED = mUTED;
	}
	public boolean isBANNED() {
		return BANNED;
	}
	public void setBANNED(boolean bANNED) {
		BANNED = bANNED;
	}
	public long getTIMEBANTIME() {
		return TIMEBANTIME;
	}
	public void setTIMEBANTIME(long tIMEBANTIME) {
		TIMEBANTIME = tIMEBANTIME;
	}
	public Timestamp getLASTUPDATE() {
		return LASTUPDATE;
	}
	public void setLASTUPDATE(Timestamp lastupdate) {
		LASTUPDATE = lastupdate;
	}

}
