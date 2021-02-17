package de.neo.rankbridge.shared.event.events;

import de.neo.rankbridge.shared.event.EventSender;

public class MinecraftLoadEvent extends CancellableEvent{
	
	public MinecraftLoadEvent(EventSender sender) {
		super(sender);
	}

	@Override
	public BridgeEventType getType() {
		return BridgeEventType.MINECRAFT_LOAD;
	}
}
