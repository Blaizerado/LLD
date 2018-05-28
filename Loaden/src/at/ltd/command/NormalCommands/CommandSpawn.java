package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.adds.Cf;
import at.ltd.lobby.LobbyUtils;

public class CommandSpawn implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		boolean b = LobbyUtils.tpPlayerToLobby(p);
		if (b) {
			Cf.rsS(Cf.TP_SPAWN, p);
		}
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
