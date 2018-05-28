package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.superuser.SuperUser;
import at.ltd.gungame.level.GLevelUtils;

public class CommandLoadLevel implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (SuperUser.isSuperUser(p)) {
			if (args.size() == 2) {
				try {
					int id = Integer.valueOf(args.get(1));
					if (!GLevelUtils.containsGLevel(id)) {
						p.sendMessage(Main.getPrefix() + "Dieses Level gibt es nicht!");
						return;
					}
					GLevelUtils.removeItems(p);
					GLevelUtils.setInventory(p, id);
					p.sendMessage(Main.getPrefix() + "Fertig! Coins für das level: " + GLevelUtils.getGLevel(id).getCoins());
				} catch (Exception e) {
					e.printStackTrace();
					p.sendMessage(Main.getPrefix() + "Ein Fehler! /loadlevel <id> hast du wirklich nur eine Zahl eingegeben?");
				}

			} else {
				p.sendMessage(Main.getPrefix() + "/loadlevel <id>");
			}
		} else {
			Cf.rsS(Cf.SUPERUSER_NORIGHTS, p);
		}
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
