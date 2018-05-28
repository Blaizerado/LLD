package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.adds.Cf;

public class CommandSay implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		Cf.rsS(Cf.COMMAND_SAY, p);
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
