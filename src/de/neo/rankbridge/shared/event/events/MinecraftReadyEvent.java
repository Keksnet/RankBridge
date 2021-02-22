package de.neo.rankbridge.shared.event.events;

import de.neo.rankbridge.shared.event.events.MinecraftLoadEvent.MinecraftType;
import de.neo.rankbridge.shared.manager.services.BridgeService;

/**
 * Is fired when the Plugin is ready.
 * 
 * @author Neo8
 * @version 1.0
 */
public class MinecraftReadyEvent extends BridgeEvent{
	
	private MinecraftType minecraftType;
	
	/**
	 * New instance.
	 * 
	 * @param sender the Sender of the event.
	 * @param type the Type of Minecraft.
	 */
	public MinecraftReadyEvent(Class<? extends BridgeService> sender, MinecraftType type) {
		super(sender);
		this.minecraftType = type;
	}
	
	/**
	 * Returns the type of Minecraft.
	 * 
	 * @return the type of Minecraft.
	 */
	public MinecraftType getMinecraftType() {
		return this.minecraftType;
	}
}
