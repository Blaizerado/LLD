package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.adds.Cf;
import at.ltd.adds.utils.threading.AsyncAble;

public class CommandHelp implements CommandExecuter {

	@AsyncAble
	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		p.sendMessage(Cf.rs(Cf.HELP_1, p));
		p.sendMessage(Cf.rs(Cf.HELP_2, p));
		p.sendMessage(Cf.rs(Cf.HELP_3, p));
		p.sendMessage(Cf.rs(Cf.HELP_4, p));
		p.sendMessage(Cf.rs(Cf.HELP_5, p));
		p.sendMessage(Cf.rs(Cf.HELP_6, p));
		p.sendMessage(Cf.rs(Cf.HELP_7, p));
		p.sendMessage(Cf.rs(Cf.HELP_8, p));
		p.sendMessage(Cf.rs(Cf.HELP_9, p));
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
