package at.ltd.gungame.events.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventGunGameDamage extends Event {

	public enum DamageType {
		GUN_HIT, VOID, PLAYER_DAMAGE, GRANADE, HELLFIRE, THROWABLE_AXE
	}

	private final static HandlerList handlers = new HandlerList();
	private Player damagee;
	private double damage;
	private Player damager;
	private boolean cancelled = false;
	private DamageType damageType;

	public EventGunGameDamage(Player damagee, Player damager, double damage, DamageType damageType) {
		this.damagee = damagee;
		this.damager = damager;
		this.damage = damage;
		this.damageType = damageType;
	}

	public double getDamage() {
		return damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public Player getDamagee() {
		return damagee;
	}

	public Player getDamager() {
		return damager;
	}

	public DamageType getDamageType() {
		return damageType;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;

	}

}
