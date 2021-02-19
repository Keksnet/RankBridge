package de.neo.rankbridge.discord.listener;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.neo.rankbridge.discord.DiscordMain;
import de.neo.rankbridge.shared.event.BridgeEventListener;
import de.neo.rankbridge.shared.event.events.BridgeEvent;
import de.neo.rankbridge.shared.event.events.message.BridgeMessageSendEvent;
import de.neo.rankbridge.shared.manager.GlobalManager;
import de.neo.rankbridge.shared.message.BridgeMessage.ConversationMember;

public class MessageSendListener implements BridgeEventListener {
	
	@Override
	public void execute(BridgeEvent event) {
		Logger l = LoggerFactory.getLogger("debug");
		l.warn("1");
		BridgeMessageSendEvent e = (BridgeMessageSendEvent) event;
		Boolean allowed = false;
		if(e.getMessage().getSender().equals(ConversationMember.DISCORD)) {
			l.warn("2");
			return;
		}
		for(ConversationMember mem : e.getMessage().getReceiver()) {
			l.warn("3");
			if(mem.equals(ConversationMember.DISCORD)) {
				l.warn("4");
				allowed = true;
				break;
			}
		}
		l.warn("5");
		if(!allowed) {
			l.warn("6");
			return;
		}
		l.warn("7");
		DiscordMain service = (DiscordMain) GlobalManager.getInstance().getServiceManager().getService(DiscordMain.class);
		if(e.getMessage().getContentUniversal().getAsString().startsWith("ADD_CODE;")) {
			l.warn("8");
			String[] args = e.getMessage().getContentUniversal().getAsString().split(";");
			l.warn(e.getMessage().getContentUniversal().getAsString());
			l.warn(args.toString());
			if(args.length == 5) {
				l.warn("9");
				String code = args[1];
				Integer group = Integer.getInteger(args[3]);
				UUID uuid = UUID.fromString(args[4]);
				service.addCode(code, group, uuid);
			}
			l.warn("10");
		}else if(e.getMessage().getContentUniversal().getAsString().startsWith("VERIFIED;")) {
			l.warn("11");
			String[] args = e.getMessage().getContentUniversal().getAsString().split(";");
			if(args.length == 3) {
				l.warn("12");
				String code = args[1];
				service.removeCode(code);
			}
			l.warn("13");
		}
	}
}
