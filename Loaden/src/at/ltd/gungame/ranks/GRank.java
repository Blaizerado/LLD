package at.ltd.gungame.ranks;

import java.util.ArrayList;

public class GRank {
	public static ArrayList<GRank> ranks = new ArrayList<>();
	public static int[] sorts = null;
	public String name;
	public Integer kills;
	public String symbol;

	public GRank(String name, Integer kills) {
		this.name = name;
		this.kills = kills;
	}

	public static ArrayList<GRank> getRanks() {
		return ranks;
	}

	public static void setRanks(ArrayList<GRank> ranks) {
		GRank.ranks = ranks;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getKills() {
		return kills;
	}

	public void setKills(Integer kills) {
		this.kills = kills;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	

}
