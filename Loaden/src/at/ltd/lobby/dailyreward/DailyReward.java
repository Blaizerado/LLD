package at.ltd.lobby.dailyreward;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import at.ltd.Main;
import at.ltd.adds.utils.LocUtils;
import at.ltd.adds.utils.data.Cooldown;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.events.custom.EventPlayerInteractAsync;

public class DailyReward implements Listener{
	
	public static void init() {
		Main.registerListener(new DailyReward());
	}

	private static Cooldown<Player> CD = new Cooldown<>();
	
	@EventHandler
	public void on(EventPlayerInteractAsync e) {
		if(e.getClickedBlock() != null) {
			Location loc = e.getClickedBlock().getLocation();
			if(loc != null) {
				if(LocUtils.isSameLocation(loc, LocUtils.locationByString("-2089.0*34.0*1380.0*0.0*0.0*GG_Lobby"))) {
					AsyncThreadWorkers.submitSyncWork(() -> e.getPlayer().closeInventory());
					e.getPlayer().sendMessage(Main.getPrefix() +"§cIn Arbeit.");;
				}
			}
		}
	}
	
}
