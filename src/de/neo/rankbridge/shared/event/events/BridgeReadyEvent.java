package de.neo.rankbridge.shared.event.events;

import de.neo.rankbridge.shared.manager.services.BridgeService;

public class BridgeReadyEvent extends BridgeEvent {
	
	public BridgeReadyEvent(Class<? extends BridgeService> sender) {
		super(sender);
	}
}
