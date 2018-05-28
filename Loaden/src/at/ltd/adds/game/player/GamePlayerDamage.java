package at.ltd.adds.game.player;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import at.ltd.Main;
import at.ltd.adds.game.player.fac.BeforeDamageHandler;
import at.ltd.adds.utils.LocUtils;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.adds.utils.visual.GhostManager;
import at.ltd.adds.utils.visual.Title;
import at.ltd.gungame.GunGame;
import at.ltd.gungame.events.custom.EventGameRoundStop;
import at.ltd.gungame.events.custom.EventGunGameDamage;
import at.ltd.gungame.events.custom.EventGunGameDamage.DamageType;
import at.ltd.gungame.events.custom.EventGunGameDeath;
import at.ltd.gungame.events.custom.EventGunGameSpawn;
import at.ltd.gungame.events.custom.EventPlayerProtectionEnd;
import at.ltd.gungame.events.custom.EventPlayerProtectionStart;
import at.ltd.gungame.guns.events.custom.EventGunShoot;

public class GamePlayerDamage implements Listener {

	public static HashMap<Player, Long> DEATH_PLAYER = new HashMap<>();
	public static int secToRespawn = 4;

	private static final NumberFormat formatter = new DecimalFormat("00");

	public static void init() {
		ArrayList<Player> remove = new ArrayList<>();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), () -> {
			for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
				GamePlayer ggp = GamePlayer.getGamePlayer(p);
				p.setHealth(ggp.getHealth());
			}
			for (Player p : DEATH_PLAYER.keySet()) {
				long time = DEATH_PLAYER.get(p) + 1000 * secToRespawn;
				long timedif = time - System.currentTimeMillis();
				if (timedif < 0.00D) {
					remove.add(p);
				} else {
					if (p.isOnline()) {
						Title.sendFullTitle(p, 0, 10, 0, ChatColor.YELLOW + ""
								+ formatter.format(TimeUnit.SECONDS.convert(timedif + 1000, TimeUnit.MILLISECONDS)),
								"");
					}
				}
			}
			if(!remove.isEmpty()) {
				for (Player p : remove) {
					DEATH_PLAYER.remove(p);
					Title.sendFullTitle(p, 0, 7, 2, ChatColor.GREEN + "GO!", "");
				}
				remove.clear();
			}
		}, 20, 5);
		Main.registerListener(new GamePlayerDamage());
	}

	@EventHandler
	public void on(EventGameRoundStop e) {
		DEATH_PLAYER.clear();
		GhostManager gm = GunGame.getGhostManager();
		for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
			if (gm.isGhost(p)) {
				gm.remove(p);
			}
		}
	}

	@EventHandler
	public void on(EventGunGameDamage e) {
		if (e.getDamager() != null) {
			if (GamePlayer.isDead(e.getDamager())) {
				if (e.getDamageType() == DamageType.PLAYER_DAMAGE) {
					e.setCancelled(true);
				}
			}
		}
		if (e.getDamager() != null) {
			if (!GamePlayer.isInRound(e.getDamager()) | !GamePlayer.isInRound(e.getDamagee())) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void on(EventGunShoot e) {
		if (GamePlayer.isDead(e.getPlayer())) {
			e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void on(EntityDamageByEntityEvent e) {
		if (e.isCancelled()) {
			return;
		}
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			Player damagee = (Player) e.getEntity();
			Player damager = (Player) e.getDamager();
			if (GamePlayer.isInRound(damagee)) {
				e.setCancelled(true);
				damagePlayer(damagee, damager, e.getDamage(), DamageType.PLAYER_DAMAGE);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void on(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			if (GamePlayer.isInRound((Player) e.getEntity())) {
				e.setCancelled(true);
			}
		}

	}

	public static EventGunGameDamage damagePlayer(Player damagee, Player damager, double damage, DamageType type,
			BeforeDamageHandler beforeDamage, boolean instantrespawn) {
		return damagePlayer(GamePlayer.getGamePlayer(damagee), GamePlayer.getGamePlayer(damager), damage, type,
				beforeDamage, instantrespawn);
	}

	public static EventGunGameDamage damagePlayer(Player damagee, Player damager, double damage, DamageType type) {
		return damagePlayer(GamePlayer.getGamePlayer(damagee), GamePlayer.getGamePlayer(damager), damage, type);
	}

	public static EventGunGameDamage damagePlayer(Player damagee, Player damager, double damage, DamageType type,
			BeforeDamageHandler beforeDamage) {
		return damagePlayer(GamePlayer.getGamePlayer(damagee), GamePlayer.getGamePlayer(damager), damage, type,
				beforeDamage);
	}

	public static EventGunGameDamage damagePlayer(GamePlayer damagee, GamePlayer damager, double damage,
			DamageType type, BeforeDamageHandler beforeDamageHandler, boolean instantrespawn) {
		EventGunGameDamage eventGunGameDamage = null;
		if (damager != null) {
			eventGunGameDamage = new EventGunGameDamage(damagee.getPlayer(), damager.getPlayer(), damage, type);
		} else {
			eventGunGameDamage = new EventGunGameDamage(damagee.getPlayer(), null, damage, type);
		}
		if (damagee.isDead()) {
			eventGunGameDamage.setCancelled(true);
			return eventGunGameDamage;
		}
		if (Main.isMainThread()) {
			Main.callEvent(eventGunGameDamage);
			if (eventGunGameDamage.isCancelled()) {
				return eventGunGameDamage;
			}
			handelDamage(eventGunGameDamage, damagee, beforeDamageHandler, instantrespawn);
		}
		return eventGunGameDamage;
	}

	public static EventGunGameDamage damagePlayer(GamePlayer damagee, GamePlayer damager, double damage,
			DamageType type, BeforeDamageHandler beforeDamageHandler) {
		EventGunGameDamage eventGunGameDamage = null;
		if (damager != null) {
			eventGunGameDamage = new EventGunGameDamage(damagee.getPlayer(), damager.getPlayer(), damage, type);
		} else {
			eventGunGameDamage = new EventGunGameDamage(damagee.getPlayer(), null, damage, type);
		}
		if (damagee.isDead()) {
			eventGunGameDamage.setCancelled(true);
			return eventGunGameDamage;
		}
		if (Main.isMainThread()) {
			Main.callEvent(eventGunGameDamage);
			if (eventGunGameDamage.isCancelled()) {
				return eventGunGameDamage;
			}
			handelDamage(eventGunGameDamage, damagee, beforeDamageHandler, false);
		}
		return eventGunGameDamage;
	}

	public static EventGunGameDamage damagePlayer(GamePlayer damagee, GamePlayer damager, double damage,
			DamageType type) {
		EventGunGameDamage eventGunGameDamage = null;
		if (damager != null) {
			eventGunGameDamage = new EventGunGameDamage(damagee.getPlayer(), damager.getPlayer(), damage, type);
		} else {
			eventGunGameDamage = new EventGunGameDamage(damagee.getPlayer(), null, damage, type);
		}
		if (damagee.isDead()) {
			eventGunGameDamage.setCancelled(true);
			return eventGunGameDamage;
		}
		if (Main.isMainThread()) {
			Main.callEvent(eventGunGameDamage);
			handelDamage(eventGunGameDamage, damagee, null, false);
		}
		return eventGunGameDamage;
	}

	private static void handelDamage(EventGunGameDamage damage, GamePlayer damagee, BeforeDamageHandler handler,
			boolean instantrespawn) {
		if (damage.isCancelled()) {
			return;
		}
		GameMode gm = damagee.getPlayer().getGameMode();
		if (gm == GameMode.CREATIVE | gm == GameMode.SPECTATOR) {
			return;
		}

		if (damage.getDamager() != null) {
			damage.getDamagee()
					.setVelocity(damage.getDamager().getLocation().add(0, 0.5, 0).getDirection().multiply(0.6));
		}

		double curhealth = damagee.getHealth() - damage.getDamage();
		if (curhealth < 1) {
			if (handler != null) {
				handler.handle(GamePlayer.getGamePlayer(damage.getDamagee()), true);
			}
			handelDeath(damage, damagee, instantrespawn);
		} else {
			if (handler != null) {
				handler.handle(GamePlayer.getGamePlayer(damage.getDamagee()), false);
			}
			damagee.setHealth(curhealth);
			damagee.getPlayer().setHealth(curhealth);
		}

	}

	private static void handelDeath(EventGunGameDamage eventGunGameDamage, GamePlayer damagee, boolean instantrespawn) {

		damagee.dead = true;

		Player p = damagee.getPlayer();
		damagee.resetHealth();

		EventGunGameDeath kill = null;
		if (eventGunGameDamage.getDamager() != null) {
			kill = new EventGunGameDeath(eventGunGameDamage.getDamager().getPlayer(), p);
		} else {
			kill = new EventGunGameDeath(null, p);
		}

		Main.callEvent(kill);

		try {
			damagee.gotKilledGunGame(kill.getLevelsBack(), eventGunGameDamage.getDamager());
			if (eventGunGameDamage.getDamager() != null) {
				GamePlayer.getGamePlayer(eventGunGameDamage.getDamager())
						.gotGunGameKill(eventGunGameDamage.getDamagee());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (instantrespawn) {
			EventGunGameSpawn spawnEvent = new EventGunGameSpawn(getSpawn(p), p, damagee);
			Main.callEvent(spawnEvent);
			damagee.dead = false;
			p.teleport(spawnEvent.getSpawnLocation());
		} else {
			DEATH_PLAYER.put(p, System.currentTimeMillis());
			GunGame.getGhostManager().add(p);

			EventPlayerProtectionStart stopevent = new EventPlayerProtectionStart(damagee);
			Main.callEvent(stopevent);

			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () -> {
				if (AsyncThreadWorkers.isPlayerOnline(p) && damagee.isInRound()) {
					EventGunGameSpawn spawnEvent = new EventGunGameSpawn(getSpawn(p), p, damagee);
					Main.callEvent(spawnEvent);
					p.teleport(spawnEvent.getSpawnLocation());
				}
			}, (20 * secToRespawn) / 2);

			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () -> {
				damagee.dead = false;
				GunGame.getGhostManager().remove(p);
				EventPlayerProtectionEnd startevent = new EventPlayerProtectionEnd(damagee);
				Main.callEvent(startevent);

			}, 20 * secToRespawn);
		}

	}

	private static Location getSpawn(Player p) {
		GamePlayer ggp = GamePlayer.getGamePlayer(p);
		Location loc = null;
		ArrayList<Location> spawns = ggp.getCurrentGameMap().getSpawns();
		if (AsyncThreadWorkers.getOnlinePlayers().size() > 6) {
			loc = LocUtils.getBestLocationWithoutPlayer(spawns);
		} else {
			loc = spawns.get((new Random()).nextInt(spawns.size()));
		}
		return loc;

	}

}
