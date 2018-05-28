package at.ltd.gungame.utils.melee.trowableaxe;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.game.player.GamePlayerDamage;
import at.ltd.adds.game.player.fac.BeforeDamageHandler;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.adds.utils.threading.removeable.RemoveAble;
import at.ltd.adds.utils.visual.Particles;
import at.ltd.gungame.events.custom.EventGunGameDamage.DamageType;

public class AxeThread implements Runnable {

	private Thread thread;
	private Player player;
	private double radius;
	private boolean running;
	private Item item;
	public static final String WEAPON_NAME = "Wurfstern";

	public AxeThread(Player player, double radius, Item item) {
		this.player = player;
		this.radius = radius;
		this.item = item;
		this.thread = new Thread(this);
	}

	public void start() {
		this.running = true;
		RemoveAble.addSec(() -> {
			if (item != null) {
				item.remove();
			}
			if (running) {
				AsyncThreadWorkers.submitSyncWork(() -> item.remove());
			}
		}, 10);
		this.thread.start();
	}

	public void stop() {
		this.running = false;
	}

	@Override
	public void run() {
		while (running) {
			for (Entity entity : item.getNearbyEntities(radius, radius, radius)) {
				if (entity instanceof Player) {
					Player target = (Player) entity;
					if (target != player) {

						AsyncThreadWorkers.submitSyncWork(new Runnable() {

							@Override
							public void run() {
								Particles.GRANADE_Tick(item.getLocation());
								AsyncThreadWorkers.submitSyncWork(() -> item.remove());
								if (target.isDead()) {
									return;
								}
								GamePlayerDamage.damagePlayer(target, player, 20, DamageType.THROWABLE_AXE, new BeforeDamageHandler() {

									@Override
									public void handle(GamePlayer tplayer, boolean dead) {
										tplayer.setLastDamager(GamePlayer.getGamePlayer(player));
										tplayer.setLastDamageWeaponName(WEAPON_NAME);
									}
								});
							}
						});
						this.stop();

					}
				}
			}

			if (item.isOnGround()) {
				Particles.GRANADE_Tick(item.getLocation());
				AsyncThreadWorkers.submitSyncWork(() -> item.remove());
				this.stop();
			}

			if (item == null) {
				this.stop();
			}

			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
