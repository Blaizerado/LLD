package at.ltd.gungame.level;

import org.bukkit.entity.Player;

import at.ltd.gungame.guns.utils.Gun;
import at.ltd.gungame.guns.utils.GunInterface;
import at.ltd.gungame.guns.utils.GunUtils;

public class GLevelGun extends GLevelItem {

	private GunInterface gun;

	public GLevelGun(GunInterface gi, int slot) {
		super(gi.getItem().clone(), slot);
		gun = gi;

	}

	public Gun getGun(Player player) {
		return GunUtils.createGun(gun, player, -1);
	}

}
