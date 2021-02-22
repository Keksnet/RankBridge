package de.neo.rankbridge.teamspeak.listener;

import java.util.List;
import java.util.UUID;

import com.github.theholywaffle.teamspeak3.api.ClientProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.VirtualServerInfo;

import de.neo.rankbridge.shared.event.BridgeEventListener;
import de.neo.rankbridge.shared.event.events.BridgeEvent;
import de.neo.rankbridge.shared.event.events.message.BridgeMessageReceivedEvent;
import de.neo.rankbridge.shared.event.events.message.BridgeMessageSendEvent;
import de.neo.rankbridge.shared.manager.GlobalManager;
import de.neo.rankbridge.shared.manager.MinecraftManager;
import de.neo.rankbridge.shared.message.BridgeMessage;
import de.neo.rankbridge.shared.message.BridgeMessage.ConversationMember;
import de.neo.rankbridge.teamspeak.TeamSpeakMain;

public class MessageSendListener implements BridgeEventListener {

	@Override
	public void execute(BridgeEvent event) {
		BridgeMessageSendEvent e = (BridgeMessageSendEvent) event;
		Boolean allowed = false;
		if(e.getMessage().getSender().equals(ConversationMember.TEAMSPEAK)) {
			return;
		}
		for(ConversationMember mem : e.getMessage().getReceiver()) {
			if(mem.equals(ConversationMember.TEAMSPEAK)) {
				allowed = true;
				break;
			}
		}
		if(!allowed) {
			return;
		}
		TeamSpeakMain service = (TeamSpeakMain) GlobalManager.getInstance().getServiceManager().getService(TeamSpeakMain.class);
		BridgeMessageReceivedEvent receivedEvent = new BridgeMessageReceivedEvent(TeamSpeakMain.class, e.getMessage());
		if(e.getMessage().getContentUniversal().getAsString().startsWith("ADD_CODE;")) {
			String[] args = e.getMessage().getContentUniversal().getAsString().split(";");
			if(args.length == 6) {
				String code = args[1];
				Integer group = Integer.valueOf(args[2]);
				UUID uuid = UUID.fromString(args[4]);
				String ip = args[5];
				service.addCode(code, group, uuid, ip);
				try {
					VirtualServerInfo info = service.getAPI().getServerInfo().get();
					Client[] css = new Client[info.getMaxClients()];
					List<Client> cs = service.getAPI().getClients().get();
					Integer clients = 0;
					for(int i = 0; i < cs.size(); i++) {
						Client c = cs.get(i);
						if(c.getIp().equals(ip) && c.isRegularClient()) {
							css[clients] = c;
							clients++;
						}
					}
					if(clients > 0) {
						if(clients == 1) {
							MinecraftManager mgr = MinecraftManager.getInstance();
							service.getAPI().addClientToServerGroup(group, css[0].getDatabaseId());
							service.getAPI().addClientToServerGroup(MinecraftManager.getInstance().getInt("teamspeak.verified_group"), css[0].getDatabaseId());
							service.getAPI().editClient(css[0].getId(), ClientProperty.CLIENT_DESCRIPTION, "UUID: " + uuid + " | Name: " + mgr.getName(uuid.toString()));
							String verified = mgr.getString("messages.teamspeak.verified").replace("%playername%", mgr.getName(uuid.toString()).replace("%uuid%", uuid.toString()));
							service.getAPI().sendPrivateMessage(css[0].getId(), verified);
							service.removeCode(code);
							BridgeMessage<String> msg = new BridgeMessage<>(ConversationMember.TEAMSPEAK);
							msg.setContent("VERIFIED;" + code + ";" + css[0].getUniqueIdentifier() + ";" + uuid);
							BridgeMessageSendEvent sendEvent = new BridgeMessageSendEvent(TeamSpeakMain.class, msg);
							GlobalManager.getInstance().getEventHandler().executeEvent(sendEvent);
						}else {
							MinecraftManager mgr = MinecraftManager.getInstance();
							Boolean found = false;
							String name = mgr.getName(uuid.toString());
							for(Client c1 : css) {
								if(c1 != null) {
									if(c1.getNickname().contains(name)) {
										if(found) {
											found = false;
											System.out.println("break");
											break;
										}
										System.out.println("weiter");
										found = true;
										service.getAPI().addClientToServerGroup(group, c1.getDatabaseId());
										service.getAPI().addClientToServerGroup(MinecraftManager.getInstance().getInt("teamspeak.verified_group"), c1.getDatabaseId());
										service.getAPI().editClient(c1.getId(), ClientProperty.CLIENT_DESCRIPTION, "UUID: " + uuid + " | Name: " + mgr.getName(uuid.toString()));
										String verified = mgr.getString("messages.teamspeak.verified").replace("%playername%", mgr.getName(uuid.toString()).replace("%uuid%", uuid.toString()));
										service.getAPI().sendPrivateMessage(c1.getId(), verified);
										service.removeCode(code);
										BridgeMessage<String> msg = new BridgeMessage<>(ConversationMember.TEAMSPEAK);
										msg.setContent("VERIFIED;" + code + ";" + c1.getUniqueIdentifier() + ";" + uuid);
										BridgeMessageSendEvent sendEvent = new BridgeMessageSendEvent(TeamSpeakMain.class, msg);
										GlobalManager.getInstance().getEventHandler().executeEvent(sendEvent);
									}
								}
							}
							if(!found) {
								BridgeMessage<String> msg = new BridgeMessage<>(ConversationMember.TEAMSPEAK);
								msg.setContent("REQUEST_CODE;" + code + ";" + uuid);
								BridgeMessageSendEvent sendEvent = new BridgeMessageSendEvent(TeamSpeakMain.class, msg);
								GlobalManager.getInstance().getEventHandler().executeEvent(sendEvent);
								for(Client c1 : css) {
									if(c1 != null) {
										service.getAPI().sendPrivateMessage(c1.getId(), mgr.getString("messages.teamspeak.verify_info"));
									}
								}
							}
						}
					}else {
						BridgeMessage<String> msg = new BridgeMessage<>(ConversationMember.TEAMSPEAK);
						msg.setContent("REQUEST_CODE;" + code + ";" + uuid);
						BridgeMessageSendEvent sendEvent = new BridgeMessageSendEvent(TeamSpeakMain.class, msg);
						GlobalManager.getInstance().getEventHandler().executeEvent(sendEvent);
					}
				}catch(InterruptedException e1) {
					e1.printStackTrace();
				}
				GlobalManager.getInstance().getEventHandler().executeEvent(receivedEvent);
			}
		}else if(e.getMessage().getContentUniversal().getAsString().startsWith("VERIFIED;")) {
			String[] args = e.getMessage().getContentUniversal().getAsString().split(";");
			if(args.length == 4) {
				String code = args[1];
				service.removeCode(code);
				GlobalManager.getInstance().getEventHandler().executeEvent(receivedEvent);
			}
		}
	}

}
