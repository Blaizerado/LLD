package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.adds.game.player.GamePlayer;

public class CommandLeave implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		GamePlayer.getGamePlayer(p).exitGunGame(false);
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
