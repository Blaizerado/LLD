package at.ltd.gungame.guns.events;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.events.custom.EventPlayerQuitAsync;
import at.ltd.gungame.events.custom.EventGameRoundStop;
import at.ltd.gungame.events.custom.EventLeaveGame;
import at.ltd.gungame.guns.utils.bullet.Bullet;

public class BulletRemoveEvent implements Listener {

	@EventHandler
	public void on(EventLeaveGame e) {
		AsyncThreadWorkers.submitWork(() -> {
			Player p = e.getPlayer();
			ArrayList<Bullet> bullets = new ArrayList<>();
			for (Bullet b : Bullet.getBullets().values()) {
				if (b.getShooter() == p) {
					bullets.add(b);
				}
			}

			if (!bullets.isEmpty()) {
				AsyncThreadWorkers.submitSyncWork(() -> {
					for (Bullet b : bullets) {
						b.removeBullet();
					}
				});

			}
		});

	}

	@EventHandler
	public void on(EventGameRoundStop e) {
		ArrayList<Bullet> bullets = new ArrayList<>();
		for (GamePlayer ggp : e.getGunGamePlayers()) {
			for (Bullet b : Bullet.getBullets().values()) {
				if (b.getShooter().equals(ggp.getPlayer())) {
					bullets.add(b);
				}
			}

		}
		for (Bullet b : bullets) {
			b.removeBullet();
		}

	}

	@EventHandler
	public void on(EventPlayerQuitAsync e) {
		Player p = e.getPlayer();
		ArrayList<Bullet> bullets = new ArrayList<>();
		for (Bullet b : Bullet.getBullets().values()) {
			if (b.getShooter() == p) {
				bullets.add(b);
			}
		}

		if (!bullets.isEmpty()) {
			for (Bullet b : bullets) {
				AsyncThreadWorkers.submitSyncWork(() -> b.removeBullet());
			}
		}
	}

}
