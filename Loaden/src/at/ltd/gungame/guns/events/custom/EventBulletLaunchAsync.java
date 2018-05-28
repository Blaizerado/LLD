package at.ltd.gungame.guns.events.custom;

import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import at.ltd.gungame.guns.utils.Gun;
import at.ltd.gungame.guns.utils.bullet.Bullet;

public class EventBulletLaunchAsync extends Event {

	private final static HandlerList handlers = new HandlerList();
	private Bullet bullet;
	private Player player;
	private Snowball snowball;
	private Gun gun;
	public EventBulletLaunchAsync(Bullet bull) {
		this.bullet = bull;
		this.player = bull.getShooter();
		this.snowball = bull.getSnowball();
		this.gun = bull.getGun();
	}

	public Bullet getBullet() {
		return bullet;
	}

	public Player getPlayer() {
		return player;
	}

	public Snowball getSnowball() {
		return snowball;
	}

	public Gun getGun() {
		return gun;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;

	}

}
