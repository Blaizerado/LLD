package at.ltd.gungame.guns.RIFLES.SETUP;

import java.util.ArrayList;
import java.util.Collections;

import at.ltd.gungame.guns.RIFLES.ASSAULT_RIFLE.ACW_R;
import at.ltd.gungame.guns.RIFLES.ASSAULT_RIFLE.ACW_R_SNOW;
import at.ltd.gungame.guns.RIFLES.ASSAULT_RIFLE.AK_47;
import at.ltd.gungame.guns.RIFLES.ASSAULT_RIFLE.AK_47_GOLD;
import at.ltd.gungame.guns.RIFLES.ASSAULT_RIFLE.AUG_BIG;
import at.ltd.gungame.guns.RIFLES.ASSAULT_RIFLE.AUG_BLACK;
import at.ltd.gungame.guns.RIFLES.ASSAULT_RIFLE.AUG_GOLD;
import at.ltd.gungame.guns.RIFLES.ASSAULT_RIFLE.AUG_GREEN;
import at.ltd.gungame.guns.RIFLES.ASSAULT_RIFLE.G36C;
import at.ltd.gungame.guns.RIFLES.ASSAULT_RIFLE.G36C_CAMO;
import at.ltd.gungame.guns.RIFLES.ASSAULT_RIFLE.G36C_SNOW;
import at.ltd.gungame.guns.RIFLES.ASSAULT_RIFLE.M4A1;
import at.ltd.gungame.guns.RIFLES.ASSAULT_RIFLE.SCAR_H;
import at.ltd.gungame.guns.RIFLES.ASSAULT_RIFLE.SCAR_H_SNOW;
import at.ltd.gungame.guns.RIFLES.ASSAULT_RIFLE.STAN_MK3;
import at.ltd.gungame.guns.RIFLES.ASSAULT_RIFLE.STONER_63;
import at.ltd.gungame.guns.RIFLES.DMR.M14_EBR;
import at.ltd.gungame.guns.RIFLES.DMR.SVD;
import at.ltd.gungame.guns.RIFLES.DMR.SVD_GOLD;
import at.ltd.gungame.guns.RIFLES.MG.MG42;
import at.ltd.gungame.guns.RIFLES.MG.MINI_GUN;
import at.ltd.gungame.guns.RIFLES.MG.PPSH;
import at.ltd.gungame.guns.RIFLES.MG.RPD;
import at.ltd.gungame.guns.RIFLES.MG.RPD_GOLD;
import at.ltd.gungame.guns.RIFLES.PDW.MAC_10;
import at.ltd.gungame.guns.RIFLES.PDW.MAC_10_PAINTJOB;
import at.ltd.gungame.guns.RIFLES.PDW.MP5K;
import at.ltd.gungame.guns.RIFLES.PDW.MP7;
import at.ltd.gungame.guns.RIFLES.PDW.P90;
import at.ltd.gungame.guns.RIFLES.PISTOL.DESERT_EAGLE;
import at.ltd.gungame.guns.RIFLES.PISTOL.DESERT_EAGLE_BIG;
import at.ltd.gungame.guns.RIFLES.PISTOL.DESERT_EAGLE_GOLD;
import at.ltd.gungame.guns.RIFLES.PISTOL.GLOCK18;
import at.ltd.gungame.guns.RIFLES.PISTOL.M1911;
import at.ltd.gungame.guns.RIFLES.PISTOL.M9;
import at.ltd.gungame.guns.RIFLES.PISTOL.P250;
import at.ltd.gungame.guns.RIFLES.ROCKET_LAUNCHER.M72_LAW;
import at.ltd.gungame.guns.RIFLES.ROCKET_LAUNCHER.M79_THUMPER;
import at.ltd.gungame.guns.RIFLES.SHOTGUN.MODEL37;
import at.ltd.gungame.guns.RIFLES.SHOTGUN.QUADBARREL;
import at.ltd.gungame.guns.RIFLES.SHOTGUN.SAIGA;
import at.ltd.gungame.guns.RIFLES.SHOTGUN.SPASS12;
import at.ltd.gungame.guns.RIFLES.SNIPER.L96;
import at.ltd.gungame.guns.RIFLES.SNIPER.M24;
import at.ltd.gungame.guns.RIFLES.SNIPER.M24_CAMO;
import at.ltd.gungame.guns.utils.GunInterface;
import at.ltd.gungame.guns.utils.GunRegister;

public class RifleSetUp {

	public static ArrayList<Object> guns = new ArrayList<>();

	public static void setUp() {
		Collections.synchronizedMap(RifleConfigMemory.DATA);

		addGuns();
		RifleConfigReader.start();

		registerInRegestry();
		// guns.clear();
	}

	private static void addGuns() {
		// ASSAULT_RIFLE
		guns.add(new ACW_R_SNOW());
		guns.add(new ACW_R());
		guns.add(new AK_47());
		guns.add(new AK_47_GOLD());
		guns.add(new AUG_BIG());
		guns.add(new AUG_BLACK());
		guns.add(new AUG_GOLD());
		guns.add(new AUG_GREEN());
		guns.add(new G36C());
		guns.add(new G36C_CAMO());
		guns.add(new G36C_SNOW());
		guns.add(new M4A1());
		guns.add(new SCAR_H());
		guns.add(new SCAR_H_SNOW());

		guns.add(new STAN_MK3());
		guns.add(new STONER_63());

		// DMR
		guns.add(new M14_EBR());
		guns.add(new SVD_GOLD());
		guns.add(new SVD());
		// MG
		guns.add(new MG42());
		guns.add(new PPSH());
		guns.add(new RPD());
		guns.add(new RPD_GOLD());
		guns.add(new MINI_GUN());
		// PDW
		guns.add(new MAC_10_PAINTJOB());
		guns.add(new MAC_10());
		guns.add(new MP5K());
		guns.add(new P90());
		guns.add(new MP7());
		// PISTOL
		guns.add(new DESERT_EAGLE());
		guns.add(new DESERT_EAGLE_BIG());
		guns.add(new DESERT_EAGLE_GOLD());
		guns.add(new GLOCK18());
		guns.add(new M9());
		guns.add(new P250());
		guns.add(new M1911());
		// ROCKET_LOUNCHER
		guns.add(new M72_LAW());
		guns.add(new M79_THUMPER());
		// SHOTGUN
		guns.add(new MODEL37());
		guns.add(new SPASS12());
		guns.add(new QUADBARREL());
		guns.add(new SAIGA());
		// SNIPER
		guns.add(new L96());
		guns.add(new M24_CAMO());
		guns.add(new M24());
	}

	public static void registerInRegestry() {
		for (Object ob : guns) {
			GunInterface gi = (GunInterface) ob;
			GunRegister.addGun(gi);
		}
	}

}
