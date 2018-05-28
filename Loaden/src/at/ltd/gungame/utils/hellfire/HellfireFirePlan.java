package at.ltd.gungame.utils.hellfire;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Location;

import at.ltd.adds.utils.LocUtils;

public class HellfireFirePlan {

	public static String build(Location from, Location to, int heigh) {
		String[] ar = new String[3];
		ar[0] = LocUtils.locationToString(from);
		ar[1] = LocUtils.locationToString(to);
		ar[2] = heigh + "";
		return StringUtils.join(ar, ">>");
	}

	public static Location getFrom(String plan) {
		String[] ar = plan.split(">>");
		return LocUtils.locationByString(ar[0]);
	}

	public static Location getTo(String plan) {
		String[] ar = plan.split(">>");
		return LocUtils.locationByString(ar[1]);
	}
	public static int getHeigh(String plan) {
		String[] ar = plan.split(">>");
		return Integer.valueOf(ar[2]);
	}

	public static HellfireRocket fireRocket(String plan) {
		HellfireRocket hr = new HellfireRocket(getFrom(plan), getTo(plan), getHeigh(plan), null);
		hr.fire();
		return hr;
	}

}
