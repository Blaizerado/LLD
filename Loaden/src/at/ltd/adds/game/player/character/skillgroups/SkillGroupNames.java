package at.ltd.adds.game.player.character.skillgroups;

import org.bukkit.entity.Player;

import at.ltd.adds.game.player.character.Character;
import at.ltd.adds.game.player.character.SkillGroup;
import at.ltd.adds.game.player.character.SkillGroupManager;

public enum SkillGroupNames {

	CLOSE_COMBAT, RELOAD_TIME, SOLDIER_DAMAGE, SNIPER_PISTOL, SNIPER_ACCURACY, MEDIC_REGENERATION_STARTING_TIME, MEDIC_REGENERATION_SPEED;

	public SkillGroup getSkillGroup() {
		return SkillGroupManager.getGroup(this.name());
	}

	public int getPlayerLvl(Player p) {
		return Character.getSkillData(p).getLevel(this.name());
	}

}
