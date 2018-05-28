package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.command.CommandManager;

public class CommandMe implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		p.sendMessage(Main.getPrefix() + "Hier: " + CommandManager.getLastArgs(args, 1));
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
