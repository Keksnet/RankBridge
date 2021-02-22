package de.neo.rankbridge.minecraft;

import de.neo.rankbridge.minecraft.listener.MessageSendListener;
import de.neo.rankbridge.shared.event.events.MinecraftLoadEvent.MinecraftType;
import de.neo.rankbridge.shared.event.events.message.BridgeMessageSendEvent;
import de.neo.rankbridge.shared.manager.GlobalManager;
import de.neo.rankbridge.shared.manager.services.ExternalService;

/**
 * A ExternalService for Minecraft.
 * 
 * @author Neo8
 * @version 1.0
 */
public class MinecraftService extends ExternalService<Class<?>> {
	
	private MinecraftType type;
	
	/**
	 * New instance
	 * 
	 * @param type the Type of Minecraft.
	 * @param main the MainClass of Minecraft. (don't use this Class, use SpigotService or BungeeService instead) 
	 */
	public MinecraftService(MinecraftType type, Class<?> main) {
		super("Minecraft", main);
		this.type = type;
		GlobalManager.getInstance().getEventHandler().registerListener(BridgeMessageSendEvent.class, new MessageSendListener(type));
	}
	
	
	/**
	 * Gets the MinecraftType.
	 * 
	 * @return type of Minecraft.
	 */
	public MinecraftType getMinecraftType() {
		return this.type;
	}

}
