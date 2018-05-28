package at.ltd.gungame.tablist;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.sql.sqlutils.SQLCollection;
import at.ltd.adds.utils.ScoreBoardManager;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.adds.utils.time.TimeCounter;
import at.ltd.adds.utils.time.TimeUnit;
import at.ltd.gungame.GameUtils;
import at.ltd.gungame.ranks.GRankPerm;
import at.ltd.gungame.ranks.GRankUtils;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class TabManager {

	public static HashMap<GRankPerm, TabTeam> teams = new HashMap<>();
	public static HashMap<Player, TabTeam> players = new HashMap<>();

	public static void start() {
		Scoreboard sb = ScoreBoardManager.getScoreBoard();
		Team inhaber = sb.registerNewTeam("0000Inhaber");
		Team admin = sb.registerNewTeam("0001Admin");
		Team developer = sb.registerNewTeam("0002Developer");
		Team cm = sb.registerNewTeam("0003CM");
		Team Moderator = sb.registerNewTeam("0004Mod");
		Team TestMod = sb.registerNewTeam("0005TestMod");
		Team Builder = sb.registerNewTeam("0006Builder");
		Team Event = sb.registerNewTeam("0007Event");
		Team Supporter = sb.registerNewTeam("0008Supporter");
		Team TestSup = sb.registerNewTeam("0009TestSup");
		Team Media = sb.registerNewTeam("0010Media");
		Team Premium = sb.registerNewTeam("0011Premium");
		Team Player = sb.registerNewTeam("0012Player");
		addPrefix(GRankPerm.Owner, inhaber, "Inhaber");
		addPrefix(GRankPerm.Admin, admin, "Admin");
		addPrefix(GRankPerm.Developer, developer, "Developer");
		addPrefix(GRankPerm.CM, cm, "CM");
		addPrefix(GRankPerm.Moderator, Moderator, "Mod");
		addPrefix(GRankPerm.TestMod, TestMod, "TestMod");
		addPrefix(GRankPerm.Builder, Builder, "Builder");
		addPrefix(GRankPerm.Event, Event, "Event");
		addPrefix(GRankPerm.Supporter, Supporter, "Supporter");
		addPrefix(GRankPerm.TestSup, TestSup, "Testsupp");
		addPrefix(GRankPerm.Media, Media, "Media");
		addPrefix(GRankPerm.Premium, Premium, "Premium");
		addPrefix(GRankPerm.Player, Player, "Spieler");
		TimeUnit tab = new TimeUnit(true, 60, "TabList (BukkitAsync)");
		AsyncThreadWorkers.submitSchedulerWorkTick(() -> {
			TimeCounter.start(tab);
			try {
				setPrefix();
			} catch (Exception e) {
				TimeCounter.stop(tab);
				e.printStackTrace();
			}

			if (tab.isInSession()) {
				TimeCounter.stop(tab);
			}
		}, 20, 8);
		TabFHAnimation.start();
	}

	public static void addPrefix(GRankPerm gRankPerm, Team team, String name) {
		String prefix = ChatColor.translateAlternateColorCodes('&', PermissionsEx.getPermissionManager().getGroup(name).getPrefix());
		TabTeam tt = new TabTeam(name, team, gRankPerm, prefix);
		tt.getTeam().setAllowFriendlyFire(true);
		tt.getTeam().setCanSeeFriendlyInvisibles(true);
		teams.put(gRankPerm, tt);
	}

	@SuppressWarnings("deprecation")
	public static synchronized void setPrefix() {
		for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
			GRankPerm grp = GRankPerm.getRank(p);
			TabTeam tt = teams.get(grp);
			tt.getTeam().addPlayer(p);
			setName(p, tt);
		}

	}

	private static final NumberFormat formatter = new DecimalFormat("00");
	
	public static void setName(Player p, TabTeam tabTeam) {
		try {
			String s = new String(tabTeam.getPrefix());
			s = s.replace("[GGRANG]", GRankUtils.getRank(SQLCollection.getPlayer(p).getKills()).getSymbol());
			s = s.replace("[NAME]", p.getName());
			s = s.replace("[GGLEVEL]", formatter.format(GamePlayer.getGamePlayer(p).getGunGameLevel()) + "");
			s = s.replace("[STATUSBAR]", GameUtils.getPlayerStatusBar(p));
			final String finals = s;
			AsyncThreadWorkers.submitSyncWork(() -> {
				p.setPlayerListName(finals);
				p.setCustomName(finals);
			});
		} catch (Exception e) {
		}

	}

}
