package de.neo.rankbridge.shared.event.events;

import de.neo.rankbridge.shared.manager.services.BridgeService;

/**
 * Is fired when the Plugin is loading.
 * 
 * @author Neo8
 * @version 1.0
 */
public class MinecraftLoadEvent extends CancellableEvent{
	
	/**
	 * Represents the type of Minecraft.
	 * 
	 * @author Neo8
	 * @version 1.0
	 */
	public enum MinecraftType {
		/**
		 * A SpigotServer.
		 */
		SPIGOT,
		
		/**
		 * A BungeeCordNetwork.
		 */
		BUNGEECORD
	}
	
	private MinecraftType minecraftType;
	
	/**
	 * New instance.
	 * 
	 * @param sender the Sender of the event.
	 * @param type the Type of Minecraft.
	 */
	public MinecraftLoadEvent(Class<? extends BridgeService> sender, MinecraftType type) {
		super(sender);
		this.minecraftType = type;
	}
	
	/**
	 * Returns the type of Minecraft.
	 * 
	 * @return the type of Minecraft
	 */
	public MinecraftType getMinecraftType() {
		return this.minecraftType;
	}
}
