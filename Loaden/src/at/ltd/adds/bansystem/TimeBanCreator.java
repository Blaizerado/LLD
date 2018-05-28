package at.ltd.adds.bansystem;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import at.ltd.adds.bansystem.BanUtils.BanStatus;
import at.ltd.adds.bansystem.events.custom.EventBan;

public class TimeBanCreator {

	public static ConcurrentHashMap<Player, TimeBanCreator> CREATIONS = new ConcurrentHashMap<>();

	public static enum TIMEBAN_STEP {
		NAME, DAY, HOURS, MINUTES, REASON, CONFIRM
	};

	private int days = 0;
	private int hours = 0;
	private int minutes = 0;
	private String name;
	private String uuid;
	private String reason;
	private Player creator;
	private TIMEBAN_STEP step;

	public TimeBanCreator(Player creator) {
		this.creator = creator;
	}

	public TIMEBAN_STEP getStep() {
		return step;
	}

	public void setStep(TIMEBAN_STEP step) {
		this.step = step;
	}

	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public int getHours() {
		return hours;
	}
	public void setHours(int hours) {
		this.hours = hours;
	}
	public int getMinutes() {
		return minutes;
	}
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUUID() {
		return uuid;
	}
	public void setUUID(String uuid) {
		this.uuid = uuid;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public boolean createBan() {
		long time = BanTimeUtils.calculateTimeInMilis(minutes, hours, days);
		EventBan eb = new EventBan(creator, uuid, name, BanStatus.TIME_BANNED, time, true);
		Bukkit.getPluginManager().callEvent(eb);
		if (eb.isCancelled()) {
			return false;
		}

		BanSystem.tempBanPlayer(uuid, reason, time);
		return true;

	}

}
