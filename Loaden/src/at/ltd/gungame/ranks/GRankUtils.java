package at.ltd.gungame.ranks;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import at.ltd.Main;
import at.ltd.adds.utils.config.ConfigManager;
import at.ltd.gungame.GameUtils;

public class GRankUtils {

	public static void init() throws Exception {
		GRank.ranks = readRanks();
		GRank.sorts = GRankUtils.getSortedKills();
		GRankPermission.read();
		Main.addReloadHandler(() -> {
			try {
				GRankUtils.init();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public static ArrayList<GRank> readRanks() throws IOException {
		File folder = new File("plugins/LTD/GRanks");
		GRank.ranks.clear();
		ArrayList<GRank> ranks = new ArrayList<>();
		for (File file : folder.listFiles()) {
			HashMap<String, String> config = ConfigManager.readConfig(file.getPath());
			String name = ChatColor.translateAlternateColorCodes('&', config.get("Name"));
			String kills = config.get("Kills");
			String symbol = config.get("Symbol");
			GRank gr = new GRank(name, Integer.valueOf(kills));
			gr.setSymbol(ChatColor.translateAlternateColorCodes('&', symbol));
			ranks.add(gr);
		}
		return ranks;

	}

	public static int[] getSortedKills() {
		ArrayList<Integer> ar = new ArrayList<>();
		for (GRank gr : GRank.ranks) {
			ar.add(gr.getKills());
		}
		Integer[] ints = ar.toArray(new Integer[0]);
		int[] intArray = Arrays.stream(ints).mapToInt(Integer::intValue).toArray();
		return bubblesort(intArray);

	}

	public static int getRankInt(int i) {
		int pos = 0;
		int MaxInteger = 0;
		for (int kills : GRank.sorts) {
			if (pos + 1 == GRank.sorts.length) {
				MaxInteger = GRank.sorts[GRank.sorts.length - 1];
				break;
			}
			int first = GRank.sorts[pos];
			int second = GRank.sorts[pos + 1];
			if ((second - 1) == i) {
				return first;
			}
			if (second == i) {
				return second;
			}
			if (isBetween(first, second, i)) {
				MaxInteger = first;
			} else {
				if (i < second) {
					break;
				}
			}
			pos++;
		}

		return MaxInteger;
	}

	public static int getRankSortPos(int Kills) {
		int pos = 0;
		for (int i : GRank.sorts) {
			if (i == Kills) {
				break;
			}
			pos++;
		}
		return pos;
	}

	public static GRank getRank(int i) {
		int RankInt = getRankInt(i);
		GRank gra = null;
		for (GRank g : GRank.ranks) {
			if (g.getKills().intValue() == RankInt) {
				gra = g;
				break;
			}
		}
		return gra;
	}

	public static boolean isBetween(int a, int b, int c) {
		return b > a ? c > a && c < b : c > b && c < a;
	}

	private static int[] bubblesort(int[] sort) {
		int temp;
		for (int i = 1; i < sort.length; i++) {
			for (int j = 0; j < sort.length - i; j++) {
				if (sort[j] > sort[j + 1]) {
					temp = sort[j];
					sort[j] = sort[j + 1];
					sort[j + 1] = temp;
				}

			}
		}
		return sort;
	}

	public static String getFormat(GRankPermission gRang, GRank gRank, String name, GRankPerm gRankPerm, Integer GGLevel, String msg, Player p) {

		String form = gRang.getFormat();

		form = form.replace("[GGrang]", gRank.getSymbol());
		form = form.replace("[Name]", name);
		form = form.replace("[GGLevel]", GGLevel.toString());
		form = form.replace("[MSG]", msg);
		if (form.contains("[STATUSBAR]")) {
			form = form.replace("[STATUSBAR]", GameUtils.getPlayerStatusBar(p));
		}

		return form;
	}

	public static Boolean containsKillNumber(Integer i) {
		Boolean bo = false;
		for (Integer ki : GRank.sorts) {
			if (ki.intValue() == i.intValue()) {
				bo = true;
				break;
			}
		}

		return bo;

	}

}
