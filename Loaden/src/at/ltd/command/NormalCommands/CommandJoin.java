package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.adds.game.player.GamePlayer;

public class CommandJoin implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		GamePlayer.getGamePlayer(p).joinGunGame(false);
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
