package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.adds.Cf;

public class CommandPlugins implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		p.sendMessage(Cf.rs(Cf.PLUGIN_COMMAND, p));

	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
