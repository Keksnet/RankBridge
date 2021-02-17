package de.neo.rankbridge.shared.event.events;

import de.neo.rankbridge.shared.event.EventSender;

public class BridgeReadyEvent extends BridgeEvent {
	
	public BridgeReadyEvent(EventSender sender) {
		super(sender);
	}

	@Override
	public BridgeEventType getType() {
		return BridgeEventType.BRIDGE_READY;
	}
}
