package at.ltd.adds.game.player.character.skillhandler;

import java.util.ArrayList;

import org.bukkit.event.Listener;

import at.ltd.Main;
import at.ltd.adds.game.player.character.skillhandler.handlers.SkillCloseCombatRangeHandler;
import at.ltd.adds.game.player.character.skillhandler.handlers.SkillMedicHealingSpeedHandler;
import at.ltd.adds.game.player.character.skillhandler.handlers.SkillMedicRegenerationStartHandler;
import at.ltd.adds.game.player.character.skillhandler.handlers.SkillReloadTimeHandler;
import at.ltd.adds.game.player.character.skillhandler.handlers.SkillSniperAccuracyHandler;
import at.ltd.adds.game.player.character.skillhandler.handlers.SkillSniperPistolHandler;
import at.ltd.adds.game.player.character.skillhandler.handlers.SkillSoldierDamageHandler;

public class SkillHandler {

	private static ArrayList<Object> HANDLERS = new ArrayList<>();

	public static void init() {

		HANDLERS.add(new SkillCloseCombatRangeHandler());
		HANDLERS.add(new SkillSoldierDamageHandler());
		HANDLERS.add(new SkillReloadTimeHandler());
		HANDLERS.add(new SkillSniperPistolHandler());
		HANDLERS.add(new SkillSniperAccuracyHandler());
		HANDLERS.add(new SkillMedicHealingSpeedHandler());
		HANDLERS.add(new SkillMedicRegenerationStartHandler());

		for (Object obj : HANDLERS) {
			try {
				SkillHandlerInterface shi = (SkillHandlerInterface) obj;
				shi.onRegister();
				if (obj instanceof Listener) {
					Main.registerListener((Listener) obj);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

}
