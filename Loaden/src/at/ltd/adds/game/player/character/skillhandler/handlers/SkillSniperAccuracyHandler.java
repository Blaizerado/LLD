package at.ltd.adds.game.player.character.skillhandler.handlers;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import at.ltd.Main;
import at.ltd.adds.game.player.character.skillgroups.SkillGroupNames;
import at.ltd.adds.game.player.character.skillhandler.SkillHandlerInterface;
import at.ltd.adds.utils.config.Config;
import at.ltd.gungame.guns.events.custom.EventGunShoot;
import at.ltd.gungame.guns.utils.enums.GunType;

public class SkillSniperAccuracyHandler implements SkillHandlerInterface, Listener {

	public static HashMap<GunType, Float> ACCURACY_DATA = new HashMap<>();

	@Override
	public void onRegister() {
		Main.registerListener(this);
		Config.getInstance("SkillSniperAccuracySettings").setReloadAble(true).setAsyncReloadAble(false)
				.setReloadHandler(() -> {
					readConfig();
				});
		readConfig();
	}

	public void readConfig() {
		ACCURACY_DATA.clear();
		Config cfg = Config.getInstance("SkillSniperAccuracySettings").load();
		for (GunType gt : GunType.values()) {
			String name = gt.name();
			if (cfg.contains(name)) {
				ACCURACY_DATA.put(gt, cfg.getFloat(name));
			} else {
				cfg.putRaw(name,"[TYPE:FLOAT]3.00");
				ACCURACY_DATA.put(gt, 3.00F);
			}
		}
	}

	@EventHandler
	public void on(EventGunShoot e) {
		Player p = e.getPlayer();
		float lvl = SkillGroupNames.SNIPER_ACCURACY.getPlayerLvl(p);
		if (lvl == 0) {
			return;
		}
		float maxlvl = SkillGroupNames.SNIPER_ACCURACY.getSkillGroup().getLevels();
		float accuracy = e.getBulletAccuracy();

		float removeaccuracy = accuracy / ACCURACY_DATA.get(e.getGun().getGunInterface().getGunType());
		removeaccuracy = (removeaccuracy * ((lvl / maxlvl)));
		accuracy -= removeaccuracy;
		e.setBulletAccuracy(accuracy);
	}

}
