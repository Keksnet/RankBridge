package de.neo.rankbridge.shared.event.events;

import de.neo.rankbridge.shared.manager.services.BridgeService;

/**
 * Is fired when Discord is loading.
 * 
 * @author Neo8
 * @version 1.0
 */
public class DiscordLoadEvent extends CancellableEvent{
	
	/**
	 * New instance.
	 * 
	 * @param sender the Sender of the event.
	 */
	public DiscordLoadEvent(Class<? extends BridgeService> sender) {
		super(sender);
	}
}
