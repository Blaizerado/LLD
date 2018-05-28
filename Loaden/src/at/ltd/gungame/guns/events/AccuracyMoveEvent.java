package at.ltd.gungame.guns.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import at.ltd.adds.utils.config.Config;
import at.ltd.adds.utils.config.ConfigAble;
import at.ltd.gungame.guns.events.custom.EventGunShoot;
import at.ltd.gungame.guns.utils.Gun;
import at.ltd.gungame.guns.utils.GunInterface;
import at.ltd.gungame.guns.utils.Movement;
import at.ltd.gungame.guns.utils.Movement.MovementType;
import at.ltd.gungame.guns.utils.enums.GunType;

public class AccuracyMoveEvent implements Listener {

	@ConfigAble(key = "", value = "0.14")
	public static float WALK_ASSAULT_RIFLE;
	@ConfigAble(key = "", value = "0.25")
	public static float SPRINT_ASSAULT_RIFLE;

	@ConfigAble(key = "", value = "0.65")
	public static float WALK_DMR;
	@ConfigAble(key = "", value = "0.9")
	public static float SPRINT_DMR;

	@ConfigAble(key = "", value = "0.9")
	public static float WALK_SNIPER;
	@ConfigAble(key = "", value = "1.8")
	public static float SPRINT_SNIPER;

	@ConfigAble(key = "", value = "0.1")
	public static float WALK_PISTOL;
	@ConfigAble(key = "", value = "0.2")
	public static float SPRINT_PISTOL;

	@ConfigAble(key = "", value = "0.16")
	public static float WALK_PDW;
	@ConfigAble(key = "", value = "0.3")
	public static float SPRINT_PDW;

	@ConfigAble(key = "", value = "0.35")
	public static float WALK_MG;
	@ConfigAble(key = "", value = "0.9")
	public static float SPRINT_MG;

	@ConfigAble(key = "", value = "0.2")
	public static float WALK_SHOTGUN;
	@ConfigAble(key = "", value = "0.12")
	public static float SPRINT_SHOTGUN;

	@ConfigAble(key = "", value = "1.4")
	public static float WALK_ROCKET_LAUNCHER;
	@ConfigAble(key = "", value = "1")
	public static float SPRINT_ROCKET_LAUNCHER;

	public static void init() {
		Config.loadMyClass("AccuracyModifier", AccuracyMoveEvent.class).setReloadAble(true).setAsyncReloadAble(false).setReloadHandler(() -> {
			Config.loadMyClass("AccuracyModifier", AccuracyMoveEvent.class);
		});
	}

	@EventHandler
	public void on(EventGunShoot e) {
		Player p = e.getPlayer();
		Gun gun = e.getGun();
		GunInterface gi = gun.getGunInterface();
		MovementType movement = Movement.getMovement(p);

		if (movement == MovementType.STAYING | movement == MovementType.SNEAKING) {
			return;
		}

		if (gi.getGunType() == GunType.ASSAULT_RIFLE) {
			if (movement == MovementType.SPRINTING) {
				e.setBulletAccuracy(e.getBulletAccuracy() + SPRINT_ASSAULT_RIFLE);
				return;
			}
			if (movement == MovementType.WALKING) {
				e.setBulletAccuracy(e.getBulletAccuracy() + WALK_ASSAULT_RIFLE);
				return;
			}
		}

		if (gi.getGunType() == GunType.MG) {
			if (movement == MovementType.SPRINTING) {
				e.setBulletAccuracy(e.getBulletAccuracy() + SPRINT_MG);
				return;
			}
			if (movement == MovementType.WALKING) {
				e.setBulletAccuracy(e.getBulletAccuracy() + WALK_MG);
				return;
			}
		}

		if (gi.getGunType() == GunType.PDW) {
			if (movement == MovementType.SPRINTING) {
				e.setBulletAccuracy(e.getBulletAccuracy() + SPRINT_PDW);
				return;
			}
			if (movement == MovementType.WALKING) {
				e.setBulletAccuracy(e.getBulletAccuracy() + WALK_PDW);
				return;
			}
		}

		if (gi.getGunType() == GunType.SNIPER) {
			if (movement == MovementType.SPRINTING) {
				e.setBulletAccuracy(e.getBulletAccuracy() + SPRINT_SNIPER);
				return;
			}
			if (movement == MovementType.WALKING) {
				e.setBulletAccuracy(e.getBulletAccuracy() + WALK_SNIPER);
				return;
			}
		}

		if (gi.getGunType() == GunType.DMR) {
			if (movement == MovementType.SPRINTING) {
				e.setBulletAccuracy(e.getBulletAccuracy() + SPRINT_DMR);
				return;
			}
			if (movement == MovementType.WALKING) {
				e.setBulletAccuracy(e.getBulletAccuracy() + WALK_DMR);
				return;
			}
		}

		if (gi.getGunType() == GunType.PISTOL) {
			if (movement == MovementType.SPRINTING) {
				e.setBulletAccuracy(e.getBulletAccuracy() + SPRINT_PISTOL);
				return;
			}
			if (movement == MovementType.WALKING) {
				e.setBulletAccuracy(e.getBulletAccuracy() + WALK_PISTOL);
				return;
			}
		}
		if (gi.getGunType() == GunType.ROCKET_LAUNCHER) {
			if (movement == MovementType.SPRINTING) {
				e.setBulletAccuracy(e.getBulletAccuracy() + SPRINT_ROCKET_LAUNCHER);
				return;
			}
			if (movement == MovementType.WALKING) {
				e.setBulletAccuracy(e.getBulletAccuracy() + WALK_ROCKET_LAUNCHER);
				return;
			}
		}

		if (gi.getGunType() == GunType.SHOTGUN) {
			if (movement == MovementType.SPRINTING) {
				e.setBulletAccuracy(e.getBulletAccuracy() + SPRINT_SHOTGUN);
				return;
			}
			if (movement == MovementType.WALKING) {
				e.setBulletAccuracy(e.getBulletAccuracy() + WALK_SHOTGUN);
				return;
			}
		}

	}

}
