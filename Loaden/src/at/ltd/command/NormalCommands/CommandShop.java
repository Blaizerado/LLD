package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.utils.LocUtils;
import at.ltd.events.custom.EventTeleport;

public class CommandShop implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		GamePlayer.getGamePlayer(p).exitGunGame(false);
		EventTeleport event = new EventTeleport(p, LocUtils.locationByString("-2109.671126557142*48.0*1361.6226280032565*-76.47002*-4.349928*GG_Lobby"), "SHOP");
		Main.callEvent(event);
		if(!event.isCancelled()) {
			p.teleport(event.getLocation());
		}
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
