package at.ltd.gungame.guns.utils;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.inventory.ItemStack;

import at.ltd.Main;
import at.ltd.adds.game.player.GamePlayerDamage;
import at.ltd.adds.utils.serializer.ItemSerializer;
import at.ltd.gungame.guns.RIFLES.SETUP.RifleSetUp;
import at.ltd.gungame.guns.events.AccuracyMoveEvent;
import at.ltd.gungame.guns.events.AkimboWatcherEvent;
import at.ltd.gungame.guns.events.AsyncCallEvent;
import at.ltd.gungame.guns.events.BulletEffectEvent;
import at.ltd.gungame.guns.events.BulletRemoveEvent;
import at.ltd.gungame.guns.events.GunDurabilityLooseEvent;
import at.ltd.gungame.guns.events.GunShootTimingsEvent;
import at.ltd.gungame.guns.events.GunUserHandlerEvent;
import at.ltd.gungame.guns.events.InstantHitWatcher;
import at.ltd.gungame.guns.events.MouseKlickEvent;
import at.ltd.gungame.guns.events.ShootBlockEvent;
import at.ltd.gungame.guns.events.ShootEvent;
import at.ltd.gungame.guns.events.ShootReloadEvent;
import at.ltd.gungame.guns.utils.bullet.BulletUtils;
import at.ltd.gungame.guns.utils.bullet.BulletWatcher;
import at.ltd.gungame.guns.utils.rocket.RocketLauncherManager;
import at.ltd.gungame.guns.utils.rocket.RocketUtils;

public class GunRegister {

	public static HashMap<Integer, GunInterface> GUN_LIST = new HashMap<>();
	public static ConcurrentHashMap<String, GunInterface> GUN_LIST_STRING = new ConcurrentHashMap<>();

	public static void initGuns() {

		RifleSetUp.setUp();

		registerEvents();

		MouseKlickEvent.startTrigger();
		BulletUtils.init();
		GunRegisterWatcher.init();
		GunActionBarInfo.init();
		AkimboWatcherEvent.init();
		Movement.init();
		AccuracyMoveEvent.init();
		RocketUtils.init();
		GamePlayerDamage.init();
	}

	public static void registerEvents() {

		Main.registerListener(new MouseKlickEvent());
		Main.registerListener(new BulletWatcher());
		Main.registerListener(new ShootEvent());
		Main.registerListener(new GunRegisterWatcher());
		Main.registerListener(new GunShootTimingsEvent());
		Main.registerListener(new ShootReloadEvent().init());
		Main.registerListener(new AkimboWatcherEvent());
		Main.registerListener(new GunDurabilityLooseEvent());
		Main.registerListener(new BulletEffectEvent());
		Main.registerListener(new BulletRemoveEvent());
		Main.registerListener(new AsyncCallEvent());
		Main.registerListener(new InstantHitWatcher());
		Main.registerListener(new AccuracyMoveEvent());
		Main.registerListener(new RocketLauncherManager());
		Main.registerListener(new ShootBlockEvent());
		Main.registerListener(new GunUserHandlerEvent());


	}

	public static void addGun(GunInterface gun) {
		GUN_LIST.put(gun.getGunID(), gun);
		GUN_LIST_STRING.put(createWeaponString(gun.getItem()), gun);
		System.out.println("Registrating Gun... INFO= NAME:'" + gun.getName() + "', ID:'" + gun.getGunID() + "', ITEM: '" + ItemSerializer.serialize(gun.getItem()) + "'.");
	}

	public static GunInterface getGun(int i) {
		return GUN_LIST.get(i);
	}

	public static synchronized String createWeaponString(ItemStack is) {
		return is.getType().name() + "-" + is.getDurability();
	}

}
