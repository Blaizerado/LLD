package at.ltd.adds;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import at.ltd.Main;
import at.ltd.adds.sql.sqlutils.SQLCollection;
import at.ltd.adds.utils.config.ConfigManager;
import at.ltd.adds.utils.external.MessageUtil;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;

public class Cf {

	// Strings

	public static String NO_OP = "[PREFIX]&4Op ist benötigt.";
	public static String NO_RIGHTS = "[PREFIX]&4Keine Berechtigung.";
	public static String TEXT_BEWERBEN = "[PREFIX]Link: https://www.ltd-net.eu/bewerberportal/?lang=de";
	public static String GETCOINS = "[PREFIX]Deine Coins&f:&4 [COINS]";
	public static String HELP_1 = "[PREFIX]Hilfe:";
	public static String HELP_2 = "&8/&7coins &4|&f Zeigt deine Coins an.";
	public static String HELP_3 = "&8/&7trade <Player> <Coins> &4|&f Gibt dem ausgewähltem Spieler Coins.";
	public static String HELP_4 = "&8/&7join &4|&f Damit trittst du der Runde bei.";
	public static String HELP_5 = "&8/&7leave &4|&f Damit verlässt du die Runde.";
	public static String HELP_6 = "&8/&7spawn &4|&f Teleportiert dich zum Spawn.";
	public static String HELP_7 = "&8/&7shop &4|&f Teleportiert dich zum Shop.";
	public static String HELP_8 = "&8/&7bewerben &4|&f Möchtest du dich bewerben?";
	public static String HELP_9 = "&8/&7lager &4|&f Teleportiert dich zu deinem Lager.";
	public static String PLUGIN_COMMAND = "Plugins(5): &aAsyncWorldEditInjector&r, &aLtdSystemAPI&r, &aLtdSystem&r, &aWorldEdit&r, &aAsyncWorldEdit&r, &aTja der rest ist geheim :)";
	public static String RESET_LAGER_COMMAND_1 = "[PREFIX]Möchtest du wirklich dein Lager resetten?";
	public static String RESET_LAGER_COMMAND_2 = "[PREFIX]So benutze: /resetlager accept";
	public static String RESET_LAGER_COMMAND_3 = "[PREFIX]Dein Lager wurde zurückgesetzt.";
	public static String COMMAND_SAY = "[PREFIX]Bitte benutze den normalen Chat.";
	public static String MAP_VOTE_COMMAND_1 = "[PREFIX]Es ist gerade kein Map voting.";
	public static String MAP_VOTE_COMMAND_2 = "[PREFIX]Du hast schon einmal gevotet. Map&6:&b [VOTEDMAP]";
	public static String COMMAND_COOLDOWN = "[PREFIX]&4Du musst noch &c[SECLEFT]&4 Sekunden warten!";
	public static String LEAVE_MESSAGE = "&c✖ &6[NAME] &7hat den Server verlassen.";
	public static String JOIN_MESSAGE = "&a➼ &6 [NAME] &7ist beigetreten!";
	public static String JOIN_NEW_MESSAGE = "&6➠ &a[PLAYER] &7ist neu auf dem Server! &7[&c#[DBID]&7]";
	public static String NOT_POSSIBLE = "[PREFIX]Nicht möglich.";
	public static String JOIN_GAME_NOT_STARTED = "[PREFIX]Die Runde ist noch nicht gestartet.";
	public static String LEAVE_GAME_NOT_PROPERLY = "[PREFIX]Bitte nicht einfach so abhauen!";
	public static String LAGER_TP = "&c&lLTD &7➠ &3Du wurdest zu deinem Lager teleportiert!";
	public static String LAGER_NO_TP = "[PREFIX]Das Teleportieren wurde abgelehnt.";
	public static String LAGER_BUY_1 = "[PREFIX]Du hast soeben erfolgreich Lager [ID] gekauft.";
	public static String LAGER_BUY_2 = "[PREFIX]Du hast nicht genügend Coins!";
	public static String LAGER_BUY_3 = "[PREFIX]Es trat ein fehler auf.";
	public static String ROUND_END_1_TITLE = "&7TIME &8>&7> &2Noch &4[TIME]&2 Minuten.";
	public static String ROUND_END_2_TITLE = "&7TIME &8>&7> &2Noch &4[TIME]&2 Minute.";
	public static String ROUND_END_3_ACTION_BAR = "&7TIME &8>&7> &2Noch &4[TIME]&2 Minuten.";
	public static String ROUND_END_4_COUNTDOWN = "[PREFIX] Die Runde endet in &4[TIME]&c Sekunden&3.";
	public static String ROUND_END_5_COUNTDOWN = "[PREFIX] Die Runde endet in &4einer&c Sekunde&3.";
	public static String ROUND_RANKING_1 = "[PREFIX]Ranking (Kills):";
	public static String ROUND_RANKING_2 = "[PREFIX]&8#&7[PLATZ] &6Name&7:&3 [NAME] &6Kills&7:&3 [KILLS]";
	public static String ROUND_CASH_OUT_1 = "[PREFIX]Du hast &6[ADDCOINS] &3Coins bekommen.";
	public static String ROUND_CASH_OUT_2 = "[PREFIX]&3Durch deinen &bBoost &3von &7[MULTICOINFACTOR]x &3hast du zusätzlich &4[PLUSCOINS] &3Coins bekommen.";
	public static String ROUND_VOTE_1 = "&c&lLTD &7➠ &3Stimme jetzt für deine Lieblingsmap ab mit &7》&a/Vote&7《 &e*KLICK*";
	public static String ROUND_VOTE_2 = "&c&lLTD &7➠ &3Die Map &b[MAP] &3wurde gewählt, klicke &a*HIER* &3um &3der &3Runde &3nachzujoinen.";
	public static String ROUND_VOTE_3 = "&c&lLTD &7➠ &3Du &3hast &3erfolgreich &3für &3die &3Map &e[MAP] &3abgestimmt!";
	public static String ROUND_VOTE_4 = "[PREFIX]Geht leider nicht mehr.";
	public static String ROUND_VOTE_5 = "[PREFIX][VOTER] hat für [MAP] abgestimmt.";
	public static String ROUND_VOTE_WAIT = "[PREFIX]Um eine Runde zu starten sind 2 Mapvotes nötig!";
	public static String ROUND_VOTE_WAIT_COMPLETE = "[PREFIX]Noch 2 Minuten dann startet die Runde!";
	public static String ROUND_VOTE_1_COUNTDOWN = "[PREFIX] Das Voting endet in &4[TIME]&c Sekunden&3.";
	public static String ROUND_VOTE_2_COUNTDOWN = "[PREFIX] Das Voting endet in &4einer&c Sekunde&3.";
	public static String ROUND_VOTE_3_COUNTDOWN = "[PREFIX] Das Voting endet in &4einer&c Minute&3.";
	public static String ROUND_JOIN = "[PREFIX][NAME] ist der Runde beigetreten.";
	public static String ROUND_LEAVE_ALL = "[PREFIX][NAME] ist aus der Runde ausgetreten.";
	public static String ROUND_LEAVE = "[PREFIX]Du hast die Runde verlassen.";
	public static String ROUND_KILL_COINS = "[PREFIX]&a+ &b[COINSKILL]x Coins";
	public static String TRADE_ERROR_INVALID_INFORMATION = "[PREFIX]Es trat ein Fehler auf. Bitte Überprüfe ob die angaben stimmen.";
	public static String TRADE_OWN_ACC = "[PREFIX]&cTransaktion&4 auf das eigene &cKonto&4 wurde abgebrochen.";
	public static String TRADE_CANCEL = "[PREFIX]&cTransaktion&4 wurde abgebrochen.";
	public static String TRADE_UNDER_15 = "[PREFIX]&cEs müssen mindestens &415 &cCoins überwiesen werden.";
	public static String TRADE_EXEPTION = "[PREFIX]Es trat ein Fehler auf bitte versuche es nochmal.";
	public static String TRADE_NOT_ENOUGHCOINS = "[PREFIX]Du hast nicht genügend Coins!";
	public static String TRADE_SUCCESS_1 = "[PREFIX]Die Coins wurden ihm zugestellt.";
	public static String TRADE_SUCCESS_2 = "[PREFIX]&2Du hast eine Zahlung in höhe von &4[COINSADD]&2 Coins bekommen von&7:&c [TRADER]";
	public static String TRADE_PLAYER_NOTFOUND = "[PREFIX]Spieler wurde nicht gefunden.";
	public static String COMMAND_PING = "[PREFIX]Dein Ping beträgt: [PING]";
	public static String PLATE_FINISH = "[PREFIX]Herzlichen Glückwunsch, du hast es Geschaft! Dabei hast du auch noch [ADDCOINS] Coins bekommen.";
	public static String PLATE_NOPE = "&c✘";
	public static String PLATE_COOLDOWN = "[PREFIX]Du bekommst in [TIME] wieder eine belohnung!";
	public static String CRAFT = "[PREFIX]Das Craften ist hier nicht erlaubt.";
	public static String NEW_LEVEL = "[PREFIX]&a[NAME] &3Ist einen Rang aufgestiegen: [RANG]";
	public static String TP_PLAYER_NOT_FOUND = "[PREFIX]Der Spieler wurde nicht gefunden.";
	public static String TP_SUCCESS_1 = "[PREFIX]Du wurdest zu [TARGET] teleportiert.";
	public static String TP_HERE_SUCCESS = "[PREFIX][TARGET] wurde zu dir teleportiert.";
	public static String TP_ALL = "[PREFIX]Alle wurde zu dir teleportiert.";
	public static String TP_SUCCESS_2 = "[PREFIX][FROM] wurde zu [TARGET] teleportiert.";
	public static String GM_SUCCESS_1 = "[PREFIX]Dein Gamemode wurde geändert.";
	public static String GM_SUCCESS_2 = "[PREFIX]Sein Gamemode wurde geändert.";
	public static String GM_SUCCESS_3 = "[PREFIX]Dein Gamemode wurde geändert von [CHANGER].";
	public static String GM_PLAYER_NOT_FOUND = "[PREFIX]Der Spieler wurde nicht gefunden.";
	public static String CLEAR = "[PREFIX]Dein Inventar wurde gecleart.";
	public static String CLEAR_PLAYER_NOT_FOUND = "[PREFIX]Der Spieler wurde nicht gefunden.";
	public static String CLEAR_REMOTE = "[PREFIX]Sein Inventar wurde gecleart.";
	public static String PORTAL = "[PREFIX]Das Portal ist nicht geöffnet.";
	public static String PORTAL_JOIN_VOTE = "[PREFIX]Es läuft zwar kein Spiel aber du kanns in der Zwischenzeit Voten.";
	public static String SUPERUSER_NORIGHTS = "[PREFIX]Du brauchst SuperUser. /su(/superuser)";
	public static String FLYSPEED_PLAYER_NOT_FOUND = "[PREFIX]Spieler wurde nicht gefunden.";
	public static String FLYSPEED_SELF_CHANGE = "[PREFIX]Dein Flyspeed wurde geändert.";
	public static String FLYSPEED_OTHER_CHANGE_1 = "[PREFIX]Sein Flyspeed wurde geändert.";
	public static String FLYSPEED_OTHER_CHANGE_2 = "[PREFIX]Dein Flyspeed wurde von [PLAYER] geändert.";
	public static String FLYSPEED_MAX_SPEED = "[PREFIX]Der maximale Flyspeed ist 10.";
	public static String MAPS_IN_ROUND = "[PREFIX]Du kannst die Maps nicht anschauen während du in einer Runde bist.";
	public static String MAPS_MAP_IS_ROUND = "[PREFIX]Die Map ist gerade belegt.";
	public static String MAPS_NO_TELEPORT = "[PREFIX]Das Teleportieren wurde abgelehnt.";
	public static String MAPS_RUN_AWAY = "[PREFIX]Bitte nicht einfach so abhauen.";
	public static String MAPS_TP_MSG = "[PREFIX]Du kannst mit /lobby wieder in die lobby gelangen.<newline>[PREFIX]Mit /maps kannst du auch noch die anderen Maps betrachten.";
	public static String BAN_PERMANENTLY_NO_REASON = "&4YOU ARE BANNED PERMANENTLY FROM FLARGO.NET <newline> &cWITHOUT A REASON. ASK AN ADMIN IN TEAMSPEAK FOR HELP IF YOU THINK THIS IS A MISTAKE.";
	public static String BAN_TEMPORARILY_NO_REASON = "&4YOU ARE BANNED TEMPORARILY FROM FLARGO.NET <newline> &cWITHOUT A REASON. ASK AN ADMIN IN TEAMSPEAK FOR HELP IF YOU THINK THIS IS A MISTAKE. <newline> RESTTIME: '[TIME]'";
	public static String BAN_TEMPORARILY = "&4YOU ARE BANNED TEMPORARILY FROM FLARGO.NET <newline> &cREASON: '[REASON]&r&c'. <newline> RESTTIME: '[TIME]'";
	public static String BAN_PERMANENTLY = "&4YOU ARE BANNED PERMANENTLY FROM FLARGO.NET <newline> &cREASON: '[REASON]&r&c'.";
	public static String BAN_CANCEL = "[PREFIX]Der Bann wurde zurückgewiesen.";
	public static String BAN_SUCCESS = "[PREFIX]Der Spieler wurde gebannt.";
	public static String TP_SPAWN = "[PREFIX]Du hast die Lobby betreten.";
	public static String ROUND_GOT_KILLED_1 = "[PREFIX]Du wurdest von [KILLER] mit [WEAPON] getötet.";
	public static String ROUND_GOT_KILLED_2 = "[PREFIX]Du wurdest von [KILLER] getötet.";
	public static String ROUND_GOT_KILLED_3 = "[PREFIX]Du wurdest von einer [WEAPON] getötet.";
	public static String AIRDROP = "[PREFIX]Es wird ein Airdrop abgeworfen!";
	public static String SERVER_START = "&cServer is still starting...";
	public static String SUPERUSER_AUTO_LOGIN = "[PREFIX]Du wurdest automatisch in den SuperUser Mode versetzt.";
	public static String ROUND_KILL_SELF = "[PREFIX]Selbstmord ist keine Lösung.";
	public static String SHOP_ITEM_MONEY_LORE = "Du hast zur Zeit [COINS] Coins.";
	public static String SHOP_ITEM_MONEY_NAME = "Coins";
	public static String SHOP_ITEM_NOTHING_NAME = "&cnix";
	public static String SHOP_ITEM_NOTHING_LORE = "";
	public static String SHOP_ITEM_CLOSE_NAME = "&cClose";
	public static String SHOP_ITEM_CLOSE_LORE = "Schließt das Inventar.";
	public static String SHOP_BUY_1_ITEM = "[PREFIX]Du hast soeben [SIZE] [ITEMNAME] gekauft.";
	public static String SHOP_BUY_2 = "[PREFIX]Du hast nicht genügend Coins.";
	public static String SHOP_BUY_3 = "[PREFIX]Es trat ein fehler auf.";
	public static String SHOP_BUY_4 = "[PREFIX]Du hast kein Platz im Inventar.";
	public static String SHOP_BUY_5_GUN = "[PREFIX]Du hast soeben [GUNNAME] mit [GUNKILLS] Kills gekauft.";
	public static String CLEAR_CHAT = "<center>&cChat wurde aufgeräumt.";
	public static String KICK_SUCCESS = "[PREFIX]Der Spieler wurde gekickt.";
	public static String KICK_REASON = "&4YOU HAVE BEEN KICKED FROM FLARGO.NET <newline> &cREASON: '[REASON]&r&c'.";
	public static String KICK_NO_REASON = "&4YOU WERE KICKED FROM FLARGO.NET";
	public static String PLAYER_NOT_FOUND = "[PREFIX]Der Spieler wurde nicht gefunden!";
	public static String TEX_DOWNLOAD = "[PREFIX]Das Texturenpaket ist für den Spielspaß essenziell.<newline>[PREFIX]Wenn es bei dir nicht automatisch heruntergeladen wurde kannst du es auch manuel downloaden.<newline>&a[LINK]";
	public static String GAME_STOP_MANUALLY = "[PREFIX]Die Runde wurde frühzeitig beendet.";
	public static String CUSTOM_GUN_BLOCK = "[PREFIX]Gekaufte Waffen können erst ab GunGameLevel [LEVEL] verwendet werden.";
	public static String GAME_WON = " <newline>[PREFIX]&c[NAME]§a hat gewonnen!<newline> ";
	public static String STATS_MSG = "Rundengespielt: [ROUNDS]<newline>Rang: [RANG]<newline>Name: [NAME]<newline>Tode: [DEATHS]<newline>Kills: [KILLS]<newline>KD: [KD]<newline>Firstjoin: [FIRSTJOIN]<newline><center>Spielzeit<newline>Days: [DAYS]<newline>Stunden: [HOURS]<newline>Minuten: [MIN]";
	public static String TEXTUREPACK = "[PREFIX]Du kannst das Texturepack hier herunterladen: [LINK]";
	public static String WINNING_ROUND = "[PREFIX]Du hast einen Fertigkeitspunkte bekommen!";
	public static String RANK_UP_SKILL_POINT = "[PREFIX]Du hast zwei Fertigkeitspunkte bekommen!";
	public static String NO_SKILL_POINTS = "[PREFIX]Du hast nich genügen Fertigkeitspunkte.";
	public static String BUY_SKILL = "[PREFIX]Du hast soeben dein Charakter verbessert.";
	public static String BUY_SKILL_MAX = "[PREFIX]Du kannst das nich weiter verbessern.";
	public static String SKILL_GROUP_NOT_SELECTED = "[PREFIX]Du musst zuerst deine Skillgruppe auswählen.";
	public static String SKILL_GROUP_SELECT = "[PREFIX]Du bist jetzt: [CHARACTER_TYPE]";
	public static String SKILL_GROUP_SELECT_NO_POINTS = "[PREFIX]Du haste zu wenig Fertigkeitspunkte für [CHARACTER_TYPE].";
	public static String SKILL_GROUP_SELECT_ALREADY = "[PREFIX]Du bist schon ein [CHARACTER_TYPE].";

	// Strings

	public static void init() {
		String configLoc = "plugins/LTD/Settings.yml";
		HashMap<String, String> hm = ConfigManager.readConfig(configLoc);
		HashMap<String, String> hm2 = (HashMap<String, String>) hm.clone();
		HashMap<String, String> in = get();
		for (String s : hm2.keySet()) {
			if (!in.containsKey(s)) {
				hm.remove(s);
			}
		}

		for (String s : in.keySet()) {
			if (!hm.containsKey(s)) {
				hm.put(s, in.get(s));
			}
		}
		Map<String, String> sorted = new TreeMap<>(hm);
		ConfigManager.setConfig(sorted, configLoc);
		Cf c = new Cf();
		for (String s : hm.keySet()) {
			set(c, s, hm.get(s));
		}
		HashMap<String, String> in2 = get();
		for (String s : in2.keySet()) {
			String text = in2.get(s);
			text = text.replace("[PREFIX]", Main.getPrefix());
			text = text.replace("<newline>", "\n");
			text = ChatColor.translateAlternateColorCodes('&', text);
			set(c, s, text);
		}
	}

	private static HashMap<String, String> get() {
		HashMap<String, String> hm = new HashMap<>();
		for (Field f : Cf.class.getFields()) {
			if (f.getType().isAssignableFrom(String.class)) {
				String strValue = null;
				try {
					strValue = (String) f.get(Cf.class);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
				hm.put(f.getName(), strValue);
			}
		}
		return hm;
	}

	private static boolean set(Object object, String fieldName, Object fieldValue) {
		Class<?> clazz = object.getClass();
		while (clazz != null) {
			try {
				Field field = clazz.getDeclaredField(fieldName);
				field.setAccessible(true);
				field.set(object, fieldValue);
				return true;
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}
		return false;
	}

	public static String rs(String toreplace) {
		String rep = new String(toreplace);
		rep = rep.replace("[PREFIX]", Main.getPrefix());
		rep = rep.replace("<newline>", "\n");
		rep = ChatColor.translateAlternateColorCodes('&', rep);
		if (rep.contains("<center>")) {
			rep = center(rep);
		}
		return rep;
	}

	public static String rs(String toreplace, final Object... replacement) {
		toreplace = new String(toreplace);
		if ((replacement.length % 2) != 0) {
			throw new IllegalArgumentException("Replacement has to be even.");
		}
		for (int i = 0; i < replacement.length; i++) {
			String first = replacement[i].toString();
			i++;
			String sec = replacement[i].toString();
			toreplace = toreplace.replace(first, sec);
		}
		toreplace = toreplace.replace("[PREFIX]", Main.getPrefix());
		toreplace = toreplace.replace("<newline>", "\n");
		if (toreplace.contains("<center>")) {
			toreplace = center(toreplace);
		}
		return toreplace;
	}

	public static void rsBC(final String rep, final Object... replacement) {
		AsyncThreadWorkers.submitWork(() -> {
			String toreplace = new String(rep);
			if ((replacement.length % 2) != 0) {
				throw new IllegalArgumentException("Replacement has to be even.");
			}
			for (int i = 0; i < replacement.length; i++) {
				String first = replacement[i].toString();
				i++;
				String sec = replacement[i].toString();
				toreplace = toreplace.replace(first, sec);
			}
			if (toreplace.contains("<center>")) {
				toreplace = center(toreplace);
			}
			for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
				p.sendMessage(toreplace);
			}
		});

	}

	public static void rsBC(final String rep) {
		AsyncThreadWorkers.submitWork(() -> {
			String toreplace = new String(rep);
			if (toreplace.contains("<center>")) {
				toreplace = center(toreplace);
			}
			for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
				p.sendMessage(toreplace);
			}
		});

	}

	public static String rs(String toreplace, final Player p) {
		toreplace = new String(toreplace);
		toreplace = toreplace.replace("[NAME]", p.getName());
		if (toreplace.contains("[COINS]")) {
			toreplace = toreplace.replace("[COINS]", SQLCollection.getPlayer(p).getCoins() + "");
		}
		if (toreplace.contains("[SP]")) {
			toreplace = toreplace.replace("[SP]", SQLCollection.getPlayer(p).getSkillPoints() + "");
		}
		if (toreplace.contains("<center>")) {
			toreplace = center(toreplace);
		}
		return toreplace;
	}

	public static String rs(String toreplace, final Player p, final Object... replacement) {
		toreplace = new String(toreplace);
		toreplace = toreplace.replace("[NAME]", p.getName());
		if (toreplace.contains("[COINS]")) {
			toreplace = toreplace.replace("[COINS]", SQLCollection.getPlayer(p).getCoins() + "");
		}
		if (toreplace.contains("[SP]")) {
			toreplace = toreplace.replace("[SP]", SQLCollection.getPlayer(p).getSkillPoints() + "");
		}
		if ((replacement.length % 2) != 0) {
			throw new IllegalArgumentException("Replacement has to be even.");
		}
		for (int i = 0; i < replacement.length; i++) {
			String first = replacement[i].toString();
			i++;
			String sec = replacement[i].toString();
			toreplace = toreplace.replace(first, sec);
		}
		if (toreplace.contains("<center>")) {
			toreplace = center(toreplace);
		}
		return toreplace;
	}

	public static void rsS(final String rep, final Player p, final Object... replacement) {
		AsyncThreadWorkers.submitWork(() -> {
			String toreplace = new String(rep);
			toreplace = toreplace.replace("[NAME]", p.getName());
			if (toreplace.contains("[COINS]")) {
				toreplace = toreplace.replace("[COINS]", SQLCollection.getPlayer(p).getCoins() + "");
			}
			if (toreplace.contains("[SP]")) {
				toreplace = toreplace.replace("[SP]", SQLCollection.getPlayer(p).getSkillPoints() + "");
			}
			if ((replacement.length % 2) != 0) {
				throw new IllegalArgumentException("Replacement has to be even.");
			}
			for (int i = 0; i < replacement.length; i++) {
				String first = replacement[i].toString();
				i++;
				String sec = replacement[i].toString();
				toreplace = toreplace.replace(first, sec);
			}
			if (toreplace.contains("<center>")) {
				toreplace = center(toreplace);
			}
			p.sendMessage(toreplace);
		});

	}

	public static void rsS(final String rep, final Player p) {
		AsyncThreadWorkers.submitWork(() -> {
			String toreplace = new String(rep);
			toreplace = toreplace.replace("[NAME]", p.getName());
			if (toreplace.contains("[COINS]")) {
				toreplace = toreplace.replace("[COINS]", SQLCollection.getPlayer(p).getCoins() + "");
			}
			if (toreplace.contains("[SP]")) {
				toreplace = toreplace.replace("[SP]", SQLCollection.getPlayer(p).getSkillPoints() + "");
			}
			if (toreplace.contains("<center>")) {
				toreplace = center(toreplace);
			}
			p.sendMessage(toreplace);
		});

	}

	private static String center(String re) {
		String[] data = re.split("\\r?\\n");
		int i = 0;
		for (String s : data) {
			if (s.contains("<center>")) {
				s = s.replace("<center>", "");
				s = MessageUtil.getCenteredMessage(s);
				data[i] = s;
			}
			i++;
		}
		String finalstring = "";
		for (String s : data) {
			if (finalstring == "") {
				finalstring = s;
			} else {
				finalstring = finalstring + "\n" + s;
			}

		}
		return finalstring;
	}

	public static String replaceColorCodes(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}

}
