package at.ltd.gungame.guns.RIFLES.ASSAULT_RIFLE;

import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import at.ltd.gungame.guns.utils.GunInterface;
import at.ltd.gungame.guns.utils.enums.AmmoType;
import at.ltd.gungame.guns.utils.enums.GunType;

public class ACW_R_SNOW implements GunInterface {

	protected String name;
	protected int magazinesize;
	protected ItemStack item;
	protected boolean akimbo;
	protected boolean special;
	protected AmmoType ammo;
	protected GunType guntype;
	protected Sound sound;
	protected int firespeed;
	protected int reloadtime;
	protected int bulletspertick;
	protected float bulletaccuracy;
	protected float bulletspeed;
	protected boolean instanthit;
	protected double damage;
	protected Sound reloadsound;
	protected float gunvolume;
	protected int bulletflytime;
	
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
		return 14;
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