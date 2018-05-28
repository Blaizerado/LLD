package at.ltd.gungame.ranks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;

import at.ltd.adds.utils.config.ConfigManager;

public class GRankPermission {
	public static ArrayList<GRankPermission> perms = new ArrayList<>();
	public GRankPermission(String name, String format, GRankPerm gRankPerm) {
		this.name = name;
		this.format = format;
		this.gRankPerm = gRankPerm;
		perms.add(this);
	}

	public String name;
	public String format;
	public GRankPerm gRankPerm;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public GRankPerm getgRankPerm() {
		return gRankPerm;
	}

	public void setgRankPerm(GRankPerm gRankPerm) {
		this.gRankPerm = gRankPerm;
	}

	public static void read() throws IOException {
		String folder = "plugins/LTD/Ränge.yml";
		perms.clear();
		HashMap<String, String> config = ConfigManager.readConfig(folder);
		for (String s : config.keySet()) {
			String form = config.get(s);
			GRankPerm gRankPerm = GRankPerm.getRank(s);
			perms.add(new GRankPermission(s, ChatColor.translateAlternateColorCodes('&', form), gRankPerm));
		}

	}

	public static GRankPermission get(GRankPerm g) {
		GRankPermission gRankPermission = null;
		for (GRankPermission grp : GRankPermission.perms) {
			if (grp.getgRankPerm().getPerm().equals(g.getPerm())) {
				gRankPermission = grp;
			}
		}
		return gRankPermission;
	}

}
