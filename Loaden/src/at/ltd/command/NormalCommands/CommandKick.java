package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.command.CommandManager;
import at.ltd.gungame.ranks.GRankPerm;

public class CommandKick implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (!GRankPerm.isAllowedToKick(p)) {
			Cf.rsS(Cf.NO_RIGHTS, p);
			return;
		}
		if (args.size() > 2 | args.size() == 2) {
			Player target = Bukkit.getPlayer(args.get(1));
			if (target == null) {
				p.sendMessage(Main.getPrefix() + "Spieler wurde nicht gefunden!");
				return;
			}
			if (args.size() == 2) {
				target.kickPlayer(Cf.rs(Cf.KICK_NO_REASON, target));
			} else {
				target.kickPlayer(Cf.rs(Cf.KICK_REASON, p, "[REASON]", CommandManager.getLastArgs(args, 2)));
			}
			Cf.rsS(Cf.KICK_SUCCESS, p);
		} else {
			p.sendMessage(Main.getPrefix() + "/kick <Player> (<Reason>)");
		}

	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
