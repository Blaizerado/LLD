package at.ltd.gungame.guns.events.custom;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.ProjectileHitEvent;

public class EventProjectileHitAsync extends Event {

	private static final HandlerList handlers = new HandlerList();

	private Projectile entity;
	private Block hitblock;
	private Entity hitEntity;
	private EntityType entityType;
	private String uuid;
	private Location loc;
	public EventProjectileHitAsync(ProjectileHitEvent phe, Location loc) {
		this.entity = phe.getEntity();
		this.hitblock = phe.getHitBlock();
		this.entityType = phe.getEntityType();
		this.hitEntity = phe.getHitEntity();
		this.loc = loc;
		this.uuid = entity.getUniqueId().toString();
	}

	public Projectile getEntity() {
		return entity;
	}

	public Block getHitBlock() {
		return hitblock;
	}

	public Entity getHitEntity() {
		return hitEntity;
	}

	public EntityType getEntityType() {
		return entityType;
	}

	public String getUUID() {
		return uuid;
	}

	public Location getLocation() {
		return loc;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;

	}

}
