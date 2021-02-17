package de.neo.rankbridge.shared.manager;

import de.neo.rankbridge.shared.event.EventHandler;
import de.neo.rankbridge.shared.event.events.ApiReadyEvent;
import de.neo.rankbridge.shared.event.events.BridgeReadyEvent;
import de.neo.rankbridge.shared.event.events.DiscordLoadEvent;
import de.neo.rankbridge.shared.event.events.DiscordReadyEvent;
import de.neo.rankbridge.shared.event.events.MinecraftLoadEvent;
import de.neo.rankbridge.shared.event.events.MinecraftReadyEvent;
import de.neo.rankbridge.shared.event.events.TeamSpeakLoadEvent;
import de.neo.rankbridge.shared.event.events.TeamSpeakReadyEvent;
import de.neo.rankbridge.shared.event.events.message.BridgeMessageReceivedEvent;
import de.neo.rankbridge.shared.event.events.message.BridgeMessageSendEvent;

/**
 * The manager for all things.
 * 
 * @author Neo8
 * @version 1.0
 */
public class GlobalManager {
	
	private static GlobalManager INSTANCE;
	
	private EventHandler eventHandler;
	private BridgeServiceManager serviceManager;
	
	/**
	 * New GlobalManager.
	 */
	public GlobalManager() {
		INSTANCE = this;
		
		this.eventHandler = new EventHandler();
		
		this.eventHandler.registerEvent(BridgeReadyEvent.class);
		this.eventHandler.registerEvent(ApiReadyEvent.class);
		
		this.eventHandler.registerEvent(DiscordLoadEvent.class);
		this.eventHandler.registerEvent(MinecraftLoadEvent.class);
		this.eventHandler.registerEvent(TeamSpeakLoadEvent.class);
		
		this.eventHandler.registerEvent(DiscordReadyEvent.class);
		this.eventHandler.registerEvent(MinecraftReadyEvent.class);
		this.eventHandler.registerEvent(TeamSpeakReadyEvent.class);
		
		this.eventHandler.registerEvent(BridgeMessageSendEvent.class);
		this.eventHandler.registerEvent(BridgeMessageReceivedEvent.class);
		
		this.serviceManager = new BridgeServiceManager();
	}
	
	/**
	 * Gets the instance of the GlobalManager.
	 * 
	 * @return the instance of the GlobalManager.
	 */
	public static GlobalManager getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Gets the EventHandler.
	 * 
	 * @return the EventHandler
	 */
	public EventHandler getEventHandler() {
		return this.eventHandler;
	}
	
	/**
	 * Gets the ServiceManager.
	 * 
	 * @return the ServiceManager
	 */
	public BridgeServiceManager getServiceManager() {
		return this.serviceManager;
	}
}
