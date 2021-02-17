package de.neo.rankbridge.shared.event.events;

import de.neo.rankbridge.shared.event.EventSender;

/**
 * The BridgeEvent represents a Event.
 * A BridgeEvent is fired by the EventHandler.
 * 
 * @author Neo8
 * @version 1.0
 * @see de.neo.rankbridge.shared.event.EventHandler
 */
public abstract class BridgeEvent {
	
	private EventSender sender;
	
	public BridgeEvent(EventSender sender) {
		this.sender = sender;
	}
	
	/**
	 * Returns the Type of the BridgeEvent.
	 * 
	 * @return the type of the BridgeEvent.
	 */
	public abstract BridgeEventType getType();
	
	/**
	 * Returns the sender of the Event.
	 * 
	 * @return The sender of the BridgeEvent.
	 */
	public EventSender getSender() {
		return this.sender;
	}
}
