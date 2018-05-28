package at.ltd.adds.ts3;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;

import at.ltd.Main;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.events.custom.EventPlayerJoinAsync;
import at.ltd.events.custom.EventPlayerQuitAsync;
import at.ltd.gungame.ranks.GRankPerm;

public class TeamSpeakWatcher {

	public static void init() {
		online();

		Main.registerListener(new Listener() {
			@EventHandler(priority = EventPriority.LOW)
			public void on(AsyncPlayerChatEvent e) {
				AsyncThreadWorkers.submitWork(() -> {
					if (!e.isCancelled()) {
						Player p = e.getPlayer();
						String msg = e.getMessage();
						String newmsg = "[" + p.getName() + " | " + GRankPerm.getRank(p).getPerm() + " ] " + msg;
						for (ClientInfo ci : TeamSpeak.getAllClientInfos().values()) {
							if (contains(ci.getServerGroups(), 25)) {
								TeamSpeak.sendMessgae(newmsg, ci.getId());
							}
						}
					}
				});
			}

			@EventHandler()
			public void on(EventPlayerJoinAsync e) {
				String ip = TeamSpeak.getIPViaPlayer(e.getPlayer());
				if (TeamSpeak.isOnlineTS(ip)) {
					ClientInfo ci = TeamSpeak.getClientInfoViaIP(ip);
					if (!contains(ci.getServerGroups(), TeamSpeak.MC_ONL_GROUP_ID)) {
						TeamSpeak.addClientToServerGroup(TeamSpeak.MC_ONL_GROUP_ID, ci);
					}
				}
				ClientInfo ci = TeamSpeak.getClientInfoFromVeriPlayer(e.getPlayer());
				if (ci != null) {
					TeamSpeak.sendMessgae("Willkommen zurück [b]" + e.getPlayer().getName() + "[/b]!", ci);
				}
			}

			@EventHandler(priority = EventPriority.HIGH)
			public void on(EventPlayerQuitAsync e) {
				String ip = TeamSpeak.getIPViaPlayer(e.getPlayer());
				if (TeamSpeak.isOnlineTS(ip)) {
					ClientInfo ci = TeamSpeak.getClientInfoViaIP(ip);
					if (contains(ci.getServerGroups(), TeamSpeak.MC_ONL_GROUP_ID)) {
						TeamSpeak.removeClientFromServerGroup(TeamSpeak.MC_ONL_GROUP_ID, ci);
					}
				}
			}
		});
	}

	private static void online() {
		AsyncThreadWorkers.submitSchedulerWorkSec(() -> {
			HashMap<Integer, ClientInfo> hm = TeamSpeak.getAllClientInfos();
			for (ClientInfo ci : hm.values()) {
				if (ci.getNickname().startsWith("LTD-NET")) {
					continue;
				}
				if (TeamSpeak.isOnlineMC(ci.getIp())) {
					if (!contains(ci.getServerGroups(), TeamSpeak.MC_ONL_GROUP_ID)) {
						TeamSpeak.addClientToServerGroup(TeamSpeak.MC_ONL_GROUP_ID, ci);
					}
				} else {
					if (contains(ci.getServerGroups(), TeamSpeak.MC_ONL_GROUP_ID)) {
						TeamSpeak.removeClientFromServerGroup(TeamSpeak.MC_ONL_GROUP_ID, ci);
					}
				}
			}
		}, 0, 10);
	}

	public static boolean contains(final int[] arr, final int key) {
		return Arrays.stream(arr).anyMatch(i -> i == key);
	}

}
