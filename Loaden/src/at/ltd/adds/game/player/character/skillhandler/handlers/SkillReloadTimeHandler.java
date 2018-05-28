package at.ltd.adds.game.player.character.skillhandler.handlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import at.ltd.Main;
import at.ltd.adds.game.player.character.SkillData;
import at.ltd.adds.game.player.character.SkillGroupManager;
import at.ltd.adds.game.player.character.skillgroups.SkillGroupNames;
import at.ltd.adds.game.player.character.skillhandler.SkillHandlerInterface;
import at.ltd.gungame.guns.events.custom.EventGunReloadAsync;

public class SkillReloadTimeHandler implements SkillHandlerInterface, Listener {

	@Override
	public void onRegister() {
		Main.registerListener(this);
	}

	@EventHandler
	public void on(EventGunReloadAsync e) {
		int time = e.getReloadTime();
		SkillData sd = SkillData.getData(e.getPlayer());
		float lvl = sd.getLevel(SkillGroupNames.RELOAD_TIME.name());
		if(lvl == 0) {
			return;
		}
		float maxlvl = SkillGroupManager.getGroup(SkillGroupNames.RELOAD_TIME.name()).getLevels();
		int removetime = time / 3;
		removetime = (int) (removetime * ((lvl / maxlvl)));
		time -= removetime;
		e.setReloadTime(time);
	}

}
