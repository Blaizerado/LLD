package at.ltd.adds.game.player.character.skillhandler.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import at.ltd.Main;
import at.ltd.adds.game.player.character.skillgroups.SkillGroupNames;
import at.ltd.adds.game.player.character.skillhandler.SkillHandlerInterface;
import at.ltd.gungame.events.custom.EventGunGameDamage;
import at.ltd.gungame.events.custom.EventGunGameDamage.DamageType;
import at.ltd.gungame.guns.utils.enums.GunType;

public class SkillSoldierDamageHandler implements SkillHandlerInterface, Listener {

	@Override
	public void onRegister() {
		Main.registerListener(this);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void on(EventGunGameDamage e) {
		Player shooter = e.getDamagee();
		double lvl = SkillGroupNames.SOLDIER_DAMAGE.getPlayerLvl(shooter);
		if (lvl == 0) {
			return;
		}
		if (e.getDamageType() == DamageType.GUN_HIT) {
			if (Main.getGunGamePlayer(shooter).getLastGunHit().getGunType() == GunType.SNIPER) {
				return;
			}
		}
		
		if(e.getDamageType() == DamageType.PLAYER_DAMAGE) {
			return;
		}
		double maxlvl = SkillGroupNames.SOLDIER_DAMAGE.getSkillGroup().getLevels();
		double damage = e.getDamage();

		double removedamage = damage / 4;
		removedamage = (removedamage * ((lvl / maxlvl)));
		damage -= removedamage;
		e.setDamage(damage);

	}

}
