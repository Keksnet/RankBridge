package de.neo.rankbridge.minecraft;

import de.neo.rankbridge.minecraft.listener.MessageSendListener;
import de.neo.rankbridge.shared.event.events.MinecraftLoadEvent.MinecraftType;
import de.neo.rankbridge.shared.event.events.message.BridgeMessageSendEvent;
import de.neo.rankbridge.shared.manager.GlobalManager;
import de.neo.rankbridge.shared.manager.services.ExternalService;

public class MinecraftService extends ExternalService<Class<?>> {
	
	private MinecraftType type;

	public MinecraftService(MinecraftType type, Class<?> main) {
		super("Minecraft", main);
		this.type = type;
		GlobalManager.getInstance().getEventHandler().registerListener(BridgeMessageSendEvent.class, new MessageSendListener(type));
	}
	
	public MinecraftType getMinecraftType() {
		return this.type;
	}

}
