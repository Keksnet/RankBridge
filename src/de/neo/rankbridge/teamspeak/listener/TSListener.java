package de.neo.rankbridge.teamspeak.listener;

import java.util.ArrayList;
import com.github.theholywaffle.teamspeak3.api.ClientProperty;
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

import de.neo.rankbridge.discord.DiscordMain;
import de.neo.rankbridge.shared.event.events.message.BridgeMessageSendEvent;
import de.neo.rankbridge.shared.manager.GlobalManager;
import de.neo.rankbridge.shared.manager.MinecraftManager;
import de.neo.rankbridge.shared.manager.PermissionManager;
import de.neo.rankbridge.shared.message.BridgeMessage;
import de.neo.rankbridge.shared.message.BridgeMessage.ConversationMember;
import de.neo.rankbridge.shared.util.MultiVar;
import de.neo.rankbridge.teamspeak.TeamSpeakMain;

public class TSListener implements TS3Listener {
	
	private TeamSpeakMain main;
	private MinecraftManager mcmgr;
	
	public TSListener(TeamSpeakMain main) {
		this.main = main;
		this.mcmgr = MinecraftManager.getInstance();
	}

	@Override
	public void onTextMessage(TextMessageEvent e) {
		MultiVar vars = this.main.getCode(e.getMessage().replace(" ", ""));
		if(vars != null) {
			try {
				Client c = this.main.getAPI().getClientByUId(e.getInvokerUniqueId()).get();
				this.main.getAPI().addClientToServerGroup(Integer.valueOf(vars.get(0)), c.getDatabaseId());
				this.main.getAPI().addClientToServerGroup(this.mcmgr.getInt("teamspeak.verified_group"), c.getDatabaseId());
				this.main.getAPI().editClient(c.getId(), ClientProperty.CLIENT_DESCRIPTION, "UUID: " + vars.get(1) + " | Name: " + this.mcmgr.getName(vars.get(1)));
				String verified = this.mcmgr.getString("messages.teamspeak.verified").replace("%playername%", this.mcmgr.getName(vars.get(1)).replace("%uuid%", vars.get(1)));
				this.main.getAPI().sendPrivateMessage(c.getId(), verified);
				this.main.removeCode(e.getMessage().replace(" ", ""));
				BridgeMessage<String> msg = new BridgeMessage<>(ConversationMember.DISCORD);
				msg.setContent("VERIFIED;" + e.getMessage().replace(" ", "") + ";" + e.getInvokerUniqueId());
				BridgeMessageSendEvent sendEvent = new BridgeMessageSendEvent(DiscordMain.class, msg);
				GlobalManager.getInstance().getEventHandler().executeEvent(sendEvent);
			}catch(InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onClientJoin(ClientJoinEvent e) {
		if(!e.getClientServerGroups().contains(String.valueOf(this.mcmgr.getInt("teamspeak.verified_group")))) {
			this.main.getAPI().sendPrivateMessage(e.getClientId(), this.mcmgr.getString("messages.teamspeak.verify_info"));
		}else {
			PermissionManager mgr = PermissionManager.getInstance();
			String uuid = e.getClientDescription().split("[|]")[0].replace("UUID: ", "").replace(" ", "");
			System.out.println(uuid + " => " + e.getClientDescription());
			if(!mgr.checkTeamspeak(e.getClientServerGroups(), uuid)) {
				for(String s : e.getClientServerGroups().split(",")) {
					if(mgr.isTeamspeakGroup(Integer.valueOf(s))) {
						this.main.getAPI().removeClientFromServerGroup(Integer.valueOf(s), e.getClientDatabaseId());
						this.main.getAPI().removeClientFromServerGroup(this.mcmgr.getInt("teamspeak.verified_group"), e.getClientDatabaseId());
					}
				}
				for(String val : (ArrayList<String>) this.mcmgr.getList("teamspeak.groups")) {
					String k = (String) val.split(",")[0];
					String v = (String) val.split(",")[1].replace(" ", "");
					if(this.mcmgr.hasPermission(uuid, k)) {
						this.main.getAPI().addClientToServerGroup(Integer.valueOf(v), e.getClientDatabaseId());
						this.main.getAPI().addClientToServerGroup(this.mcmgr.getInt("teamspeak.verified_group"), e.getClientDatabaseId());
						return;
					}
				}
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
