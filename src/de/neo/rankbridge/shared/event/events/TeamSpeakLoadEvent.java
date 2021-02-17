package de.neo.rankbridge.shared.event.events;

import de.neo.rankbridge.shared.manager.services.BridgeService;

public class TeamSpeakLoadEvent extends CancellableEvent{

	public TeamSpeakLoadEvent(Class<? extends BridgeService> sender) {
		super(sender);
	}
}
