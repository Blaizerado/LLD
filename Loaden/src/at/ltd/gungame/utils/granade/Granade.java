package at.ltd.gungame.utils.granade;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import at.ltd.Main;
import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.game.player.GamePlayerDamage;
import at.ltd.adds.game.player.fac.BeforeDamageHandler;
import at.ltd.adds.utils.LocUtils;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.adds.utils.visual.Particles;
import at.ltd.gungame.events.custom.EventGunGameDamage.DamageType;
import at.ltd.gungame.events.custom.EventGunGameSpawn;
import at.ltd.gungame.guns.events.custom.EventProjectileHitAsync;

public class Granade implements Listener {

	public static CopyOnWriteArrayList<String> UUIDS = new CopyOnWriteArrayList<>();

	public static void init() {
		Bukkit.getServer().getPluginManager().registerEvents(new Granade(), Main.getPlugin());
	}

	@EventHandler
	public void on(EventProjectileHitAsync e) {
		if (e.getEntity() instanceof Egg) {
			Egg egg = (Egg) e.getEntity();
			if (egg.getShooter() instanceof Player) {
				Player player = (Player) egg.getShooter();
				AsyncThreadWorkers.submitSyncWork(new Runnable() {

					@Override
					public void run() {
						Item ground = player.getWorld().dropItem(egg.getLocation(), new ItemStack(Material.EGG));
						UUIDS.add(ground.getUniqueId().toString());

						AsyncThreadWorkers.submitDelayedWorkMili(() -> {
							UUIDS.remove(ground.getUniqueId().toString());
							AsyncThreadWorkers.submitSyncWork(() -> {
								GranadeExplosionEffect.makeEffect(ground.getLocation());
								ground.remove();
							});
							handelDamage(ground.getLocation(), player);
						}, 2600);
						AsyncThreadWorkers.submitRepeatingTickWork(new Runnable() {

							@Override
							public void run() {
								Location loc = AsyncThreadWorkers.getEntityLocation(ground);
								if (loc != null) {
									Particles.GRANADE_Tick(loc);
								}
							}
						}, 50);
					}
				});

			}
		}
	}

	@EventHandler
	public void on(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_AIR | e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (!GamePlayer.isInRound(e.getPlayer())) {
				ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
				if (is != null && is.getType() == Material.EGG) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void on(PlayerEggThrowEvent e) {
		e.getEgg().setVelocity(e.getPlayer().getLocation().getDirection().multiply(1.3D));
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {

			@Override
			public void run() {
				e.getEgg().remove();
			}
		}, 10 * 20);
	}

	@EventHandler
	public void on(EntityPickupItemEvent e) {
		if (UUIDS.contains(e.getItem().getUniqueId().toString())) {
			e.setCancelled(true);
		}
	}

	private void handelDamage(Location loc, Player shooter) {
		List<Player> damaged = LocUtils.getPlayersNearLocation(loc, 5);
		AsyncThreadWorkers.submitSyncWork(new Runnable() {

			@Override
			public void run() {
				for (Player p : damaged) {
					if (shooter != null) {
						if (p.isDead()) {
							continue;
						}
						GamePlayerDamage.damagePlayer(p, shooter, 20.00D, DamageType.GRANADE, new BeforeDamageHandler() {

							@Override
							public void handle(GamePlayer player, boolean dead) {
								player.setLastDamager(GamePlayer.getGamePlayer(shooter));
								player.setLastDamageWeaponName("Granate");
							}
						});
					}

				}
			}
		});
	}

	@EventHandler
	public void on(EventGunGameSpawn e) {
		e.getPlayer().setFireTicks(-20);
	}

}
