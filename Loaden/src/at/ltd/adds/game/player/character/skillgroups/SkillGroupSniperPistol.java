package at.ltd.adds.game.player.character.skillgroups;

import org.bukkit.entity.Player;

import at.ltd.adds.Cf;
import at.ltd.adds.game.player.character.CharacterType;
import at.ltd.adds.game.player.character.SkillData;
import at.ltd.adds.game.player.character.SkillGroup;
import at.ltd.adds.game.player.character.SkillGroupManager;
import at.ltd.adds.utils.config.Config;
import at.ltd.adds.utils.config.ConfigAble;

public class SkillGroupSniperPistol implements SkillGroup {

	@Override
	public void onRegister() {
		Config.loadMyClass(this.getClass().getSimpleName(), this).setReloadAble(true).setAsyncReloadAble(true)
				.setReloadHandler(() -> {
					Config.loadMyClass(this.getClass().getSimpleName(), this);
				});
	}

	@Override
	public String getCodeName() {
		return "SNIPER_PISTOL";
	}

	@Override
	public String getFrontEndName() {
		return "Pistolen Verbesserung";
	}

	@Override
	public int getLevels() {
		return 5;
	}

	@Override
	public int getPlayerLevel(Player p) {
		return SkillData.getData(p).getLevel(getCodeName());
	}

	@ConfigAble(key = "", value = "Lvl: [LVL]<newline>Max. Lvl: [MLVL]<newline>Kosten: [COST]<newline>Klasse: [TYPE]<newline>Verbessert die Fluggeschwindigkeit & Feuerrate von Pistolenkugeln")
	public String info;

	@Override
	public String getInfo(Player p) {
		int cost = SkillGroupManager.getCost(p, this);
		return Cf.rs(info, "[LVL]", getPlayerLevel(p), "[MLVL]", getLevels(), "[COST]", cost, "[TYPE]",
				getCharacterType().getDisplayName());
	}

	@Override
	public CharacterType getCharacterType() {
		return CharacterType.SNIPER;
	}

	@Override
	public void levelUp(Player p) {
		SkillData sd = SkillData.getData(p);
		sd.setLevel(getCodeName(), sd.getLevel(getCodeName()) + 1);
		if (sd.getLevel(getCodeName()) > getLevels()) {
			sd.setLevel(getCodeName(), getLevels());
		}
	}

	@Override
	public void levelDown(Player p) {
		SkillData sd = SkillData.getData(p);
		sd.setLevel(getCodeName(), sd.getLevel(getCodeName()) - 1);
		if (sd.getLevel(getCodeName()) < 0) {
			sd.setLevel(getCodeName(), 0);
		}
	}

}
