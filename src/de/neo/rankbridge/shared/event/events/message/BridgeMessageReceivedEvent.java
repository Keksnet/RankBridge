package de.neo.rankbridge.shared.event.events.message;

import de.neo.rankbridge.shared.manager.services.BridgeService;
import de.neo.rankbridge.shared.message.BridgeMessage;

public class BridgeMessageReceivedEvent extends BridgeMessageEvent{

	public BridgeMessageReceivedEvent(Class<? extends BridgeService> sender, BridgeMessage<?> message) {
		super(sender, message);
	}
}
