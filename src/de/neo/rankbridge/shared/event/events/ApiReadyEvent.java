package de.neo.rankbridge.shared.event.events;

import de.neo.rankbridge.shared.manager.services.BridgeService;

public class ApiReadyEvent extends BridgeEvent{

	public ApiReadyEvent(Class<? extends BridgeService> sender) {
		super(sender);
	}

}
