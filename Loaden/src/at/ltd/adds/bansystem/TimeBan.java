package at.ltd.adds.bansystem;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import at.ltd.adds.bansystem.TimeBanCreator.TIMEBAN_STEP;

public class TimeBan implements Listener {

	public static void start(Player p) {
		p.sendMessage(BanSystem.p + "Folgende daten werden abgefragt! ");
		p.sendMessage(BanSystem.p + " -> (Name/UUID, Tage, Stunden, Minuten, Grund)");
		p.sendMessage(BanSystem.p + "Du kannst ganz einfach in den Chat schreiben. ");
		p.sendMessage(BanSystem.p + " -> Die Daten die du absendest wird niemand sehen.");
		p.sendMessage(BanSystem.p + "Wenn du es abbrechen möchtest, ");
		p.sendMessage(BanSystem.p + " -> tippe einfach 'stop' in den Chat.");
		p.sendMessage(BanSystem.p + "Tippe jetzt bitte den Name/UUID des Täters ein.");
		TimeBanCreator tbc = new TimeBanCreator(p);
		TimeBanCreator.CREATIONS.put(p, tbc);
		tbc.setStep(TIMEBAN_STEP.NAME);
	}

	@EventHandler
	public void on(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (TimeBanCreator.CREATIONS.containsKey(p)) {
			TimeBanCreator.CREATIONS.remove(p);
		}
	}

}
