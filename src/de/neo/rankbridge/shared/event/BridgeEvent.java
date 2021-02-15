package de.neo.rankbridge.shared.event;

/**
 * The BridgeEvent represents a Event.
 * A BridgeEvent is fired by the EventHandler.
 * 
 * @author Neo8
 * @version 1.0
 * @see de.neo.rankbridge.shared.event.EventHandler
 */
public abstract class BridgeEvent {
	
	/**
	 * The Type of a BridgeEvent
	 * 
	 * @author Neo8
	 * @version 1.0
	 * @see de.neo.rankbridge.shared.event.BridgeEvent
	 */
	public enum BridgeEventType {
		READY
	}
	
	/**
	 * Returns the Type of the BridgeEvent.
	 * 
	 * @return the type of the BridgeEvent.
	 */
	public abstract BridgeEventType getType();
}
