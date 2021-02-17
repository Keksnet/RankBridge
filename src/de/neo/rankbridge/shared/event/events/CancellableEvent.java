package de.neo.rankbridge.shared.event.events;

import de.neo.rankbridge.shared.manager.services.BridgeService;

/**
 * Respresents a BridgeEvent that can cancelled.
 * 
 * @author Neo8
 * @version 1.0
 */
public abstract class CancellableEvent extends BridgeEvent{
	
	private Boolean cancelled;
	
	/**
	 * New CancellableEvent.
	 * 
	 * @param sender EventSender
	 */
	public CancellableEvent(Class<? extends BridgeService> sender) {
		super(sender);
		this.cancelled = false;
	}
	
	/**
	 * Is the Event cancelled?
	 * 
	 * @return Boolean whether the BridgeEvent is cancelled or not.
	 */
	public Boolean isCancelled() {
		return this.cancelled;
	}
	
	/**
	 * Set the cancelled state of the BridgeEvent.
	 * 
	 * @param cancelled Boolean whether the BridgeEvent should cancelled or not.
	 */
	public void setCancelled(Boolean cancelled) {
		this.cancelled = cancelled;
	}
}
