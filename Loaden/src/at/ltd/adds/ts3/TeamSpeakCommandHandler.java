package at.ltd.adds.ts3;

import java.util.Arrays;
import java.util.HashMap;

import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;

public class TeamSpeakCommandHandler {
	
	public static void handl(TextMessageEvent e) {
		if (e.getTargetMode() == TextMessageTargetMode.CLIENT) {
			String msg = e.getMessage();
			int clientid = e.getInvokerId();
			ClientInfo ci = TeamSpeak.getInfo(clientid);
			if (!msg.startsWith("!")) {
				TeamSpeak.sendMessgae(
						"A command starts here with a call sign(!). If you want to talk to me, I am sorry i am not developed far enoth to do that.",
						clientid);
				return;
			}

			if (msg.startsWith("!createchannel")) {
				if (TeamSpeak.API.getChannelByNameExact(e.getInvokerName() + " Channel", true) != null) {
					TeamSpeak.sendMessgae("Sorry, you already got a channel.", clientid);
					return;
				}
				final HashMap<ChannelProperty, String> properties = new HashMap<>();
				properties.put(ChannelProperty.CHANNEL_FLAG_TEMPORARY, "1");
				properties.put(ChannelProperty.CHANNEL_CODEC_IS_UNENCRYPTED, "0");
				properties.put(ChannelProperty.CHANNEL_CODEC_QUALITY, "10");
				int defaultChannelId = TeamSpeak.API.getChannelByNameExact("[*spacer187]_", true).getId();
				properties.put(ChannelProperty.CPID, String.valueOf(defaultChannelId));
				int ChannelID = TeamSpeak.API.createChannel(e.getInvokerName() + " Channel", properties);
				TeamSpeak.ASYNC_API.moveClient(clientid, ChannelID);
				TeamSpeak.ASYNC_API.setClientChannelGroup(5, ChannelID, ci.getDatabaseId());
				TeamSpeak.sendMessgae("So, here is your privat channel. You also can change some settings.", clientid);
				TeamSpeak.ASYNC_API.moveClient(TeamSpeak.MY_ID, defaultChannelId);
				return;
			}
			
			int[] groups = ci.getServerGroups();

			if (msg.startsWith("!nomove")) {
				if (contains(groups, 11)) {
					TeamSpeak.ASYNC_API.removeClientFromServerGroup(11, ci.getDatabaseId());
					TeamSpeak.sendMessgae("Removed you from the servergroup: Move", clientid);
				} else {
					TeamSpeak.ASYNC_API.addClientToServerGroup(11, ci.getDatabaseId());
					TeamSpeak.sendMessgae("Added you to the servergroup: Move", clientid);
				}
				return;
			}

			if (msg.startsWith("!nopoke")) {
				if (contains(groups, 9)) {
					TeamSpeak.ASYNC_API.removeClientFromServerGroup(9, ci.getDatabaseId());
					TeamSpeak.sendMessgae("Removed you from the servergroup: NoPoke", clientid);
				} else {
					TeamSpeak.ASYNC_API.addClientToServerGroup(9, ci.getDatabaseId());
					TeamSpeak.sendMessgae("Added you to the servergroup: NoPoke", clientid);
				}
				return;
			}
			
			if (msg.startsWith("!nodisturb")) {
				if (contains(groups, 10)) {
					TeamSpeak.ASYNC_API.removeClientFromServerGroup(10, ci.getDatabaseId());
					TeamSpeak.sendMessgae("Removed you from the servergroup: NoDisturb", clientid);
				} else {
					TeamSpeak.ASYNC_API.addClientToServerGroup(10, ci.getDatabaseId());
					TeamSpeak.sendMessgae("Added you to the servergroup: NoDisturb", clientid);
				}
				return;
			}
			
			if (msg.startsWith("!showserverchat")) {
				if (contains(groups, 25)) {
					TeamSpeak.ASYNC_API.removeClientFromServerGroup(25, ci.getDatabaseId());
					TeamSpeak.sendMessgae("Removed you from the servergroup: Serverchat", clientid);
				} else {
					TeamSpeak.ASYNC_API.addClientToServerGroup(25, ci.getDatabaseId());
					TeamSpeak.sendMessgae("Added you to the servergroup: Serverchat", clientid);
				}
				return;
			}

		}
	}

	public static boolean contains(final int[] arr, final int key) {
		return Arrays.stream(arr).anyMatch(i -> i == key);
	}
	
}
