package at.ltd.lobby.ranking.stats;

public class PlayerStats {

	public String uuid;
	public int kills;
	public int deaths;
	public String name;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUUID() {
		return uuid;
	}
	public void setUUID(String uuid) {
		this.uuid = uuid;
	}
	public int getKills() {
		return kills;
	}
	public void setKills(int kills) {
		this.kills = kills;
	}
	public int getDeaths() {
		return deaths;
	}
	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public String toString() {
		return "Name: " + name + ", Kills: " + kills;
	}

}
