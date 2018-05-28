package at.ltd.gungame.guns.utils;

import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import at.ltd.gungame.guns.utils.enums.AmmoType;
import at.ltd.gungame.guns.utils.enums.GunType;

public interface GunInterface {

	public String getName();
	public int getGunID();
	public double getDamage();
	public int getMagazineSize();
	public ItemStack getItem();
	public boolean isAkimbo();
	public boolean isSpecial();
	public AmmoType getAmmoType();
	public GunType getGunType();
	public Sound getSound();
	public Sound getReloadSound();
	public float getVolume();
	public int getFireSpeed();
	public int getReloadTime();
	public int getBulletFlyTime();
	public int getAmmountOfBulletsPerTick();
	public float getBulletAccuracy();
	public float getBulletSpeed();
	public boolean isInstantHit();


}
