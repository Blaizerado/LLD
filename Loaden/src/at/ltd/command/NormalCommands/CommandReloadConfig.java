package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.superuser.SuperUser;
import at.ltd.events.custom.EventConfigReload;

public class CommandReloadConfig implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (SuperUser.isSuperUser(p)) {
			long time = System.currentTimeMillis();
			Main.readServerConfig();
			Main.setPreifx(Main.getServerConfig().get("Prefix"));
			EventConfigReload event = new EventConfigReload();
			Bukkit.getPluginManager().callEvent(event);
			Cf.init();
			long taken = System.currentTimeMillis() - time;
			p.sendMessage(Main.getPrefix() + "Reloaded. In " + taken + " Millisekunden.");
		} else {
			Cf.rsS(Cf.SUPERUSER_NORIGHTS, p);
		}
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
