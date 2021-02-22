package de.neo.rankbridge.shared.event.events;

import de.neo.rankbridge.shared.manager.services.BridgeService;

/**
 * Is fired when the API is ready.
 * 
 * @author Neo8
 * @version 1.0
 */
public class ApiReadyEvent extends BridgeEvent{

	/**
	 * New instance.
	 * 
	 * @param sender the Sender of the event.
	 */
	public ApiReadyEvent(Class<? extends BridgeService> sender) {
		super(sender);
	}

}
