package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CommandSettings implements CommandExecuter{

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		p.sendMessage("in arbeit");
		
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
