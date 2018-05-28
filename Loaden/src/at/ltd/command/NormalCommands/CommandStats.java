package at.ltd.command.NormalCommands;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.sql.SQLPlayer;
import at.ltd.adds.sql.sqlutils.SQLCollection;
import at.ltd.adds.utils.GameMath;
import at.ltd.adds.utils.data.Data3;
import at.ltd.adds.utils.threading.AsyncAble;
import at.ltd.gungame.ranks.GRankPerm;

public class CommandStats implements CommandExecuter {

	@Override
	@AsyncAble
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (args.size() == 2) {
			Player from = Bukkit.getPlayer(args.get(1));
			if (from == null) {
				Cf.rsS(Cf.PLAYER_NOT_FOUND, p);
				return;
			}
			send(from, p);
		} else if (args.size() == 1) {
			send(p, p);
		} else {
			p.sendMessage(Main.getPrefix() + "/stats (<Spieler>)");
		}
	}

	public static void send(Player from, Player to) {
		SQLPlayer sql = SQLCollection.getPlayer(from);

		String rank = GRankPerm.getRank(from).getPerm();

		String name = from.getName();

		int deaths = sql.getDeaths();
		int kills = sql.getKills();
		long millis = TimeUnit.MINUTES.toMillis(sql.getPlaytime());
		Data3<String, String, String> data = getTime(millis);
		String days = data.getFirst();
		String hours = data.getSecond();
		String min = data.getThird();
		int roundsplayed = sql.getRoundsPlayed();
		String KD = new DecimalFormat("#.##").format(GameMath.getKD(kills, deaths));
		String firstjoin = sql.getFirstjoin().toGMTString().replace(":00:00 GMT", " Uhr");
		Cf.rsS(Cf.STATS_MSG, to, "[ROUNDS]", roundsplayed, "[RANG]", rank, "[PLAYERNAME]", name, "[DEATHS]", deaths, "[KILLS]", kills, "[KD]", KD, "[FIRSTJOIN]", firstjoin, "[DAYS]", days, "[HOURS]", hours, "[MIN]", min);

	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

	public static Data3<String, String, String> getTime(long timemillis) {
		long time = timemillis;
		long seconds = time / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		long days = hours / 24;
		Data3<String, String, String> data = new Data3<String, String, String>(days + "", hours % 24 + "", minutes % 60 + "");
		return data;
	}

}
