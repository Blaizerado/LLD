package at.ltd.gungame.utils.hellfire;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.game.player.GamePlayerDamage;
import at.ltd.adds.game.player.fac.BeforeDamageHandler;
import at.ltd.adds.utils.LocUtils;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.gungame.events.custom.EventGunGameDamage.DamageType;

public class HellfireRocket {

	private Location to;
	private Location from;
	private Player shooter;
	private Snowball missile;
	private boolean flying = false;
	private long timeFired;
	private int gain;

	private double damage = 14.00D;
	private boolean fire = true;

	public static ConcurrentHashMap<String, HellfireRocket> FLYING_HELLFIRES = new ConcurrentHashMap<>();

	public HellfireRocket(Location from, Location to, int gain, Player shooter) {
		this.from = from;
		this.to = to;
		this.shooter = shooter;
		this.gain = gain;
	}

	public void fire() {
		if (!Bukkit.isPrimaryThread()) {
			throw new IllegalAccessError("Only on the Main thread!");
		}
		Vector from = new Vector(this.from.getX(), this.from.getY(), this.from.getZ());
		Vector to = new Vector(this.to.getX(), this.to.getY(), this.to.getZ());

		Vector v = LocUtils.calculateVelocity(from, to, gain);
		Snowball s = this.from.getWorld().spawn(this.from, Snowball.class);
		if (shooter != null) {
			s.setShooter(shooter);
		}
		s.setInvulnerable(true);

		missile = s;
		flying = true;
		FLYING_HELLFIRES.put(s.getUniqueId().toString(), this);
		s.setVelocity(v);
		timeFired = System.currentTimeMillis();
	}

	public void hit(Location loc) {
		HellfireExplosionEffect.makeEffect(loc);
		handelDamage(loc);
		remove();
	}

	private void handelDamage(Location loc) {
		if (damage < 0.5D) {
			return;
		}
		List<Player> damaged = LocUtils.getPlayersNearLocation(loc, 10);
		for (Player p : damaged) {
			if (p.isDead()) {
				continue;
			}
			if (shooter != null) {
				GamePlayerDamage.damagePlayer(p, shooter, 20, DamageType.HELLFIRE, new BeforeDamageHandler() {

					@Override
					public void handle(GamePlayer player, boolean dead) {
						player.setLastDamager(GamePlayer.getGamePlayer(shooter));
						player.setLastDamageWeaponName("Hellfire");
					}
				});
			} else {
				GamePlayerDamage.damagePlayer(p, null, 20, DamageType.HELLFIRE, new BeforeDamageHandler() {

					@Override
					public void handle(GamePlayer player, boolean dead) {
						player.setLastDamager(null);
						player.setLastDamageWeaponName("Hellfire");
					}
				});
			}

		}
	}

	public void remove() {
		flying = false;
		AsyncThreadWorkers.submitSyncWork(() -> {
			FLYING_HELLFIRES.remove(missile.getUniqueId().toString());
			missile.remove();
		});

	}

	public Location getTo() {
		return to;
	}

	public Location getFrom() {
		return from;
	}

	public Player getShooter() {
		return shooter;
	}

	public long getTimeFired() {
		return timeFired;
	}

	public Snowball getMissile() {
		return missile;
	}

	public boolean isFlying() {
		return flying;
	}

	public void setFlying(boolean flying) {
		this.flying = flying;
	}

	public int getGain() {
		return gain;
	}

	public double getDamage() {
		return damage;
	}

	public HellfireRocket setDamage(double damage) {
		this.damage = damage;
		return this;
	}

	public boolean isFireEnabled() {
		return fire;
	}

	public HellfireRocket setFireOnHit(boolean fire) {
		this.fire = fire;
		return this;
	}

}
