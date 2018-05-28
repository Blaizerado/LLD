package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.utils.LocUtils;
import at.ltd.adds.utils.JsonMSG.FancyMessage;
import at.ltd.adds.utils.net.web.WebData;

public class CommandLoc implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		@SuppressWarnings("deprecation")
		Block block = p.getTargetBlock(null, 200);
		Location bl = block.getLocation();
		new FancyMessage(at.ltd.Main.getPrefix() + "Klick!").suggest(LocUtils.locationToString(bl)).send(p);
		p.sendMessage(Main.getPrefix() + "Oder: " + WebData.addData(LocUtils.locationToString(bl)));
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
