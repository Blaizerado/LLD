package at.ltd.adds.game.player.character;

import org.bukkit.entity.Player;

import at.ltd.adds.game.player.character.skillgroups.SkillGroupNames;
import at.ltd.adds.game.player.character.skillhandler.SkillHandler;

public class Character {
	
	public static void init() {
		SkillGroupManager.init();
		SkillHandler.init();
	}
	
	
	public static SkillData getSkillData(Player p) {
		return SkillData.getData(p);
	}
	
	public static SkillGroup getSkillGroup(String name){
		return SkillGroupManager.getGroup(name);
	}

	public static SkillGroup getSkillGroup(SkillGroupNames name){
		return SkillGroupManager.getGroup(name.name());
	}
	
}
