package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.superuser.SuperUser;
import at.ltd.gungame.GameUtils.GameStatus;
import at.ltd.gungame.GunGame;

public class CommandStopRound implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (SuperUser.isSuperUser(p)) {
			if (GameStatus.MATCH == Main.getGameStatus()) {
				Cf.rsBC(Cf.GAME_STOP_MANUALLY);
				GunGame.getRoundManager().getGameRound().stop();
			} else {
				p.sendMessage(Main.getPrefix() + "Konnte nicht gestoppt werden.");
			}
		} else {
			Cf.rsS(Cf.SUPERUSER_NORIGHTS, p);
		}
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
