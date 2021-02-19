package de.neo.rankbridge.teamspeak.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class TSListener implements TS3Listener {
	
	private Logger l;
	
	public TSListener() {
		this.l = LoggerFactory.getLogger("TSBot");
	}

	@Override
	public void onTextMessage(TextMessageEvent e) {
	}

	@Override
	public void onClientJoin(ClientJoinEvent e) {
		l.warn(e.getClientServerGroups());
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
