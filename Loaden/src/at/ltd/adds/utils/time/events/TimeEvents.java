package at.ltd.adds.utils.time.events;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import at.ltd.Main;
import at.ltd.adds.utils.config.ConfigManager;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;

public class TimeEvents {

	public static final String TIME_FILE = "plugins/LTD/Time.txt";

	public static void init() {
		AsyncThreadWorkers.submitSchedulerWorkSec(() -> {
			check();
		}, 30, 30);
	}

	private synchronized static void check() {
		System.out.println("Check 1");
		String month = getCurrentDateMonth();
		String day = getCurrentDateDay();
		String hour = getCurrentDateHour();

		HashMap<String, String> times = ConfigManager.readConfig(TIME_FILE);

		if (!times.containsKey("M")) {
			times.put("M", month);
		}

		if (!times.containsKey("D")) {
			times.put("D", day);
		}

		if (!times.containsKey("H")) {
			times.put("H", hour);
		}

		if (!times.get("M").equals(month)) {
			times.remove("M");
			AsyncThreadWorkers.submitSyncWork(() -> {
				Main.callEvent(new EventMonth());
			});
		}

		if (!times.get("D").equals(day)) {
			times.remove("D");
			AsyncThreadWorkers.submitSyncWork(() -> {
				Main.callEvent(new EventDay());
			});
		}

		if (!times.get("H").equals(hour)) {
			times.remove("H");
			AsyncThreadWorkers.submitSyncWork(() -> {
				Main.callEvent(new EventHour());
			});
		}
		times.clear();
		times.put("M", month);
		times.put("D", day);
		times.put("H", hour);
		ConfigManager.setConfig(times, TIME_FILE);
	}

	public static String getCurrentDateMonth() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM");
		Date currentTime = new Date();
		return formatter.format(currentTime);
	}

	public static String getCurrentDateDay() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.DD");
		Date currentTime = new Date();
		return formatter.format(currentTime);
	}

	public static String getCurrentDateHour() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.DD.HH");
		Date currentTime = new Date();
		return formatter.format(currentTime);
	}

}
