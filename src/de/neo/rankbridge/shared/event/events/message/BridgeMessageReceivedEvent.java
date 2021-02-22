package de.neo.rankbridge.shared.event.events.message;

import de.neo.rankbridge.shared.manager.services.BridgeService;
import de.neo.rankbridge.shared.message.BridgeMessage;

/**
 * Is fired when a Message was received and proceed.
 * 
 * @author Neo8
 * @version 1.0
 */
public class BridgeMessageReceivedEvent extends BridgeMessageEvent{
	
	/**
	 * New instance
	 * 
	 * @param sender the sender of the service.
	 * @param message the message of the event.
	 */
	public BridgeMessageReceivedEvent(Class<? extends BridgeService> sender, BridgeMessage<?> message) {
		super(sender, message);
	}
}
