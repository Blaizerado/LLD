package at.ltd.adds.game.player.character.skillhandler.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import at.ltd.Main;
import at.ltd.adds.game.player.character.skillgroups.SkillGroupNames;
import at.ltd.adds.game.player.character.skillhandler.SkillHandlerInterface;
import at.ltd.gungame.guns.events.custom.EventGunShoot;
import at.ltd.gungame.guns.utils.enums.GunType;

public class SkillSniperPistolHandler implements SkillHandlerInterface, Listener {

	@Override
	public void onRegister() {
		Main.registerListener(this);
	}

	@EventHandler
	public void on(EventGunShoot e) {
		if(e.getGun().getGunInterface().getGunType() == GunType.PISTOL) {
			Player p = e.getPlayer();
			float lvl = SkillGroupNames.SNIPER_PISTOL.getPlayerLvl(p);
			if(lvl == 0) {
				return;
			}
			float maxlvl = SkillGroupNames.SNIPER_PISTOL.getSkillGroup().getLevels();
			
			float speed = e.getBulletSpeed();
			
			float addspeed = (float) (speed / 3.3);
			addspeed = (float) (addspeed * ((lvl / maxlvl)));
			speed += addspeed;
			e.setBulletSpeed(speed);
			
			int firedelay = e.getFirespeed();
			
			double removedelay = firedelay / 3;
			removedelay = (removedelay * ((lvl / maxlvl)));
			firedelay -= removedelay;
			e.setFirespeed(firedelay);
			
			
		}

		
	}

}
