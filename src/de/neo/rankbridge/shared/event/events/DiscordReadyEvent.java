package de.neo.rankbridge.shared.event.events;

import de.neo.rankbridge.shared.manager.services.BridgeService;

public class DiscordReadyEvent extends BridgeEvent{

	public DiscordReadyEvent(Class<? extends BridgeService> sender) {
		super(sender);
	}
}
