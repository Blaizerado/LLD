package at.ltd.adds.game.player.character;

import org.bukkit.entity.Player;

import at.ltd.adds.game.player.character.skillgroups.SkillGroupNames;

public interface SkillGroup {

	public void onRegister();

	public String getCodeName();
	
	public String getFrontEndName();

	public int getLevels();

	public int getPlayerLevel(Player p);

	public void levelUp(Player p);
	
	public void levelDown(Player p);
	
	public String getInfo(Player p);

	public CharacterType getCharacterType();

	
	public static SkillGroup getSkillGroup(String name){
		return SkillGroupManager.getGroup(name);
	}

	public static SkillGroup getSkillGroup(SkillGroupNames name){
		return SkillGroupManager.getGroup(name.name());
	}
	
}
