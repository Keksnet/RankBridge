package de.neo.rankbridge.teamspeak.listener;

import java.util.List;
import java.util.UUID;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import de.neo.rankbridge.discord.DiscordMain;
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
					Client[] css = new Client[] {};
					List<Client> cs = service.getAPI().getClients().get();
					for(int i = 0; i < cs.size(); i++) {
						Client c = cs.get(i);
						System.out.println(ip);
						if(c.getIp().equals(ip)) {
							css[css.length] = c;
						}
					}
					if(css.length > 0) {
						if(css.length == 1) {
							service.getAPI().addClientToServerGroup(group, css[0].getDatabaseId());
							service.getAPI().addClientToServerGroup(MinecraftManager.getInstance().getInt("teamspeak.verified_group"), css[0].getDatabaseId());
							MinecraftManager mgr = MinecraftManager.getInstance();
							String verified = mgr.getString("messages.teamspeak.verified").replace("%playername%", mgr.getName(uuid.toString()).replace("%uuid%", uuid.toString()));
							service.getAPI().sendPrivateMessage(css[0].getId(), verified);
							service.removeCode(code);
							BridgeMessage<String> msg = new BridgeMessage<>(ConversationMember.DISCORD);
							msg.setContent("VERIFIED;" + code + ";" + css[0].getUniqueIdentifier());
							BridgeMessageSendEvent sendEvent = new BridgeMessageSendEvent(DiscordMain.class, msg);
							GlobalManager.getInstance().getEventHandler().executeEvent(sendEvent);
						}
					}
				}catch(InterruptedException e1) {
					e1.printStackTrace();
				}
				GlobalManager.getInstance().getEventHandler().executeEvent(receivedEvent);
			}
		}else if(e.getMessage().getContentUniversal().getAsString().startsWith("VERIFIED;")) {
			String[] args = e.getMessage().getContentUniversal().getAsString().split(";");
			if(args.length == 3) {
				String code = args[1];
				service.removeCode(code);
				GlobalManager.getInstance().getEventHandler().executeEvent(receivedEvent);
			}
		}
	}

}
