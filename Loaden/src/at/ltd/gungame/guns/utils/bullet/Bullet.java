package at.ltd.gungame.guns.utils.bullet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.adds.utils.threading.removeable.RemoveAble;
import at.ltd.gungame.guns.events.custom.EventBulletHitPlayer;
import at.ltd.gungame.guns.events.custom.EventBulletHitPlayerAsync;
import at.ltd.gungame.guns.events.custom.EventBulletLaunchAsync;
import at.ltd.gungame.guns.utils.Gun;

public class Bullet {

	protected final static HashMap<String, Bullet> FLYING_BULLETS = new HashMap<>();

	protected String uuid;
	protected double damage;
	protected Snowball snowball;
	protected Player shooter;
	protected long timeshot;
	protected int flytime;
	protected Bullet instance;
	protected Gun gun;
	protected Location locfromplayer;
	protected Location locfromplayereye;
	protected Location hitlocation;
	protected Vector bulletvector;
	protected static float multi = 0.24F;
	protected boolean flying = false;
	protected Location currentLocation;

	protected boolean removed = false;

	public Bullet(Player p, double damage, double speed, float accuracy, int flytime, Gun gun) {
		Snowball snowball = p.launchProjectile(Snowball.class);
		snowball.setGravity(false);
		this.snowball = snowball;
		this.flying = true;
		this.uuid = snowball.getUniqueId().toString();
		this.damage = damage;
		this.shooter = p;
		this.timeshot = System.currentTimeMillis();
		this.flytime = flytime;
		this.gun = gun;
		this.locfromplayer = p.getLocation();
		this.locfromplayereye = p.getEyeLocation();
		this.instance = this;
		this.currentLocation = snowball.getLocation();

		synchronized (FLYING_BULLETS) {
			FLYING_BULLETS.put(uuid, this);
		}

		AsyncThreadWorkers.submitWork(() -> {
			Location loc = locfromplayer;
			Random r = new Random();
			double yaw = Math.toRadians(-loc.getYaw() - 90.0F);
			double pitch = Math.toRadians(-loc.getPitch());
			double[] spread = { 1.0D, 1.0D, 1.0D };
			for (int t = 0; t < 3; t++) {
				spread[t] = ((r.nextDouble() - r.nextDouble()) * accuracy * multi);
			}
			double x = Math.cos(pitch) * Math.cos(yaw) + spread[0];
			double y = Math.sin(pitch) + spread[1];
			double z = -Math.sin(yaw) * Math.cos(pitch) + spread[2];

			Vector dirVel = new Vector(x, y, z);
			bulletvector = dirVel.clone();
			AsyncThreadWorkers.submitSyncWork(() -> snowball.setVelocity(dirVel.multiply(speed)));
			EventBulletLaunchAsync event = new EventBulletLaunchAsync(instance);
			Bukkit.getPluginManager().callEvent(event);
		});
	}

	public double hit(Player enemy) {
		synchronized (this) {
			flying = false;
			this.hitlocation = AsyncThreadWorkers.getEntityLocation(snowball);
		}
		EventBulletHitPlayer eventBulletHit = new EventBulletHitPlayer(shooter, enemy, this);
		Bukkit.getPluginManager().callEvent(eventBulletHit);
		AsyncThreadWorkers.submitWork(() -> {
			EventBulletHitPlayerAsync eventBulletHitAsync = new EventBulletHitPlayerAsync(shooter, enemy, instance,
					eventBulletHit.isCancelled());
			Bukkit.getPluginManager().callEvent(eventBulletHitAsync);
		});

		if (eventBulletHit.isCancelled()) {
			removeBullet();
			return 0;
		}

		double damage = eventBulletHit.getDamage();

		if (damage > enemy.getHealth()) {
			damage = enemy.getHealth();
		}

		GamePlayer ggp = GamePlayer.getGamePlayer(enemy);
		ggp.setLastGunHit(gun.getGunInterface());
		ggp.setLastDamager(GamePlayer.getGamePlayer(shooter));
		removeBullet();
		return damage;
	}

	public void removeBullet() {
		synchronized (this) {
			if (!removed) {
				removed = true;
				flying = false;
				RemoveAble.addSec(() -> {
					synchronized (FLYING_BULLETS) {
						FLYING_BULLETS.remove(uuid);
					}
				}, 10);

				AsyncThreadWorkers.submitSyncWork(() -> snowball.remove());
			}
		}

	}

	public void setDamage(double damage) {
		synchronized (this) {
			this.damage = damage;
		}
	}

	public String getUUID() {
		synchronized (this) {
			return uuid;
		}
	}

	public Snowball getSnowball() {
		synchronized (this) {
			return snowball;
		}
	}

	public Player getShooter() {
		synchronized (this) {
			return shooter;
		}
	}

	public int getFlytime() {
		synchronized (this) {
			return flytime;
		}
	}

	public Location getLocFromPlayer() {
		synchronized (this) {
			return locfromplayer;
		}
	}

	public Location getCurrentBulletLocation() {
		synchronized (this) {
			return currentLocation.clone();
		}
	}

	public void setCurrentBulletLocation(Location loc) {
		synchronized (this) {
			currentLocation = loc;
		}
	}

	public Location getLocFromPlayerEye() {
		synchronized (this) {
			return locfromplayereye;
		}
	}

	public Location getHitLocation() {
		synchronized (this) {
			return hitlocation;
		}
	}

	public void setHitlocation(Location hitlocation) {
		synchronized (this) {
			this.hitlocation = hitlocation;
		}
	}

	public Vector getBulletVector() {
		synchronized (this) {
			return bulletvector;
		}
	}

	public void setBulletvector(Vector bulletvector) {
		synchronized (this) {
			this.bulletvector = bulletvector;
		}
	}

	public void setFlytime(int flytime) {
		synchronized (this) {
			this.flytime = flytime;
		}
	}
	
	public Gun getGun() {
		synchronized (this) {
			return gun;
		}
	}

	public double getDamage() {
		synchronized (this) {
			return damage;
		}
	}

	public boolean isFlying() {
		synchronized (this) {
			return flying;
		}
	}

	public static HashMap<String, Bullet> getBullets() {
		synchronized (FLYING_BULLETS) {
			return (HashMap<String, Bullet>) Bullet.FLYING_BULLETS.clone();
		}
	}

	public static List<Bullet> getBulletsAsList() {
		ArrayList<Bullet> bullets = new ArrayList<>();
		synchronized (FLYING_BULLETS) {
			FLYING_BULLETS.values().forEach(bul -> bullets.add(bul));
			return bullets;
		}
	}

	public static Bullet getBullet(final String uuid) {
		synchronized (FLYING_BULLETS) {
			return Bullet.FLYING_BULLETS.get(uuid);
		}
	}

	public static Bullet getBullet(final Entity entity) {
		synchronized (FLYING_BULLETS) {
			return Bullet.FLYING_BULLETS.get(entity.getUniqueId().toString());
		}
	}

	public static boolean isBulletFlying(final String uuid) {
		synchronized (FLYING_BULLETS) {
			return Bullet.FLYING_BULLETS.containsKey(uuid);
		}
	}

}
