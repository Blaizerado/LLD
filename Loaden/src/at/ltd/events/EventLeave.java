package at.ltd.events;

import java.sql.Timestamp;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import at.ltd.adds.Cf;
import at.ltd.adds.sql.SQLPlayer;
import at.ltd.adds.sql.sqlutils.SQLCollection;
import at.ltd.adds.sql.sqlutils.SQLManagePlayer;

public class EventLeave implements Listener {

	@EventHandler
	public void on(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		SQLPlayer player = SQLCollection.getPlayer(p);
		if (player != null) {
			player.setLastupdate(new Timestamp(System.currentTimeMillis()));
			player.setName(p.getName());
			player.setUUID(p.getUniqueId().toString());
			e.setQuitMessage(Cf.rs(Cf.LEAVE_MESSAGE, p));
			SQLManagePlayer.removePlayer(p);
		} else {
			System.out.println("[LTD] SQL PLAYER = NULL | at.ltd.events.EventLeave ");
		}

	}

}
