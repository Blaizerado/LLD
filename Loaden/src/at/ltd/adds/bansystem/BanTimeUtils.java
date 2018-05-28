package at.ltd.adds.bansystem;

import java.sql.ResultSet;
import java.util.concurrent.TimeUnit;

import at.ltd.Main;
import at.ltd.adds.sql.SQLQuery;

public class BanTimeUtils {

	public static long calculateTimeInMilis(int min, int hour, int day) {
		long min_mili = TimeUnit.MINUTES.toMillis(min);
		long hour_mili = TimeUnit.HOURS.toMillis(hour);
		long day_mili = TimeUnit.DAYS.toMillis(day);
		long time = min_mili + hour_mili + day_mili;
		return time;
	}
	public static long addCurrentTime(long timebantime) {
		return System.currentTimeMillis() + timebantime;
	}

	public static long calculateBanDurationMilis(long timebantime) {
		return timebantime - System.currentTimeMillis();
	}

	public static boolean isTimeBanGone(long bantime) {
		if (bantime < System.currentTimeMillis()) {
			return true;
		} else {
			return false;
		}
	}

	public static String getTime(long bantime) {
		long time = calculateBanDurationMilis(bantime);
		long seconds = time / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		long days = hours / 24;
		String time2 = "Days: '" + days + "', Hours: '" + hours % 24 + "', Minutes: '" + minutes % 60 + "', Seconds: '" + seconds % 60;
		return time2;
	}

	public static long getBanTime(String uuid) {
		SQLQuery query = Main.getSQLQuery();
		try {
			ResultSet rs = query.querySQL("SELECT * FROM `MC` WHERE `UUID` LIKE '" + uuid + "'");
			if (rs.next()) {
				return rs.getLong("TIMEBANTIME");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return System.currentTimeMillis();
	}

}
