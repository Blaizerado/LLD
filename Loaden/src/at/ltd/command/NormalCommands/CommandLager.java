package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.gungame.lager.Lager;

public class CommandLager implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		Lager.lagerTP(p);

	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
