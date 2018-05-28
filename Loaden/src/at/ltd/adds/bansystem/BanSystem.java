package at.ltd.adds.bansystem;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.bansystem.events.BanCreateChatEvent;
import at.ltd.adds.bansystem.events.BanListener;
import at.ltd.adds.bansystem.events.JoinEvent;
import at.ltd.adds.sql.SQLQuery;
import at.ltd.adds.sql.sqlutils.SQLCollection;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;

public class BanSystem {

	public static String p = "§7[§8BS§6FLARGO§7] ";

	public static void init() {
		JoinEvent.time = System.currentTimeMillis();
		Bukkit.getPluginManager().registerEvents(new BanCreateChatEvent(), Main.getPlugin());
		Bukkit.getPluginManager().registerEvents(new JoinEvent(), Main.getPlugin());
		Bukkit.getPluginManager().registerEvents(new TimeBan(), Main.getPlugin());
		Main.registerListener(new BanListener());
	}

	public static void banPlayer(String uuid, String reason) {

		AsyncThreadWorkers.submitWork(() -> {
			boolean isonline = AsyncThreadWorkers.isPlayerOnline(uuid);
			if (isonline) {
				SQLCollection.getPlayer(uuid).setBanned(true);
			} else {
				setBanned(uuid);
			}

			String rs = null;
			if (reason == null) {
				rs = Cf.rs(Cf.BAN_PERMANENTLY_NO_REASON);
			} else {
				rs = Cf.rs(Cf.BAN_PERMANENTLY, "[REASON]", reason);
			}

			if (isonline) {
				String finalreason = rs;
				AsyncThreadWorkers.submitSyncWork(() -> {
					Player p = Bukkit.getPlayer(UUID.fromString(uuid));
					p.kickPlayer(finalreason);

				});

			}

			if (reason == null) {
				BanUtils.removeBanReson(uuid);
			} else {
				BanUtils.addBanReson(reason, uuid);
			}
		});
	}

	public static void banPlayer(Player p, String reason) {
		banPlayer(p.getUniqueId().toString(), reason);
	}

	public static void tempBanPlayer(String uuid, String reason, long timeMilis) {
		final long time = BanTimeUtils.addCurrentTime(timeMilis);
		AsyncThreadWorkers.submitWork(() -> {

			boolean isonline = AsyncThreadWorkers.isPlayerOnline(uuid);
			if (isonline) {
				SQLCollection.getPlayer(uuid).setTimebantime(time);
			} else {
				setTimeBanned(uuid, time);
			}
			String rs = null;
			if (reason == null) {
				rs = Cf.rs(Cf.BAN_TEMPORARILY_NO_REASON, "[TIME]", BanTimeUtils.getTime(time));
			} else {
				rs = Cf.rs(Cf.BAN_TEMPORARILY, "[REASON]", reason, "[TIME]", BanTimeUtils.getTime(time));
			}

			if (isonline) {
				String finalreason = rs;
				AsyncThreadWorkers.submitSyncWork(() -> {
					Player p = Bukkit.getPlayer(UUID.fromString(uuid));
					p.kickPlayer(finalreason);
				});

			}

			try {
				if (reason == null) {
					BanUtils.removeBanReson(uuid);
				} else {
					BanUtils.addBanReson(reason, uuid);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}

	public static void tempBanPlayer(Player player, String reason, long timeMilis) {
		tempBanPlayer(player.getUniqueId().toString(), reason, timeMilis);
	}

	public static void unbanPlayer(String uuid) {
		AsyncThreadWorkers.submitWork(() -> {
			SQLQuery query = Main.getSQLQuery();
			try {
				query.updateSQL("UPDATE `MC` SET `BANNED` = '0' WHERE `MC`.`UUID` = '" + uuid + "';");
				query.updateSQL("UPDATE `MC` SET `TIMEBANTIME` = '0' WHERE `MC`.`UUID` = '" + uuid + "';");
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}

	public static void unbanPlayer(Player player) {
		unbanPlayer(player.getUniqueId().toString());
	}

	public static String getReason(String uuid) {
		return BanUtils.getBanReason(uuid);
	}

	public static String getReason(Player player) {
		return getReason(player.getUniqueId().toString());
	}

	private static void setBanned(String uuid) {
		try {
			Main.createSQLQuery().updateSQL("UPDATE `MC` SET `BANNED` = 1 WHERE `UUID` LIKE \"" + uuid + "\"");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void setTimeBanned(String uuid, long time) {
		try {
			Main.createSQLQuery().updateSQL("UPDATE `MC` SET `TIMEBANTIME` = " + time + " WHERE `UUID` LIKE \"" + uuid + "\"");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
