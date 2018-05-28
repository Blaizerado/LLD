package at.ltd.gungame.guns.events.custom;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import at.ltd.gungame.guns.utils.Gun;

public class EventGunShootAsync extends Event {

	private static final HandlerList handlers = new HandlerList();

	private Player player;
	private Gun gun;
	private boolean wascancelled;
	private float bulletAccuracy;
	private float bulletSpeed;
	private float volume;
	private int bulletsPerTick;
	private int bulletflytime;
	private double damage;
	private Sound shootSound;

	public EventGunShootAsync(Player p, EventGunShoot egs) {
		this.player = p;
		this.gun = egs.getGun();
		bulletAccuracy = egs.getBulletAccuracy();
		bulletSpeed = egs.getBulletSpeed();
		bulletsPerTick = egs.getBulletsPerTick();
		shootSound = egs.getShootSound();
		volume = egs.getVolume();
		bulletflytime = egs.getBulletFlyTime();
		damage = egs.getDamage();
		wascancelled = egs.isCancelled();
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;

	}

	public boolean wasCancelled() {
		return wascancelled;
	}

	public Gun getGun() {
		return gun;
	}

	public float getBulletAccuracy() {
		return bulletAccuracy;
	}

	public float getBulletSpeed() {
		return bulletSpeed;
	}

	public int getBulletsPerTick() {
		return bulletsPerTick;
	}

	public Sound getShootSound() {
		return shootSound;
	}

	public float getVolume() {
		return volume;
	}

	public int getBulletFlyTime() {
		return bulletflytime;
	}

	public double getDamage() {
		return damage;
	}

}
