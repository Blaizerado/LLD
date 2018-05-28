package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.superuser.SuperUser;
import at.ltd.adds.utils.threading.AsyncAble;
import at.ltd.lobby.ranking.RankingLastVoted;
import at.ltd.lobby.ranking.RankingTopKiller;

public class CommandUpdate implements CommandExecuter {

	@AsyncAble
	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (SuperUser.isSuperUser(p)) {
			p.sendMessage(Main.getPrefix() + "Initialisierung von einem Worker Thread wurde gestartet!");
			p.sendMessage(Main.getPrefix() + "Status: §cRankingTopKilled§2 updated!");
			RankingTopKiller.update();
			p.sendMessage(Main.getPrefix() + "Status: §cRankingTopKiller§2 updated!");
			RankingLastVoted.update();
			p.sendMessage(Main.getPrefix() + "Status: §cRankingLastVoted§2 updated!");
			p.sendMessage(Main.getPrefix() + "§2fertig!");
		} else {
			p.sendMessage(Cf.rs(Cf.SUPERUSER_NORIGHTS, p));
		}

	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
