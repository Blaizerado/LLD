package at.ltd.gungame.guns.events.custom;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import at.ltd.gungame.guns.utils.Gun;
import at.ltd.gungame.guns.utils.GunInterface;

public class EventGunShoot extends Event {

	private static final HandlerList handlers = new HandlerList();

	private Player player;
	private Gun gun;
	private boolean iscancelled = false;
	private float bulletAccuracy;
	private float bulletSpeed;
	private float volume;
	private int bulletsPerTick;
	private int bulletflytime;
	private int firespeed;
	private double damage;
	private Sound shootSound;

	public EventGunShoot(Player p, Gun gun) {
		this.player = p;
		this.gun = gun;
		GunInterface gi = gun.getGunInterface();
		bulletAccuracy = gi.getBulletAccuracy();
		bulletSpeed = gi.getBulletSpeed();
		bulletsPerTick = gi.getAmmountOfBulletsPerTick();
		shootSound = gi.getSound();
		volume = gi.getVolume();
		bulletflytime = gi.getBulletFlyTime();
		damage = gi.getDamage();
		firespeed = gi.getFireSpeed();
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

	public void setCancelled(boolean bool) {
		this.iscancelled = bool;
	}

	public boolean isCancelled() {
		return iscancelled;
	}

	public Gun getGun() {
		return gun;
	}

	public float getBulletAccuracy() {
		return bulletAccuracy;
	}

	public void setBulletAccuracy(float bulletAccuracy) {
		this.bulletAccuracy = bulletAccuracy;
	}

	public float getBulletSpeed() {
		return bulletSpeed;
	}

	public void setBulletSpeed(float bulletSpeed) {
		this.bulletSpeed = bulletSpeed;
	}

	public int getBulletsPerTick() {
		return bulletsPerTick;
	}

	public void setBulletsPerTick(int bulletsPerTick) {
		this.bulletsPerTick = bulletsPerTick;
	}

	public Sound getShootSound() {
		return shootSound;
	}

	public void setShootSound(Sound shootSound) {
		this.shootSound = shootSound;
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	public int getBulletFlyTime() {
		return bulletflytime;
	}

	public void setBulletFlyTime(int bulletflytime) {
		this.bulletflytime = bulletflytime;
	}

	public double getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getFirespeed() {
		return firespeed;
	}

	public void setFirespeed(int firespeed) {
		this.firespeed = firespeed;
	}

	
	

}
