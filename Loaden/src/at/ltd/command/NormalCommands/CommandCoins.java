package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.adds.Cf;
import at.ltd.adds.utils.threading.AsyncAble;

public class CommandCoins implements CommandExecuter {

	@AsyncAble
	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		p.sendMessage(Cf.rs(Cf.GETCOINS, p));

	}


	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
