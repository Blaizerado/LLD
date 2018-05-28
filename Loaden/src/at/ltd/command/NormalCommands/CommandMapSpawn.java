package at.ltd.command.NormalCommands;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.superuser.SuperUser;
import at.ltd.adds.utils.LocUtils;
import at.ltd.adds.utils.config.ConfigManager;

public class CommandMapSpawn implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (SuperUser.isSuperUser(p)) {
			String saveLoc = "plugins/LTD/WorldSpawn/" + p.getLocation().getWorld().getName() + ".txt";
			HashMap<String, String> hm = ConfigManager.readConfig(saveLoc);
			Integer id = 0;
			if (!hm.isEmpty()) {
				for (String s : hm.keySet()) {
					Integer i = Integer.valueOf(s);
					if (i > id || id.intValue() == i.intValue()) {
						id = i;
					}
				}
			}
			id++;
			hm.put(id.toString(), LocUtils.locationToString(p.getLocation()));
			ConfigManager.setConfig(hm, saveLoc);
			p.sendMessage(Main.getPrefix() + "Spawn wurde gesetzt! Id: " + id);
		} else {
			p.sendMessage(Cf.rs(Cf.SUPERUSER_NORIGHTS, p));
		}
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
