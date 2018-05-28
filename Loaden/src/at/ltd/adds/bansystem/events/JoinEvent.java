package at.ltd.adds.bansystem.events;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import at.ltd.adds.Cf;
import at.ltd.adds.bansystem.BanSystem;
import at.ltd.adds.bansystem.BanTimeUtils;
import at.ltd.adds.bansystem.BanUtils;
import at.ltd.adds.bansystem.BanUtils.BanStatus;
import at.ltd.adds.bansystem.events.custom.EventPlayerPreJoinAsync;

public class JoinEvent implements Listener {

	public static long time;
	public static long timetowait = TimeUnit.SECONDS.toMillis(7);

	@EventHandler
	public void on(AsyncPlayerPreLoginEvent e) {
		String uuid = e.getUniqueId().toString();

		if ((System.currentTimeMillis() - time) < timetowait) {
			e.disallow(Result.KICK_OTHER, Cf.SERVER_START);
			return;
		}

		BanStatus bs = BanUtils.getBanStatus(uuid);

		if (bs == BanStatus.NO_BAN) {
			e.allow();
			EventPlayerPreJoinAsync eppja = new EventPlayerPreJoinAsync(e.getUniqueId());
			Bukkit.getPluginManager().callEvent(eppja);
			return;
		}

		if (bs == BanStatus.BANNED) {
			String reason = BanSystem.getReason(uuid);
			if (reason == null) {
				e.disallow(Result.KICK_BANNED, Cf.rs(Cf.BAN_PERMANENTLY_NO_REASON));
			} else {
				e.disallow(Result.KICK_BANNED, Cf.rs(Cf.BAN_PERMANENTLY, "[REASON]", reason));
			}
		}

		if (bs == BanStatus.TIME_BANNED) {
			String reason = BanSystem.getReason(uuid);
			long time = BanTimeUtils.getBanTime(uuid);
			if (reason == null) {
				e.disallow(Result.KICK_BANNED, Cf.rs(Cf.BAN_TEMPORARILY_NO_REASON, "[TIME]", BanTimeUtils.getTime(time)));
			} else {
				e.disallow(Result.KICK_BANNED, Cf.rs(Cf.BAN_TEMPORARILY, "[REASON]", reason, "[TIME]", BanTimeUtils.getTime(time)));
			}

		}
	}

}
