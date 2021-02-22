package de.neo.rankbridge.shared.event.events.message;

import de.neo.rankbridge.shared.manager.services.BridgeService;
import de.neo.rankbridge.shared.message.BridgeMessage;

/**
 * Is fired when a Message is sent.
 * 
 * @author Neo8
 * @version 1.0
 */
public class BridgeMessageSendEvent extends BridgeMessageEvent{
	
	/**
	 * New instance
	 * 
	 * @param sender the Sender of the Event.
	 * @param message the messsage in the Event.
	 */
	public BridgeMessageSendEvent(Class<? extends BridgeService> sender, BridgeMessage<?> message) {
		super(sender, message);
	}
}
