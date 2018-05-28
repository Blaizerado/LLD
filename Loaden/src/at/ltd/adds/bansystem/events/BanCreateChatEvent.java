package at.ltd.adds.bansystem.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.bansystem.BanSystem;
import at.ltd.adds.bansystem.BanUtils;
import at.ltd.adds.bansystem.TimeBanCreator;
import at.ltd.adds.bansystem.TimeBanCreator.TIMEBAN_STEP;

public class BanCreateChatEvent implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onName(AsyncPlayerChatEvent e) {
		String msg = e.getMessage();
		Player p = e.getPlayer();
		if (TimeBanCreator.CREATIONS.containsKey(p)) {
			TimeBanCreator tbc = TimeBanCreator.CREATIONS.get(p);
			if (tbc.getStep() != TIMEBAN_STEP.NAME) {
				return;
			}
			if (e.isCancelled()) {
				return;
			}
			e.setCancelled(true);
			if (msg.length() == 36) {
				if (BanUtils.doesUUIDExist(msg)) {
					tbc.setUUID(msg);
				} else {
					p.sendMessage(BanSystem.p + "§cDiese UUID ist der Datenbank nicht bekannt. Versuche es nochmal.");
					return;
				}

			} else {
				if (Bukkit.getPlayer(msg) != null) {
					Player banned = Bukkit.getPlayer(msg);
					tbc.setUUID(banned.getUniqueId().toString());
					tbc.setName(banned.getName());
				} else {
					p.sendMessage(BanSystem.p + "§cKein Spieler gefunden. Versuche es nochmal.");
					return;
				}
			}

			tbc.setStep(TIMEBAN_STEP.DAY);
			if (tbc.getName() == null) {
				p.sendMessage(BanSystem.p + "UUID: §c" + tbc.getUUID());
			} else {
				p.sendMessage(BanSystem.p + "Name: §c" + tbc.getName());
			}

			p.sendMessage(BanSystem.p + "Gib jetzt bitte die Tage die er gebannt werden soll ein.");

		}
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onDays(AsyncPlayerChatEvent e) {
		String msg = e.getMessage();
		Player p = e.getPlayer();
		if (TimeBanCreator.CREATIONS.containsKey(p)) {

			TimeBanCreator tbc = TimeBanCreator.CREATIONS.get(p);
			if (tbc.getStep() != TIMEBAN_STEP.DAY) {
				return;
			}
			if (e.isCancelled()) {
				return;
			}
			e.setCancelled(true);
			int i = 0;
			try {
				i = Integer.valueOf(msg);
			} catch (Exception e2) {
				p.sendMessage(BanSystem.p + "§cDas ist keine gültige Zahl. Versuchs nochmal!");
				return;
			}
			tbc.setDays(i);
			tbc.setStep(TIMEBAN_STEP.HOURS);
			p.sendMessage(BanSystem.p + "Gib jetzt bitte die Stunden die er gebannt werden soll ein.");
		}
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onHours(AsyncPlayerChatEvent e) {
		String msg = e.getMessage();
		Player p = e.getPlayer();
		if (TimeBanCreator.CREATIONS.containsKey(p)) {

			TimeBanCreator tbc = TimeBanCreator.CREATIONS.get(p);
			if (tbc.getStep() != TIMEBAN_STEP.HOURS) {
				return;
			}
			if (e.isCancelled()) {
				return;
			}
			e.setCancelled(true);
			int i = 0;
			try {
				i = Integer.valueOf(msg);
			} catch (Exception e2) {
				p.sendMessage(BanSystem.p + "§cDas ist keine gültige Zahl. Versuchs nochmal!");
				return;
			}
			tbc.setHours(i);
			tbc.setStep(TIMEBAN_STEP.MINUTES);
			p.sendMessage(BanSystem.p + "Gib jetzt bitte die Minuten die er gebannt werden soll ein.");
		}
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onMin(AsyncPlayerChatEvent e) {
		String msg = e.getMessage();
		Player p = e.getPlayer();
		if (TimeBanCreator.CREATIONS.containsKey(p)) {

			TimeBanCreator tbc = TimeBanCreator.CREATIONS.get(p);
			if (tbc.getStep() != TIMEBAN_STEP.MINUTES) {
				return;
			}
			if (e.isCancelled()) {
				return;
			}
			e.setCancelled(true);
			int i = 0;
			try {
				i = Integer.valueOf(msg);
			} catch (Exception e2) {
				p.sendMessage(BanSystem.p + "§cDas ist keine gültige Zahl. Versuchs nochmal!");
				return;
			}
			tbc.setMinutes(i);
			tbc.setStep(TIMEBAN_STEP.REASON);
			p.sendMessage(BanSystem.p + "Gib jetzt bitte den Grund ein. 'no' für keinen Grund.");
		}
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onReason(AsyncPlayerChatEvent e) {
		String msg = e.getMessage();
		Player p = e.getPlayer();
		if (TimeBanCreator.CREATIONS.containsKey(p)) {
			TimeBanCreator tbc = TimeBanCreator.CREATIONS.get(p);
			if (tbc.getStep() != TIMEBAN_STEP.REASON) {
				return;
			}
			if (e.isCancelled()) {
				return;
			}
			e.setCancelled(true);
			if (msg.equals("no") | msg.equals("")) {
				tbc.setReason(null);
			} else {
				tbc.setReason(msg);
			}

			if (tbc.getName() == null) {
				p.sendMessage(BanSystem.p + "UUID:§c " + tbc.getUUID());
			} else {
				p.sendMessage(BanSystem.p + "Name:§c " + tbc.getName());
			}
			p.sendMessage(BanSystem.p + "Tage:§c " + tbc.getDays());
			p.sendMessage(BanSystem.p + "Strunden:§c " + tbc.getHours());
			p.sendMessage(BanSystem.p + "Minuten:§c " + tbc.getMinutes());
			if (tbc.getReason() == null) {
				p.sendMessage(BanSystem.p + "Reason:§c Kein");
			} else {
				p.sendMessage(BanSystem.p + "Reason:§c " + tbc.getReason());
			}
			p.sendMessage(BanSystem.p + "Zum Bestätigen tippe jetzt 'ja', zum abbrechen 'stop'.");
			tbc.setStep(TIMEBAN_STEP.CONFIRM);
		}
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onConfirm(AsyncPlayerChatEvent e) {
		String msg = e.getMessage();
		Player p = e.getPlayer();
		if (TimeBanCreator.CREATIONS.containsKey(p)) {
			TimeBanCreator tbc = TimeBanCreator.CREATIONS.get(p);
			if (tbc.getStep() != TIMEBAN_STEP.CONFIRM) {
				return;
			}
			if (e.isCancelled()) {
				return;
			}
			e.setCancelled(true);
			if (msg.equalsIgnoreCase("ja")) {
				boolean bool = tbc.createBan();
				if (bool) {
					p.sendMessage(BanSystem.p + "§cEr wurde gebannt.");
					TimeBanCreator.CREATIONS.remove(p);
				} else {
					Cf.rsS(Cf.BAN_CANCEL, p);
					TimeBanCreator.CREATIONS.remove(p);
				}
			} else {
				p.sendMessage(BanSystem.p + "Zum Bestätigen tippe jetzt 'ja', zum abbrechen 'stop'.");
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onCancel(AsyncPlayerChatEvent e) {
		String msg = e.getMessage();
		Player p = e.getPlayer();
		if (TimeBanCreator.CREATIONS.containsKey(p)) {
			if (msg.equalsIgnoreCase("stop")) {
				e.setCancelled(true);
				p.sendMessage(Main.getPrefix() + "§4Abgebrochen.");
				TimeBanCreator.CREATIONS.remove(p);
			}
		}
	}

}
