package at.ltd.adds.bansystem.events.custom;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import at.ltd.adds.bansystem.BanUtils.BanStatus;

public class EventBan extends Event {

	private final static HandlerList handlers = new HandlerList();
	private Player banner;
	private String toBeBannedUUID;
	private String toBeBannedNAME;
	private BanStatus banstatus;
	private long time;
	private boolean cancelled = false;
	private boolean offlineban = false;;

	public EventBan(Player banner, String uuid, String name, BanStatus banStatus, Long time, boolean offlineban) {
		this.time = time;
		this.banstatus = banStatus;
		this.toBeBannedNAME = name;
		this.toBeBannedUUID = uuid;
		this.banner = banner;
		this.offlineban = offlineban;
	}

	public boolean isOfflineban() {
		return offlineban;
	}
	public Player getBanner() {
		return banner;
	}

	public String getToBeBannedUUID() {
		return toBeBannedUUID;
	}

	public String getToBeBannedNAME() {
		return toBeBannedNAME;
	}

	public BanStatus getBanstatus() {
		return banstatus;
	}
	public long getTimeBanTime() {
		return time;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;

	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean c) {
		cancelled = c;
	}

}
