package at.ltd.gungame.tablist;

import org.bukkit.scoreboard.Team;

import at.ltd.gungame.ranks.GRankPerm;

public class TabTeam {
	public TabTeam(String name, Team team, GRankPerm prem, String prefix) {
		this.name = name;
		this.team = team;
		this.rank = prem;
		this.prefix = prefix;
	}
	private String name;
	private Team team;
	private GRankPerm rank;
	private String prefix;

	public String getName() {
		return name;
	}
	public Team getTeam() {
		return team;
	}
	public GRankPerm getRank() {
		return rank;
	}
	public String getPrefix() {
		return prefix;
	}

}
