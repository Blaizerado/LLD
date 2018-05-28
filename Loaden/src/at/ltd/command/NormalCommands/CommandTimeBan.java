package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.adds.Cf;
import at.ltd.adds.bansystem.TimeBan;
import at.ltd.gungame.ranks.GRankPerm;

public class CommandTimeBan implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (GRankPerm.isAllowedToBan(p)) {
			TimeBan.start(p);
		} else {
			Cf.rsS(Cf.NO_RIGHTS, p);
		}
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
