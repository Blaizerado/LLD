package at.ltd.adds.bansystem.events;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import at.ltd.Main;
import at.ltd.adds.bansystem.events.custom.EventBan;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;

public class BanListener implements Listener {

	@EventHandler
	public void on(EventBan e) {
		Player banner = e.getBanner();
		if (Main.isOnlineUUID(e.getToBeBannedUUID())) {
			Player target = Bukkit.getPlayer(UUID.fromString(e.getToBeBannedUUID()));
			if (target.getName().equals(banner.getName())) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onLog(EventBan e) {
		Player banner = e.getBanner();
		if (!e.isCancelled()) {
			AsyncThreadWorkers.submitWork(() -> {
				try {
					Main.createSQLQuery().updateSQL("DELETE FROM `BAN_LOG` WHERE `TARGET` LIKE \"" + e.getToBeBannedUUID() + "\";");
				} catch (Exception e2) {
				}
				try {
					Main.createSQLQuery().updateSQL("INSERT INTO `BAN_LOG` (`BANNER`, `TARGET`) VALUES ('" + banner.getUniqueId().toString() + "', '" + e.getToBeBannedUUID() + "');");
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			});
		}
	}

}
