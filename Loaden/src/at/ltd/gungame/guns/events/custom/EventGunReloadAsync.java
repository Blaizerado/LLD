package at.ltd.gungame.guns.events.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import at.ltd.gungame.guns.utils.Gun;

public class EventGunReloadAsync extends Event {

	private static final HandlerList handlers = new HandlerList();

	private Player player;
	private Gun GUN;
	
	public EventGunReloadAsync(Gun gun) {
		this.player = gun.getPlayer();
		this.GUN = gun;
	}

	public Player getPlayer() {
		return player;
	}

	public Gun getGun() {
		return this.GUN;
	}
	
	public void setReloadTime(int time) {
		GUN.setReloadTime(time);
	}
	
	public int getReloadTime() {
		return GUN.getReloadTime();
	}

	public String getUUID() {
		return GUN.getUUID();
	}

	public EventGunReloadAsync setGun(Gun gun) {
		this.GUN = gun;
		return this;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;

	}

}
