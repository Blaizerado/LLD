package at.ltd.gungame.guns.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.gungame.guns.events.custom.EventProjectileHitAsync;

public class AsyncCallEvent implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(ProjectileHitEvent e) {
		EventProjectileHitAsync epha = new EventProjectileHitAsync(e, e.getEntity().getLocation());
		AsyncThreadWorkers.submitWork(() -> Bukkit.getPluginManager().callEvent(epha));
	}

}
