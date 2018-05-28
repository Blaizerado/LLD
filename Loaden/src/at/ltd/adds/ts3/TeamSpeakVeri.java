package at.ltd.adds.ts3;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.github.theholywaffle.teamspeak3.api.ClientProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;

import at.ltd.Main;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.events.custom.EventPlayerJoinAsync;

public class TeamSpeakVeri implements Listener {

	public static HashMap<String, String> VERIS = new HashMap<>();

	public static void init() {
		AsyncThreadWorkers.submitSyncWork(() -> Main.registerListener(new TeamSpeakVeri()));
	}

	@EventHandler
	public void on(EventPlayerJoinAsync e) {
		String ip = TeamSpeak.getIPViaPlayer(e.getPlayer());
		if (TeamSpeak.isOnlineTS(ip)) {
			ClientInfo ci = TeamSpeak.getClientInfoViaIP(ip);
			if (!TeamSpeakEvent.contains(ci.getServerGroups(), TeamSpeak.MC_VER_GROUP_ID)) {
				TeamSpeak.sendMessgae("Möchtest du dich mit deinem Minecraftaccount verbinden?", ci.getId());
				String key = randomString(10);
				TeamSpeak.sendMessgae("Wenn ja, gib in Minecraft folgenden Command ein: /ts " + key, ci.getId());
				VERIS.put(key, ci.getUniqueIdentifier());
			}
		}
	}

	public static void veri(Player p, String key) {
		if (VERIS.containsKey(key)) {
			ClientInfo ci = TeamSpeak.API.getClientByUId(VERIS.get(key));
			if (TeamSpeakEvent.contains(ci.getServerGroups(), TeamSpeak.MC_VER_GROUP_ID)) {
				p.sendMessage(Main.getPrefix() + "Du bist bereits regestriert.");
				return;
			}
			if (ci.getNickname() != null) {
				TeamSpeak.ASYNC_API.editClient(ci.getId(), Collections.singletonMap(ClientProperty.CLIENT_DESCRIPTION,
						"Minecraft Verified | UUID: " + p.getUniqueId().toString()));
				TeamSpeak.addClientToServerGroup(TeamSpeak.MC_VER_GROUP_ID, ci);
				TeamSpeak.sendMessgae("Du hast dich soeben erfolgreich mit deinem Minecraft Account verbunden.",
						ci.getId());
				VERIS.remove(key);
				p.sendMessage(Main.getPrefix() + "Du hast dich soeben erfolgreich verifiziert.");
			} else {
				p.sendMessage(Main.getPrefix() + "Du musst im TeamSpeak Online sein.");
			}
		} else {
			p.sendMessage(Main.getPrefix() + "Nicht gültig.");
		}
	}

	public static void check(ClientInfo ci) {
		AsyncThreadWorkers.submitWork(() -> {
			if (!TeamSpeakEvent.contains(ci.getServerGroups(), TeamSpeak.MC_VER_GROUP_ID)) {
				if (TeamSpeak.isOnlineMC(ci.getIp())) {
					TeamSpeak.sendMessgae("Möchtest du dich mit deinem Minecraftaccount verbinden?", ci.getId());
					String key = randomString(10);
					TeamSpeak.sendMessgae("Wenn ja, gib in Minecraft folgenden Command ein: /ts " + key, ci.getId());
					VERIS.put(key, ci.getUniqueIdentifier());
				}
			}
		});
	}

	static int i = 0;
	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static SecureRandom rnd = new SecureRandom();

	public static String randomString(int len) {
		StringBuilder sb = new StringBuilder(len);
		i++;
		for (int i = 0; i < len; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return i + sb.toString();
	}

}
