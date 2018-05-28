package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.utils.LocUtils;
import at.ltd.adds.utils.JsonMSG.FancyMessage;
import at.ltd.adds.utils.net.web.WebData;

public class CommandPos implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		new FancyMessage(at.ltd.Main.getPrefix() + "Klick!").suggest(LocUtils.locationToString(p.getLocation())).send(p);
		p.sendMessage(Main.getPrefix() + "Oder: " + WebData.addData(LocUtils.locationToString(p.getLocation())));
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
