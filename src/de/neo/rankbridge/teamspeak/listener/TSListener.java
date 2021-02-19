package de.neo.rankbridge.teamspeak.listener;

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
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.neo.rankbridge.shared.manager.GlobalManager;
import de.neo.rankbridge.teamspeak.TeamSpeakMain;

public class TSListener implements TS3Listener {
	
	private TeamSpeakMain main;
	
	public TSListener() {
		this.main = (TeamSpeakMain) GlobalManager.getInstance().getServiceManager().getService(TeamSpeakMain.class);
	}

	@Override
	public void onTextMessage(TextMessageEvent e) {
		/* Coming soon! */
	}

	@Override
	public void onClientJoin(ClientJoinEvent e) {
		if(!e.getClientServerGroups().contains(this.main.getString("teamspeak.verified_group"))) {
			try {
				Client client = this.main.getAPI().getClientByUId(e.getUniqueClientIdentifier()).get();
				this.main.getAPI().sendPrivateMessage(client.getId(), this.main.getString("messages.teamspeak.verify_info"));
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
		}
	}

	@Override
	public void onClientLeave(ClientLeaveEvent e) {
	}

	@Override
	public void onServerEdit(ServerEditedEvent e) {
	}

	@Override
	public void onChannelEdit(ChannelEditedEvent e) {
	}

	@Override
	public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent e) {
	}

	@Override
	public void onClientMoved(ClientMovedEvent e) {
	}

	@Override
	public void onChannelCreate(ChannelCreateEvent e) {
	}

	@Override
	public void onChannelDeleted(ChannelDeletedEvent e) {
	}

	@Override
	public void onChannelMoved(ChannelMovedEvent e) {
	}

	@Override
	public void onChannelPasswordChanged(ChannelPasswordChangedEvent e) {
	}

	@Override
	public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent e) {
	}

}
