package at.ltd.adds.game.player.character.skillhandler.handlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import at.ltd.Main;
import at.ltd.adds.game.player.character.SkillData;
import at.ltd.adds.game.player.character.SkillGroupManager;
import at.ltd.adds.game.player.character.skillgroups.SkillGroupNames;
import at.ltd.adds.game.player.character.skillhandler.SkillHandlerInterface;
import at.ltd.adds.game.player.healing.EventPlayerHealing;
import at.ltd.adds.game.player.healing.Healing;

public class SkillMedicRegenerationStartHandler implements SkillHandlerInterface, Listener {

	@Override
	public void onRegister() {
		Main.registerListener(this);
	}

	@EventHandler
	public void on(EventPlayerHealing e) {
		if(e.isCanceled()) {
			int time = Healing.MIN_LAST_HIT_TIME;
			SkillData sd = SkillData.getData(e.getPlayer());
			float lvl = sd.getLevel(SkillGroupNames.MEDIC_REGENERATION_STARTING_TIME.name());
			if(lvl == 0) {
				return;
			}
			float maxlvl = SkillGroupManager.getGroup(SkillGroupNames.MEDIC_REGENERATION_STARTING_TIME.name()).getLevels();
			int removetime = time / 3;
			removetime = (int) (removetime * ((lvl / maxlvl)));
			time -= removetime;
			if(e.getLastHitTime() > time) {
				e.setCanceled(false);
			}
		}

	}
	
}