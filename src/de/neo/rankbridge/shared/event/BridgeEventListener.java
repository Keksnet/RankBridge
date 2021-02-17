package de.neo.rankbridge.shared.event;

import de.neo.rankbridge.shared.event.events.BridgeEvent;

/**
 * The BridgeEventListener represents a EventListener.
 * A BridgeEventListener handles a BridgeEvent.
 * 
 * @author Neo8
 * @version 1.0
 * @see de.neo.rankbridge.shared.event.events.BridgeEvent
 */
public interface BridgeEventListener {
	
	/**
	 * Executes the Event.
	 * 
	 * @param event The Event instance that is fired.
	 */
	public void execute(BridgeEvent event);
}
