package at.ltd.gungame.guns.RIFLES.MG;

import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import at.ltd.gungame.guns.utils.GunInterface;
import at.ltd.gungame.guns.utils.enums.AmmoType;
import at.ltd.gungame.guns.utils.enums.GunType;

public class RPD_GOLD implements GunInterface {

	private String name;
	private int magazinesize;
	private ItemStack item;
	private boolean akimbo;
	private boolean special;
	private AmmoType ammo;
	private GunType guntype;
	private Sound sound;
	private int firespeed;
	private int reloadtime;
	private int bulletspertick;
	private float bulletaccuracy;
	private float bulletspeed;
	private boolean instanthit;
	private double damage;
	private Sound reloadsound;
	private float gunvolume;
	private int bulletflytime;

	@Override
	public double getDamage() {
		return damage;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getGunID() {
		return 32;
	}

	@Override
	public int getMagazineSize() {
		return magazinesize;
	}

	@Override
	public ItemStack getItem() {
		return item;
	}

	@Override
	public boolean isAkimbo() {
		return akimbo;
	}

	@Override
	public boolean isSpecial() {
		return special;
	}

	@Override
	public AmmoType getAmmoType() {
		return ammo;
	}

	@Override
	public GunType getGunType() {
		return guntype;
	}

	@Override
	public Sound getSound() {
		return sound;
	}

	@Override
	public int getFireSpeed() {
		return firespeed;
	}

	@Override
	public int getReloadTime() {
		return reloadtime;
	}

	@Override
	public int getAmmountOfBulletsPerTick() {
		return bulletspertick;
	}

	@Override
	public float getBulletAccuracy() {
		return bulletaccuracy;
	}

	@Override
	public float getBulletSpeed() {
		return bulletspeed;
	}

	@Override
	public boolean isInstantHit() {
		return instanthit;
	}
	@Override
	public Sound getReloadSound() {
		return reloadsound;
	}

	@Override
	public float getVolume() {
		return gunvolume;
	}

	@Override
	public int getBulletFlyTime() {
		return bulletflytime;
	}

}