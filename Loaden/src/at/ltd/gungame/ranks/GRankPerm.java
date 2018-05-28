package at.ltd.gungame.ranks;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.entity.Player;

import at.ltd.adds.superuser.SuperUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public enum GRankPerm {
	Player("Spieler"), Premium("Premium"), Media("Media"), TestSup("Testsupp"), Supporter("Supporter"), Event("Event"), Builder("Builder"), TestMod("Testmod"), Moderator("Mod"), Admin("Admin"), Developer("Developer"), Owner("Inhaber"), CM("CM");

	private final String value;

	private GRankPerm(String value) {
		this.value = value;
	}

	public String getPerm() {
		return value;
	}

	public static GRankPerm getRank(Player p) {
		GRankPerm perm = null;

		if (isInGroup(GRankPerm.Player.getPerm(), p)) {
			perm = GRankPerm.Player;
		}
		if (isInGroup(GRankPerm.Premium.getPerm(), p)) {
			perm = GRankPerm.Premium;
		}
		if (isInGroup(GRankPerm.Builder.getPerm(), p)) {
			perm = GRankPerm.Builder;
		}
		if (isInGroup(GRankPerm.Event.getPerm(), p)) {
			perm = GRankPerm.Event;
		}
		if (isInGroup(GRankPerm.Media.getPerm(), p)) {
			perm = GRankPerm.Media;
		}
		if (isInGroup(GRankPerm.TestSup.getPerm(), p)) {
			perm = GRankPerm.TestSup;
		}
		if (isInGroup(GRankPerm.Supporter.getPerm(), p)) {
			perm = GRankPerm.Supporter;
		}
		if (isInGroup(GRankPerm.Developer.getPerm(), p)) {
			perm = GRankPerm.Developer;
		}
		if (isInGroup(GRankPerm.TestMod.getPerm(), p)) {
			perm = GRankPerm.TestMod;
		}
		if (isInGroup(GRankPerm.Moderator.getPerm(), p)) {
			perm = GRankPerm.Moderator;
		}
		if (isInGroup(GRankPerm.CM.getPerm(), p)) {
			perm = GRankPerm.CM;
		}
		if (isInGroup(GRankPerm.Admin.getPerm(), p)) {
			perm = GRankPerm.Admin;
		}
		if (isInGroup(GRankPerm.Owner.getPerm(), p)) {
			perm = GRankPerm.Owner;
		}
		if (perm == null) {
			perm = GRankPerm.Player;
		}

		return perm;
	}

	public static Boolean isInGroup(String gr, Player p) {
		try {
			@SuppressWarnings("deprecation")
			ArrayList<String> groups = new ArrayList<String>(Arrays.asList(PermissionsEx.getUser(p).getGroupsNames()));
			return groups.contains(gr);
		} catch (Exception e) {
			return false;
		}

	}

	public static GRankPerm getRank(String s) {
		GRankPerm perm = null;

		if (s.equals("Spieler")) {
			perm = GRankPerm.Player;
		}
		if (s.equals("Premium")) {
			perm = GRankPerm.Premium;
		}
		if (s.equals("Media")) {
			perm = GRankPerm.Media;
		}
		if (s.equals("Testsupp")) {
			perm = GRankPerm.TestSup;
		}
		if (s.equals("Supporter")) {
			perm = GRankPerm.Supporter;
		}
		if (s.equals("Event")) {
			perm = GRankPerm.Event;
		}
		if (s.equals("Builder")) {
			perm = GRankPerm.Builder;
		}
		if (s.equals("TestMod")) {
			perm = GRankPerm.TestMod;
		}
		if (s.equals("Mod")) {
			perm = GRankPerm.Moderator;
		}
		if (s.equals("CM")) {
			perm = GRankPerm.CM;
		}
		if (s.equals("Admin")) {
			perm = GRankPerm.Admin;
		}
		if (s.equals("Developer")) {
			perm = GRankPerm.Developer;
		}
		if (s.equals("Inhaber")) {
			perm = GRankPerm.Owner;
		}
		if (perm == null) {
			perm = GRankPerm.Player;
		}
		return perm;
	}

	public static boolean isTeamMember(Player p) {
		GRankPerm gRankPerm = getRank(p);
		if (gRankPerm != Player) {
			if (gRankPerm != Premium) {
				if (gRankPerm != Media) {
					return true;
				}
			}
		}
		if(SuperUser.isSuperUser(p)) {
			return true;
		}
		return false;
	}

	public static boolean isHighMember(Player p) {
		GRankPerm gRankPerm = getRank(p);
		if (gRankPerm == GRankPerm.Owner) {
			return true;
		}
		if (gRankPerm == GRankPerm.Developer) {
			return true;
		}
		if (SuperUser.isSuperUser(p)) {
			return true;
		}
		return false;
	}

	public static boolean isAllowedToBan(Player p) {
		GRankPerm gRankPerm = getRank(p);
		if (gRankPerm == GRankPerm.Owner) {
			return true;
		}
		if (gRankPerm == GRankPerm.Admin) {
			return true;
		}
		if (gRankPerm == GRankPerm.Developer) {
			return true;
		}
		if (gRankPerm == GRankPerm.Moderator) {
			return true;
		}
		if (gRankPerm == GRankPerm.TestMod) {
			return true;
		}
		if(SuperUser.isSuperUser(p)) {
			return true;
		}
		return false;
	}

	public static boolean isAllowedToKick(Player p) {
		GRankPerm gRankPerm = getRank(p);
		if (gRankPerm == GRankPerm.Media) {
			return false;
		}
		if (gRankPerm == GRankPerm.Player) {
			return false;
		}
		if (gRankPerm == GRankPerm.Premium) {
			return false;
		}
		if(SuperUser.isSuperUser(p)) {
			return true;
		}
		return true;
	}
}
