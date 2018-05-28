package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.ts3.TeamSpeakVeri;
import at.ltd.adds.ts3.TeamSpeakWatcher;

public class CommandTs implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (args.size() == 2) {
			TeamSpeakVeri.veri(p, args.get(1));
		} else {
			p.sendMessage(Main.getPrefix() + "/ts <key>");
		}
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {

	}

}
