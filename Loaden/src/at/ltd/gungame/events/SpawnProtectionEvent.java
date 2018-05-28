package at.ltd.gungame.events;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import at.ltd.Main;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.gungame.events.custom.EventGameRoundStop;
import at.ltd.gungame.events.custom.EventGunGameSpawn;
import at.ltd.gungame.events.custom.EventLeaveGame;
import at.ltd.gungame.guns.events.custom.EventBulletHitPlayer;
import at.ltd.gungame.guns.events.custom.EventGunShoot;

public class SpawnProtectionEvent implements Listener {

	private static ArrayList<Player> PROTECTED_PLAYER = new ArrayList<>();
	private static final int TICKS = 20 * 2;

	@EventHandler
	public void on(EventGameRoundStop e) {
		AsyncThreadWorkers.submitDelayedTickWork(() -> {
			PROTECTED_PLAYER.clear();
		}, 5);
	}

	@EventHandler
	public void on(EventGunGameSpawn e) {
		Player p = e.getPlayer();
		add(p);
	}

	@EventHandler
	public void on(EventLeaveGame e) {
		Player p = e.getPlayer();
		remove(p);
	}

	@EventHandler
	public void on(EventGunShoot e) {
		if (PROTECTED_PLAYER.contains(e.getPlayer())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void on(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		remove(p);
	}

	@EventHandler
	public void on(EventBulletHitPlayer e) {
		Player p = e.getEnemy();
		if (PROTECTED_PLAYER.contains(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void on(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (PROTECTED_PLAYER.contains(p)) {
				e.setCancelled(true);
			}
		}
	}

	public static void add(Player p) {
		if (PROTECTED_PLAYER.contains(p)) {
			return;
		}
		PROTECTED_PLAYER.add(p);
		//GHOST_MANAGER.addPlayer(p);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () -> {
			remove(p);
		}, TICKS);
	}

	public static void remove(Player p) {
		if (!PROTECTED_PLAYER.contains(p)) {
			return;
		}
		//GHOST_MANAGER.removePlayer(p);
		PROTECTED_PLAYER.remove(p);
	}

}
