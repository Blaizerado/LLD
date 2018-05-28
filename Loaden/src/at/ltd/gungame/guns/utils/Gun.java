package at.ltd.gungame.guns.utils;

import java.util.HashMap;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import at.ltd.gungame.guns.utils.bullet.Bullet;

public class Gun {

	// STATIC CONTENT
	private static HashMap<String, Gun> GUN_MEMORY_LIST = new HashMap<>();

	public static Gun getGun(String uuid) {
		synchronized (GUN_MEMORY_LIST) {
			return GUN_MEMORY_LIST.get(uuid);
		}
	}

	public static Gun addGun(String uuid, Gun gun) {
		synchronized (GUN_MEMORY_LIST) {
			GUN_MEMORY_LIST.put(uuid, gun);
			return gun;
		}
	}

	public static void removeGun(String uuid) {
		synchronized (GUN_MEMORY_LIST) {
			GUN_MEMORY_LIST.remove(uuid);
		}

	}

	public static boolean doesGunExist(String uuid) {
		synchronized (GUN_MEMORY_LIST) {
			return GUN_MEMORY_LIST.containsKey(uuid);
		}
	}

	public static HashMap<String, Gun> getGunList() {
		synchronized (GUN_MEMORY_LIST) {
			return (HashMap<String, Gun>) GUN_MEMORY_LIST.clone();
		}
	}

	// STATIC CONTENT

	// GUN_DATA
	private String UUID;
	private GunInterface GUN;
	private Player p;
	private ItemStack ITEM_GUN;
	private int AMMO = 0;
	private boolean IS_RELOADING = false;
	private boolean GUNGAME_ITEM = false;
	private int KILLS_LEFT = -1;
	private long LAST_SHOOT = System.currentTimeMillis();
	private int RELOAD_TIME = 0;
	// GUN_DATA

	public Gun(GunInterface gi, ItemStack is, int kills, int bullets, Player p) {
		if (kills == 0) {
			kills = -1;
		}
		this.UUID = GunSerialNumber.getString(gi, kills, bullets);
		this.GUN = gi;
		this.p = p;
		this.ITEM_GUN = is;
		this.GUNGAME_ITEM = kills == -1;
		this.KILLS_LEFT = kills;
		this.AMMO = bullets;
		this.RELOAD_TIME = gi.getReloadTime();

	}

	public Gun registerGun() {
		synchronized (GUN_MEMORY_LIST) {
			if (!GUN_MEMORY_LIST.containsKey(UUID)) {
				GUN_MEMORY_LIST.put(UUID, this);
				return this;
			}
			return null;
		}
	}

	public String getUUID() {
		return UUID;
	}

	public GunInterface getGunInterface() {
		return GUN;
	}

	public Player getPlayer() {
		return p;
	}

	public boolean isReloading() {
		return IS_RELOADING;
	}

	public Gun setReloadingStatus(boolean isreloading) {
		this.IS_RELOADING = isreloading;
		return this;
	}

	public ItemStack getItemStack() {
		return ITEM_GUN;
	}

	public Gun setItemStack(ItemStack itemStack) {
		this.ITEM_GUN = itemStack;
		return this;
	}

	public void removeGunFromMemory() {
		updateSerialNumberData();
		synchronized (GUN_MEMORY_LIST) {
			GUN_MEMORY_LIST.remove(UUID);
		}
	}

	public void updateSerialNumberData() {
		String newuuid = GunSerialNumber.getSerialNumberWithNewData(UUID, GUN, KILLS_LEFT, AMMO);
		synchronized (GUN_MEMORY_LIST) {
			GUN_MEMORY_LIST.remove(UUID);
			this.UUID = newuuid;
			GUN_MEMORY_LIST.put(UUID, this);
		}
		GunUtils.setLore(ITEM_GUN, newuuid);
	}

	public ItemStack updateSerialNumberData(ItemStack gun) {
		String newuuid = GunSerialNumber.getSerialNumberWithNewData(UUID, GUN, KILLS_LEFT, AMMO);
		Gun.removeGun(UUID);
		this.UUID = newuuid;
		Gun.addGun(UUID, this);
		GunUtils.setLore(gun, newuuid);
		return gun;
	}

	public boolean isGunGameItem() {
		return GUNGAME_ITEM;
	}

	public int getKillsLeft() {
		return KILLS_LEFT;
	}

	public Gun setKillsLeft(int kills) {
		KILLS_LEFT = kills;
		return this;
	}

	public Gun gotKill(Player killed) {
		if (KILLS_LEFT == -1) {
			return this;
		}

		if (KILLS_LEFT == 1) {
			KILLS_LEFT = 0;
			GunUtils.removeGunFromInv(this);
			removeGunFromMemory();
		} else {
			KILLS_LEFT--;
		}
		return this;

	}

	public long getLastShootTime() {
		return LAST_SHOOT;
	}

	public Gun setLastShootTime(long lastShoot) {
		LAST_SHOOT = lastShoot;
		return this;
	}

	public int getReloadTime() {
		return RELOAD_TIME;
	}

	public Gun setReloadTime(int time) {
		this.RELOAD_TIME = time;
		return this;
	}

	public int getAmmoLeft() {
		return AMMO;
	}

	public Gun setAmmoLeft(int ammo) {
		AMMO = ammo;
		return this;
	}

	public Gun refillGun() {
		setAmmoLeft(GUN.getMagazineSize());
		return this;
	}

	public void shoot(float bulletAccuracy, float bulletSpeed, float volume, int bulletsPerTick, Sound shootSound,
			double damage, int bulletflytime) {
		p.getWorld().playSound(p.getLocation(), shootSound, volume, 10);
		AMMO--;
		for (int i = 0; i < bulletsPerTick; i++) {
			new Bullet(p, damage, bulletSpeed, bulletAccuracy, bulletflytime, this);
		}
	}

}
