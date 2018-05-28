package at.ltd.adds.game.player.character;

import java.util.HashMap;

import org.bukkit.entity.Player;

import at.ltd.Main;
import at.ltd.adds.game.player.character.skillgroups.SkillGroupCloseCombatRange;
import at.ltd.adds.game.player.character.skillgroups.SkillGroupMedicHealingSpeed;
import at.ltd.adds.game.player.character.skillgroups.SkillGroupMedicRegenerationStartingTime;
import at.ltd.adds.game.player.character.skillgroups.SkillGroupReloadTime;
import at.ltd.adds.game.player.character.skillgroups.SkillGroupSniperAccuracy;
import at.ltd.adds.game.player.character.skillgroups.SkillGroupSniperPistol;
import at.ltd.adds.game.player.character.skillgroups.SkillGroupSoldierDamage;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;

public class SkillGroupManager {

	public static HashMap<String, SkillGroup> GROUPS = new HashMap<>();

	public static void init() {

		addGroup(new SkillGroupCloseCombatRange());
		addGroup(new SkillGroupReloadTime());
		addGroup(new SkillGroupMedicHealingSpeed());
		addGroup(new SkillGroupMedicRegenerationStartingTime());
		addGroup(new SkillGroupSniperAccuracy());
		addGroup(new SkillGroupSniperPistol());
		addGroup(new SkillGroupSoldierDamage());

		Main.registerListener(new SkillEventHandler());
		AsyncThreadWorkers.submitSchedulerWorkSec(new Runnable() {

			@Override
			public void run() {
				for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
					Character.getSkillData(p).save();
				}
			}
		}, 60, 30);

	}

	public static int getCost(Player p, SkillGroup sg) {
		CharacterType ct = Character.getSkillData(p).getType();
		if (sg.getCharacterType() == CharacterType.NOT_SELECTED) {
			return 0;
		}
		if (sg.getCharacterType() == ct | sg.getCharacterType() == CharacterType.ALL) {
			return 1;
		} else {
			return 2;
		}
	}

	public static void addGroup(SkillGroup group) {
		synchronized (GROUPS) {
			GROUPS.put(group.getCodeName(), group);
		}
		try {
			group.onRegister();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static SkillGroup getGroup(String name) {
		synchronized (GROUPS) {
			return GROUPS.get(name);
		}
	}

}
