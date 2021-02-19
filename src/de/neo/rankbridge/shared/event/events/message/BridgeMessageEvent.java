package de.neo.rankbridge.shared.event.events.message;

import de.neo.rankbridge.shared.event.events.BridgeEvent;
import de.neo.rankbridge.shared.manager.services.BridgeService;
import de.neo.rankbridge.shared.message.BridgeMessage;

/**
 * Represents a BridgeEvent for messages.
 * 
 * @author Neo8
 * @version 1.0
 */
public abstract class BridgeMessageEvent extends BridgeEvent{
	
	private BridgeMessage<?> message;
	
	/**
	 * New BridgeMessageEvent.
	 * 
	 * @param sender Sender of the Event.
	 */
	public BridgeMessageEvent(Class<? extends BridgeService> sender, BridgeMessage<?> message) {
		super(sender);
		this.message = message;
	}
	
	/**
	 * Gets the BridgeMessage.
	 * 
	 * @return the message.
	 */
	public BridgeMessage<?> getMessage() {
		return this.message;
	}
}
