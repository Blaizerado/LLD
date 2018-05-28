package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;

public class CommandQuestion implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		p.sendMessage(Main.getPrefix() + "Du hast eine Frage?");
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
