package at.ltd.gungame.round.vote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.utils.ScoreBoardManager;
import at.ltd.adds.utils.config.Config;
import at.ltd.adds.utils.config.ConfigAble;
import at.ltd.gungame.events.custom.EventGameRoundStart;
import at.ltd.gungame.events.custom.EventVoteStart;
import at.ltd.gungame.maps.maputils.GameMap;

public class VoteScoreBoard implements Listener {

	public static boolean voting = false;
	public static int updaterID = 0;
	public static LinkedHashMap<GameMap, Integer> list;

	public static String[] names;
	@ConfigAble(key = "", value = "&6 ➠ &7")
	public static String prefix;

	@ConfigAble(key = "", value = "&e&l&nVotes,&6&l&nV&e&l&notes,&e&l&nV&6&l&no&e&l&ntes,&e&l&nVo&6&l&nt&e&l&nes,&e&l&nVot&6&l&ne&e&l&ns,&e&l&nVote&6&l&ns,&e&l&nVotes,&e&l&nVotes")
	public static String animation;

	public static int pos = 0;

	public static void init() {
		Config.loadMyClass("VoteScoreboardAnimation", VoteScoreBoard.class);
		names = Cf.replaceColorCodes(animation).split(",");
		prefix = Cf.replaceColorCodes(prefix);
		Main.registerListener(new VoteScoreBoard());
	}

	@EventHandler
	public void on(EventVoteStart e) {
		voting = true;
		list = sort(RoundVote.getVotes(), 6);
		pos = 0;

		Scoreboard sb = ScoreBoardManager.getScoreBoard();
		Objective obj = sb.registerNewObjective("VOTE", "NJOO");

		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(names[0]);
		for (GameMap gm : list.keySet()) {
			int votes = list.get(gm);
			obj.getScore(getName(gm)).setScore(votes);
		}

		updaterID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {

			@Override
			public void run() {
				if (voting) {
					if (pos == names.length - 1) {
						pos = 0;
					}
					String name = names[pos];
					Scoreboard sb = ScoreBoardManager.getScoreBoard();

					LinkedHashMap<GameMap, Integer> newlist = sort(RoundVote.getVotes(), 6);
					ArrayList<GameMap> ar = new ArrayList<>();

					for (GameMap gm : list.keySet()) {
						if (!newlist.containsKey(gm)) {
							ar.add(gm);
						}
					}
					list = newlist;
					Objective obj = sb.getObjective("VOTE");
					for (GameMap gm : ar) {
						obj.getScoreboard().resetScores(getName(gm));
					}

					obj.setDisplayName(name);
					for (GameMap gm : list.keySet()) {
						int votes = list.get(gm);
						obj.getScore(getName(gm)).setScore(votes);
					}
					pos++;

				}
			}
		}, 5, 10);

	}

	public static String getName(GameMap gm) {
		String name = gm.getMapName();
		name = ChatColor.stripColor(name);
		name = prefix + name;
		return name;
	}

	@EventHandler
	public void on(EventGameRoundStart e) {
		Bukkit.getScheduler().cancelTask(updaterID);
		Scoreboard sb = ScoreBoardManager.getScoreBoard();
		sb.getObjective("VOTE").unregister();
		voting = false;
		list.clear();
		list = null;
	}

	@SuppressWarnings("unchecked")
	public static LinkedHashMap<GameMap, Integer> sort(HashMap<GameMap, Integer> tosort, int count) {
		Object[] a = tosort.entrySet().toArray();
		Arrays.sort(a, new Comparator<Object>() {
			@SuppressWarnings("unchecked")
			public int compare(Object o1, Object o2) {
				return ((Map.Entry<GameMap, Integer>) o2).getValue()
						.compareTo(((Map.Entry<GameMap, Integer>) o1).getValue());
			}
		});
		LinkedHashMap<GameMap, Integer> sorted = new LinkedHashMap<>();
		int i = 0;
		for (Object e : a) {
			if (i == count) {
				break;
			}
			GameMap key = ((Map.Entry<GameMap, Integer>) e).getKey();
			Integer value = ((Map.Entry<GameMap, Integer>) e).getValue();
			sorted.put(key, value);
			i++;
		}
		return sorted;
	}

}
