package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.superuser.SuperUser;
import at.ltd.gungame.level.GLevelUtils;

public class CommandSetLevel implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (SuperUser.isSuperUser(p)) {
			if (args.size() == 2) {
				try {
					int id = Integer.valueOf(args.get(1));
					GLevelUtils.setLevel(p, id);
					p.sendMessage(Main.getPrefix() + "Reading all GLevels: 0/" + GLevelUtils.MAX_LEVEL);
					GLevelUtils.read();
					p.sendMessage(Main.getPrefix() + "Finish! GLevels: " + GLevelUtils.MAX_LEVEL + "/" + GLevelUtils.MAX_LEVEL);
				} catch (Exception e) {
					e.printStackTrace();
					p.sendMessage(Main.getPrefix() + "Ein Fehler! /setlevel <id> hast du wirklich nur eine Zahl eingegeben?");
				}

			} else {
				p.sendMessage(Main.getPrefix() + "/setlevel <id>");
			}
		}else{
			Cf.rsS(Cf.SUPERUSER_NORIGHTS, p);
		}
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
