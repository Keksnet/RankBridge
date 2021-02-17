package de.neo.rankbridge.shared.event.events;

import de.neo.rankbridge.shared.event.EventSender;

public abstract class CancellableEvent extends BridgeEvent{
	
	private Boolean cancelled;
	
	/**
	 * New CancellableEvent.
	 */
	public CancellableEvent(EventSender sender) {
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

	@Override
	public abstract BridgeEventType getType();
}
