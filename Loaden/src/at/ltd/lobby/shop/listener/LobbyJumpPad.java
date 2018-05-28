package at.ltd.lobby.shop.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import at.ltd.adds.utils.LocUtils;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.command.NormalCommands.CommandBuildMode;
import at.ltd.events.custom.EventPlayerMoveBlockEventAsync;

public class LobbyJumpPad implements Listener {
	public static Location locJ = LocUtils.locationByString("-2072.0*33.0*1367.0*0.0*0.0*GG_Lobby");
	public static Location throwLoc = LocUtils.locationByString("-2071.576868692258*34.0*1367.5209859767679*-227.07861*-36.45015*GG_Lobby");

	@EventHandler
	public void on(EventPlayerMoveBlockEventAsync e) {
		Location loc = e.getBlockStandingOn().getLocation();
		Player p = e.getPlayer();
		if (loc.equals(locJ)) {
			if (!CommandBuildMode.canBuild(p)) {
				AsyncThreadWorkers.submitSyncWork(() -> {
					p.setVelocity(throwLoc.getDirection().normalize().multiply(3.43D));
				});
			}
		}
	}

}
