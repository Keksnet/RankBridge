package de.neo.rankbridge.teamspeak.listener;

import java.util.ArrayList;
import java.util.Map;

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
import de.neo.rankbridge.shared.manager.GlobalManager;
import de.neo.rankbridge.shared.manager.MinecraftManager;
import de.neo.rankbridge.shared.manager.PermissionManager;
import de.neo.rankbridge.shared.util.MultiVar;
import de.neo.rankbridge.teamspeak.TeamSpeakMain;

public class TSListener implements TS3Listener {
	
	private TeamSpeakMain main;
	private MinecraftManager mcmgr;
	
	public TSListener() {
		this.main = (TeamSpeakMain) GlobalManager.getInstance().getServiceManager().getService(TeamSpeakMain.class);
		this.mcmgr = MinecraftManager.getInstance();
	}

	@Override
	public void onTextMessage(TextMessageEvent e) {
		MultiVar vars = this.main.getCode(e.getMessage().replace(" ", ""));
		if(vars != null) {
			try {
				Client c = this.main.getAPI().getClientByUId(e.getInvokerUniqueId()).get();
				this.main.getAPI().addClientToServerGroup(Integer.valueOf(vars.get(0)), c.getDatabaseId());
				this.main.getAPI().addClientToServerGroup(Integer.valueOf(this.mcmgr.getString("teamspeak.verified_group")), c.getDatabaseId());
				Map<String, String> map = this.main.getAPI().getCustomClientProperties(c.getDatabaseId()).get();
				map.put(ClientProperty.CLIENT_DESCRIPTION.name(), "UUID: " + vars.get(0) + " | Name: " + this.mcmgr.getName(vars.get(1)));
				this.main.getAPI().setCustomClientProperties(c.getDatabaseId(), map);
				String verified = this.mcmgr.getString("messages.teamspeak.verified").replace("%playername%", this.mcmgr.getName(vars.get(1)).replace("%uuid%", vars.get(1)));
				this.main.getAPI().sendPrivateMessage(c.getId(), verified);
			}catch(InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onClientJoin(ClientJoinEvent e) {
		if(!e.getClientServerGroups().contains(this.mcmgr.getString("teamspeak.verified_group"))) {
			try {
				Client client = this.main.getAPI().getClientByUId(e.getUniqueClientIdentifier()).get();
				this.main.getAPI().sendPrivateMessage(client.getId(), this.mcmgr.getString("messages.teamspeak.verify_info"));
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
		}else {
			PermissionManager mgr = PermissionManager.getInstance();
			String uuid = e.getClientDescription().split("|")[0].replace("UUID: ", "").replace(" ", "");
			if(!mgr.checkTeamspeak(e.getUniqueClientIdentifier(), uuid)) {
				for(String s : e.getClientServerGroups().split(";")) {
					if(mgr.isTeamspeakGroup(Integer.valueOf(s))) {
						this.main.getAPI().removeClientFromServerGroup(Integer.valueOf(s), e.getClientDatabaseId());
					}
				}
				for(String val : (ArrayList<String>) this.mcmgr.getList("teamspeak.groups")) {
					String k = (String) val.split(",")[0];
					String v = (String) val.split(",")[1].replace(" ", "");
					if(this.mcmgr.hasPermission(uuid, k)) {
						this.main.getAPI().addClientToServerGroup(Integer.valueOf(v), e.getClientDatabaseId());
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
