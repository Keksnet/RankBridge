package de.neo.rankbridge.shared.event.events.message;

import de.neo.rankbridge.shared.manager.services.BridgeService;
import de.neo.rankbridge.shared.message.BridgeMessage;

public class BridgeMessageSendEvent extends BridgeMessageEvent{

	public BridgeMessageSendEvent(Class<? extends BridgeService> sender, BridgeMessage<?> message) {
		super(sender, message);
	}
}
