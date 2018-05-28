package at.ltd.gungame.guns.utils;

import org.apache.commons.lang.StringUtils;

public class GunSerialNumber {

	public static String format = "[UID]:[GID]:[KILLS]:[BULLETS]";
	public static int radix = 36;

	public static String getString(GunInterface gunInterface, int kills, int bullets) {
		return getSerialNumber(gunInterface, kills, bullets);
	}

	private static int num = 0;

	private static String getSerialNumber(GunInterface gi, int kills, int bullets) {
		num++;

		String uid = Integer.toString(num, radix);
		String gid = Integer.toString(gi.getGunID(), radix);
		String UUID = format.replace("[UID]", uid);
		UUID = UUID.replace("[GID]", gid);
		UUID = UUID.replace("[KILLS]", Integer.toString(kills, radix));
		UUID = UUID.replace("[BULLETS]", Integer.toString(bullets, radix));
		return UUID;
	}

	public static String getSerialNumberWithNewData(String oldSerialID, GunInterface gi, int kills, int bullets) {
		String[] newsernum = new String[4];
		String gid = Integer.toString(gi.getGunID(), radix);
		String killsS = Integer.toString(kills, radix);
		String bulletsS = Integer.toString(bullets, radix);

		newsernum[0] = getUID(oldSerialID);
		newsernum[1] = gid;
		newsernum[2] = killsS;
		newsernum[3] = bulletsS;

		return StringUtils.join(newsernum, ':');
	}

	public static GunInterface getGunInterface(String serialID) {
		String[] splitedSerialID = serialID.split(":");
		return GunUtils.getGunInterfaceByID(Integer.valueOf(splitedSerialID[1], radix));
	}

	public static Gun getGun(String serialID) {
		if (!Gun.doesGunExist(serialID)) {
			return null;
		}
		return Gun.getGun(serialID);
	}

	public static boolean isGunGameItem(String serialID) {
		String[] splitedSerialID = serialID.split(":");
		int i = Integer.valueOf(splitedSerialID[2], radix);
		return i == -1;
	}

	public static int getKillsLeft(String serialID) {
		String[] splitedSerialID = serialID.split(":");
		return Integer.valueOf(splitedSerialID[2], radix);
	}

	public static int getBullets(String serialID) {
		String[] splitedSerialID = serialID.split(":");
		return Integer.valueOf(splitedSerialID[3], radix);
	}

	public static String getUID(String serialID) {
		String[] splitedSerialID = serialID.split(":");
		return splitedSerialID[0];
	}

}
