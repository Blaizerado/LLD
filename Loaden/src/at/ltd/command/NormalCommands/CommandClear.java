package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;

public class CommandClear implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (args.size() == 1) {
			p.getInventory().clear();
			Cf.rsS(Cf.CLEAR, p);
		} else {
			if (args.size() == 2) {
				Player set;
				if (Bukkit.getPlayer(args.get(1)) != null) {
					set = Bukkit.getPlayer(args.get(1));
				} else {
					Cf.rsS(Cf.CLEAR_PLAYER_NOT_FOUND, p);
					return;
				}

				set.getInventory().clear();
				Cf.rsS(Cf.CLEAR_REMOTE, p);
			} else {
				p.sendMessage(Main.getPrefix() + "/clear ([Player])");
			}

		}

	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
