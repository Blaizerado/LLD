package at.ltd.adds.ts3;

import java.util.Arrays;

import com.github.theholywaffle.teamspeak3.api.event.ChannelCreateEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelDeletedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelDescriptionEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelPasswordChangedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.PrivilegeKeyUsedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ServerEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3Listener;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;

public class TeamSpeakEvent implements TS3Listener {

	@Override
	public void onChannelCreate(ChannelCreateEvent arg0) {
	}

	@Override
	public void onChannelDeleted(ChannelDeletedEvent arg0) {
	}

	@Override
	public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent arg0) {
	}

	@Override
	public void onChannelEdit(ChannelEditedEvent arg0) {
	}

	@Override
	public void onChannelMoved(ChannelMovedEvent arg0) {
	}

	@Override
	public void onChannelPasswordChanged(ChannelPasswordChangedEvent arg0) {
	}

	@Override
	public void onClientJoin(ClientJoinEvent e) {

		TeamSpeak.updateInfo(e.getClientId());
		ClientInfo ci = TeamSpeak.getInfo(e.getClientId());
		TeamSpeak.sendMessgae("[b]Welcome to the LTD-NET and FLARGO TeamSpeak.[/b]", e.getClientId());
		TeamSpeak.sendMessgae("[b]Commands:[/b]", e.getClientId());
		TeamSpeak.sendMessgae("[b]1. !createchannel 2. !nomove 3. !nopoke 4. !nodisturb 5. !showserverchat[/b]", e.getClientId());
		if (TeamSpeak.isOnlineMC(ci.getIp())) {
			if (!contains(ci.getServerGroups(), TeamSpeak.MC_ONL_GROUP_ID)) {
				TeamSpeak.addClientToServerGroup(TeamSpeak.MC_ONL_GROUP_ID, ci);
			}
		}

		TeamSpeakVeri.check(ci);
	}

	@Override
	public void onClientLeave(ClientLeaveEvent arg0) {
	}

	@Override
	public void onClientMoved(ClientMovedEvent arg0) {
	}

	@Override
	public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent arg0) {
	}

	@Override
	public void onServerEdit(ServerEditedEvent arg0) {
	}

	@Override
	public void onTextMessage(TextMessageEvent e) {
		TeamSpeakCommandHandler.handl(e);
	}

	public static boolean contains(final int[] arr, final int key) {
		return Arrays.stream(arr).anyMatch(i -> i == key);
	}

}
