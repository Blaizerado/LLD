package at.ltd.gungame.guns.utils.enums;

import org.bukkit.Material;

import at.ltd.gungame.guns.utils.bullet.BulletUtils;
import at.ltd.gungame.guns.utils.rocket.RocketUtils;

public enum AmmoType {

	BULLET(BulletUtils.BULLET_MATERIAL), ROCKET(RocketUtils.ROCKET_MATERIAL);

	private final Material material;

	private AmmoType(Material s) {
		material = s;
	}

	public String toString() {
		return this.material.name();
	}

	public Material getMaterial() {
		return material;
	}

}
