package at.ltd.command.NormalCommands;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.superuser.SuperUser;
import at.ltd.adds.utils.config.ConfigManager;
import at.ltd.gungame.level.GLevelUtils;

public class CommandSetLevelCoins implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (SuperUser.isSuperUser(p)) {
			if (args.size() == 3) {
				try {
					int id = Integer.valueOf(args.get(1));
					int coins = Integer.valueOf(args.get(2));
					HashMap<String, String> hm = ConfigManager.readConfig(GLevelUtils.FOLDER + id + ".txt");
					if (hm.containsKey("COINS")) {
						hm.remove("COINS");
					}
					hm.put("COINS", coins + "");
					ConfigManager.setConfig(hm, GLevelUtils.FOLDER + id + ".txt");
					p.sendMessage(Main.getPrefix() + "Reading all GLevels: 0/" + GLevelUtils.MAX_LEVEL);
					GLevelUtils.read();
					p.sendMessage(Main.getPrefix() + "Finish! GLevels: " + GLevelUtils.MAX_LEVEL + "/" + GLevelUtils.MAX_LEVEL);
				} catch (Exception e) {
					e.printStackTrace();
					p.sendMessage(Main.getPrefix() + "Ein Fehler! /setlevel <id> <coins> hast du wirklich nur Zahlen eingegeben?");
				}

			} else {
				p.sendMessage(Main.getPrefix() + "/setlevelcoins <id> <coins>");
			}
		} else {
			Cf.rsS(Cf.SUPERUSER_NORIGHTS, p);
		}
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
