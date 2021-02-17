package de.neo.rankbridge.shared.event.events;

import de.neo.rankbridge.shared.manager.services.BridgeService;

public class MinecraftLoadEvent extends CancellableEvent{
	
	public enum MinecraftType {
		SPIGOT,
		BUNGEECORD
	}
	
	private MinecraftType minecraftType;
	
	public MinecraftLoadEvent(Class<? extends BridgeService> sender, MinecraftType type) {
		super(sender);
		this.minecraftType = type;
	}
	
	public MinecraftType getMinecraftType() {
		return this.minecraftType;
	}
}
