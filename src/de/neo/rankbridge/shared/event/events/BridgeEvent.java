package de.neo.rankbridge.shared.event.events;

import de.neo.rankbridge.shared.manager.services.BridgeService;

/**
 * The BridgeEvent represents a Event.
 * A BridgeEvent is fired by the EventHandler.
 * 
 * @author Neo8
 * @version 1.0
 * @see de.neo.rankbridge.shared.event.EventHandler
 */
public abstract class BridgeEvent {
	
	private Class<? extends BridgeService> sender;
	
	public BridgeEvent(Class<? extends BridgeService> sender) {
		this.sender = sender;
	}
	
	/**
	 * Returns the sender of the Event.
	 * 
	 * @return The sender of the BridgeEvent.
	 */
	public Class<? extends BridgeService> getSender() {
		return this.sender;
	}
}
