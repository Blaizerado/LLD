package at.ltd.adds.bansystem;

import java.sql.ResultSet;

import org.bukkit.ChatColor;

import at.ltd.Main;
import at.ltd.adds.sql.SQLQuery;

public class BanUtils {

	public static enum BanStatus {
		TIME_BANNED, BANNED, NO_BAN
	};

	public static String getBanReason(String uuid) {
		try {
			SQLQuery query = Main.getSQLQuery();
			ResultSet rs = query.querySQL("SELECT * FROM `BANREASON` WHERE `UUID` LIKE '" + uuid + "'");
			if (rs.next()) {
				return ChatColor.stripColor(rs.getString("REASON"));
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void addBanReson(String reason, String uuid) {
		try {
			SQLQuery query = Main.getSQLQuery();
			query.updateSQL("REPLACE INTO `BANREASON` (`UUID`, `REASON`) VALUES ('" + uuid + "', '" + reason + "')");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void removeBanReson(String uuid) {
		try {
			SQLQuery query = Main.getSQLQuery();
			query.updateSQL("DELETE FROM `BANREASON` WHERE UUID = '" + uuid + "'");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static BanStatus getBanStatus(String uuid) {
		SQLQuery query = Main.getSQLQuery();
		try {
			ResultSet rs = query.querySQL("SELECT * FROM `MC` WHERE `UUID` LIKE '" + uuid + "'");
			if (rs.next()) {
				if (rs.getBoolean("BANNED")) {
					return BanStatus.BANNED;
				}
				long time = rs.getLong("TIMEBANTIME");
				if (!BanTimeUtils.isTimeBanGone(time)) {
					return BanStatus.TIME_BANNED;
				} else {
					if (time != 0) {
						query.updateSQL("UPDATE `MC` SET `TIMEBANTIME` = '0' WHERE `MC`.`UUID` = '" + uuid + "'");
					}
				}
			} else {
				return BanStatus.NO_BAN;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return BanStatus.NO_BAN;

	}

	public static String getUUIDFromName(String name) {
		SQLQuery query = Main.getSQLQuery();
		try {
			ResultSet rs = query.querySQL("SELECT * FROM `MC` WHERE LOWER(`NAME`) LIKE LOWER('" + name + "')");
			if (rs.next()) {
				return rs.getString("UUID");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getNameFromUUID(String uuid) {
		SQLQuery query = Main.getSQLQuery();
		try {
			ResultSet rs = query.querySQL("SELECT * FROM `MC` WHERE `UUID` LIKE '" + uuid + "'");
			if (rs.next()) {
				return rs.getString("NAME");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean doesUUIDExist(String uuid) {
		SQLQuery query = Main.getSQLQuery();
		try {
			ResultSet rs = query.querySQL("SELECT * FROM `MC` WHERE `UUID` LIKE '" + uuid + "'");
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String getUUIDFromBanner(String banneduuid) {
		SQLQuery query = Main.createSQLQuery();
		try {
			ResultSet rs = query.querySQL("SELECT * FROM `BAN_LOG` WHERE `TARGET` LIKE '" + banneduuid + "'");
			if (rs.next()) {
				return rs.getString("BANNER");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
