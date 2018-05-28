package at.ltd.gungame.guns.events.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import at.ltd.gungame.guns.utils.bullet.Bullet;

public class EventBulletHitPlayerAsync extends Event {

	private static final HandlerList handlers = new HandlerList();

	private Player shooter;
	private Player enemy;
	private Bullet bullet;
	private double damage;
	private boolean cancelled;

	public EventBulletHitPlayerAsync(Player shooter, Player enemy, Bullet bullet, boolean wascancelled) {
		this.shooter = shooter;
		this.enemy = enemy;
		this.damage = bullet.getDamage();
		this.bullet = bullet;
		this.cancelled = wascancelled;
	}

	public boolean wasCancelled() {
		return cancelled;
	}

	public Player getEnemy() {
		return enemy;
	}

	public Player getShooter() {
		return shooter;
	}

	public Bullet getBullet() {
		return bullet;
	}

	public double getDamage() {
		return damage;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;

	}

}
