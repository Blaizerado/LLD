package at.ltd.gungame.utils.hellfire;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import at.ltd.Main;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.adds.utils.visual.Particles;
import at.ltd.gungame.guns.events.custom.EventProjectileHitAsync;

public class HellfireManager implements Listener {

	public static void init() {
		Bukkit.getServer().getPluginManager().registerEvents(new HellfireManager(), Main.getPlugin());
		AsyncThreadWorkers.submitSchedulerWorkTick(() -> {
			ArrayList<String> remove = new ArrayList<>();
			for (HellfireRocket rocket : HellfireRocket.FLYING_HELLFIRES.values()) {
				Snowball s = rocket.getMissile();
				Location loc = AsyncThreadWorkers.getEntityLocation(s);
				if (loc != null) {
					Particles.HELLFIRE_TRACE(loc);
				}
				if (!AsyncThreadWorkers.getEntityStatus(s)) {
					if (loc != null) {
						remove.add(s.getUniqueId().toString());
					}
				} else if ((System.currentTimeMillis() - rocket.getTimeFired()) > 30000) {
					remove.add(s.getUniqueId().toString());
				}

			}

			for (String uuid : remove) {
				HellfireRocket.FLYING_HELLFIRES.get(uuid).remove();
			}
		}, 20, 0);
	}

	@EventHandler
	public void on(EventProjectileHitAsync e) {
		String uuid = e.getUUID();
		if (HellfireRocket.FLYING_HELLFIRES.containsKey(uuid)) {
			HellfireRocket hr = HellfireRocket.FLYING_HELLFIRES.get(uuid);
			AsyncThreadWorkers.submitSyncWork(() -> hr.hit(e.getLocation()));
		}
	}

}
