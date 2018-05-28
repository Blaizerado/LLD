package at.ltd.gungame.guns.events.custom;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import at.ltd.gungame.guns.utils.bullet.Bullet;

public class EventBulletHitEntity extends Event {

	private static final HandlerList handlers = new HandlerList();

	private Player shooter;
	private Entity enemy;
	private Bullet bullet;
	private double damage;
	private boolean cancel = false;

	public EventBulletHitEntity(Player shooter, Entity enemy, Bullet bullet) {
		this.shooter = shooter;
		this.enemy = enemy;
		damage = bullet.getDamage();
		this.bullet = bullet;
	}

	public void setCancelled(boolean bool) {
		this.cancel = bool;
	}

	public boolean isCancelled() {
		return cancel;
	}

	public void setDamage(double damage) {
		bullet.setDamage(damage);
	}

	public Entity getEnemy() {
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
