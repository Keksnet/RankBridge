package de.neo.rankbridge.shared.event.events;

import de.neo.rankbridge.shared.event.events.MinecraftLoadEvent.MinecraftType;
import de.neo.rankbridge.shared.manager.services.BridgeService;

public class MinecraftReadyEvent extends BridgeEvent{
	
	private MinecraftType minecraftType;

	public MinecraftReadyEvent(Class<? extends BridgeService> sender, MinecraftType type) {
		super(sender);
		this.minecraftType = type;
	}
	
	public MinecraftType getMinecraftType() {
		return this.minecraftType;
	}
}
