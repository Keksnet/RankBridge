package de.neo.rankbridge.discord.listener;

import java.util.UUID;

import de.neo.rankbridge.discord.DiscordMain;
import de.neo.rankbridge.shared.event.BridgeEventListener;
import de.neo.rankbridge.shared.event.events.BridgeEvent;
import de.neo.rankbridge.shared.event.events.message.BridgeMessageSendEvent;
import de.neo.rankbridge.shared.manager.GlobalManager;
import de.neo.rankbridge.shared.message.BridgeMessage.ConversationMember;

public class MessageSendListener implements BridgeEventListener {
	
	@Override
	public void execute(BridgeEvent event) {
		BridgeMessageSendEvent e = (BridgeMessageSendEvent) event;
		Boolean allowed = false;
		if(e.getMessage().getSender().equals(ConversationMember.DISCORD)) {
			return;
		}
		for(ConversationMember mem : e.getMessage().getReceiver()) {
			if(mem.equals(ConversationMember.DISCORD)) {
				allowed = true;
				break;
			}
		}
		if(!allowed) {
			return;
		}
		DiscordMain service = (DiscordMain) GlobalManager.getInstance().getServiceManager().getService(DiscordMain.class);
		if(e.getMessage().getContentUniversal().getAsString().startsWith("ADD_CODE;")) {
			String[] args = e.getMessage().getContentUniversal().getAsString().split(";");
			if(args.length == 6) {
				System.out.println(e.getMessage().getContentUniversal().getAsString());
				String code = args[1];
				Long group = Long.valueOf(args[3]);
				UUID uuid = UUID.fromString(args[4]);
				service.addCode(code, group, uuid);
			}
		}else if(e.getMessage().getContentUniversal().getAsString().startsWith("VERIFIED;")) {
			String[] args = e.getMessage().getContentUniversal().getAsString().split(";");
			if(args.length == 3) {
				String code = args[1];
				service.removeCode(code);
			}
		}
	}
}
