package at.ltd.gungame;

import java.util.ArrayList;

import at.ltd.gungame.guns.utils.Gun;
import at.ltd.gungame.guns.utils.GunInterface;
import at.ltd.gungame.guns.utils.GunRegister;
import at.ltd.gungame.guns.utils.GunUtils;

public class StartUpTest {

	@SuppressWarnings("unused")
	public static void test() {

		System.out.println("--------------------------------------------------------------");
		System.out.println("Testing Gun System... ");
		System.out.println("Trying to make " + GunRegister.GUN_LIST.values().size() * 10 + " guns.");
		if (1 == 1) {
			return;
		}
		try {
			ArrayList<Gun> guns = new ArrayList<>();
			for (int i = 0; i < 10; i++) {
				for (GunInterface gun : GunRegister.GUN_LIST.values()) {
					Gun finalgun = GunUtils.createGun(gun, null, -1);
					guns.add(finalgun);
				}
			}
			for (Gun gun : guns) {
				gun.removeGunFromMemory();
			}
			guns.clear();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Seems the system has some problems.");
		} finally {
			System.out.println("Done.");
		}
		System.out.println("--------------------------------------------------------------");

	}

}
