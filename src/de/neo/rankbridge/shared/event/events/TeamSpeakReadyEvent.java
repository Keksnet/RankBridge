package de.neo.rankbridge.shared.event.events;

import de.neo.rankbridge.shared.manager.services.BridgeService;

public class TeamSpeakReadyEvent extends BridgeEvent{

	public TeamSpeakReadyEvent(Class<? extends BridgeService> sender) {
		super(sender);
	}
}
