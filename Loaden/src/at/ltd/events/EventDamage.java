package at.ltd.events;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import at.ltd.command.NormalCommands.CommandBuildMode;
import at.ltd.lobby.LobbyUtils;

public class EventDamage implements Listener {

	@EventHandler
	public void on(EntityDamageByEntityEvent e) {
		if (e.getDamager().getType() == EntityType.PLAYER) {
			Player p = (Player) e.getDamager();
			if (LobbyUtils.isLocationInLobby(p.getLocation())) {
				if (!CommandBuildMode.canBuild(p)) {
					e.setCancelled(true);
					return;
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamageEvent(final EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		if (e.getCause() == DamageCause.FALL) {
			e.setCancelled(true);
		}
	}
}
