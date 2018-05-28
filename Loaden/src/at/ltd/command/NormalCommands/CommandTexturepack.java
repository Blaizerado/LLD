package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.adds.Cf;
import at.ltd.events.EventJoin;

public class CommandTexturepack implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		Cf.rsS(Cf.TEXTUREPACK, p, "[LINK]", EventJoin.downloadlink);
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
