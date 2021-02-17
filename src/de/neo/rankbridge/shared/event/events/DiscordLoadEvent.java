package de.neo.rankbridge.shared.event.events;

import de.neo.rankbridge.shared.manager.services.BridgeService;

public class DiscordLoadEvent extends CancellableEvent{

	public DiscordLoadEvent(Class<? extends BridgeService> sender) {
		super(sender);
	}
}
