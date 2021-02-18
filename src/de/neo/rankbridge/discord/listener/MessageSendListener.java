package de.neo.rankbridge.discord.listener;

import java.util.UUID;

import de.neo.rankbridge.shared.event.BridgeEventListener;
import de.neo.rankbridge.shared.event.events.BridgeEvent;
import de.neo.rankbridge.shared.event.events.message.BridgeMessageSendEvent;
import de.neo.rankbridge.shared.manager.GlobalManager;
import de.neo.rankbridge.shared.message.BridgeMessage.ConversationMember;
import de.neo.rankbridge.teamspeak.TeamSpeakMain;

public class MessageSendListener implements BridgeEventListener {
	
	@Override
	public void execute(BridgeEvent event) {
		BridgeMessageSendEvent e = (BridgeMessageSendEvent) event;
		Boolean allowed = false;
		for(ConversationMember mem : e.getMessage().getReceiver()) {
			if(mem.equals(ConversationMember.DISCORD)) {
				allowed = true;
				break;
			}
		}
		if(!allowed) {
			return;
		}
		if(e.getMessage().getContentUniversal().getAsString().startsWith("ADD_CODE-")) {
			String[] args = e.getMessage().getContentUniversal().getAsString().split("-");
			if(args.length == 4) {
				String code = args[1];
				Integer group = Integer.getInteger(args[2]);
				UUID uuid = UUID.fromString(args[3]);
				TeamSpeakMain service = (TeamSpeakMain) GlobalManager.getInstance().getServiceManager().getService(TeamSpeakMain.class);
				service.addCode(code, group, uuid);
			}
		}
	}
}
