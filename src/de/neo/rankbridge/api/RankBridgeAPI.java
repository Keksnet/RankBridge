package de.neo.rankbridge.api;

import de.neo.rankbridge.discord.DiscordMain;
import de.neo.rankbridge.minecraft.MinecraftService;
import de.neo.rankbridge.shared.event.EventHandler;
import de.neo.rankbridge.shared.event.events.MinecraftLoadEvent.MinecraftType;
import de.neo.rankbridge.shared.manager.BridgeServiceManager;
import de.neo.rankbridge.shared.manager.GlobalManager;
import de.neo.rankbridge.teamspeak.TeamSpeakMain;

/**
 * The API Class for RankBridge.
 * 
 * @author Neo8
 * @version 1.0
 */
public class RankBridgeAPI {
	
	private GlobalManager globalManager;
	private EventHandler eventHandler;
	private BridgeServiceManager serviceManager;
	private TeamSpeakMain ts3main;
	private DiscordMain dcmain;
	private MinecraftType type;
	
	/**
	 * New API Instance. If you want to use the API use this constructor.
	 */
	public RankBridgeAPI() {
		this.globalManager = GlobalManager.getInstance();
		this.eventHandler = this.globalManager.getEventHandler();
		this.serviceManager = this.globalManager.getServiceManager();
		this.ts3main = this.serviceManager.isServiceRegistered(TeamSpeakMain.class) ? (TeamSpeakMain) this.serviceManager.getService(TeamSpeakMain.class) : null;
		this.dcmain = this.serviceManager.isServiceRegistered(DiscordMain.class) ? (DiscordMain) this.serviceManager.getService(DiscordMain.class) : null;
		this.type = ((MinecraftService) this.serviceManager.getService(MinecraftService.class)).getMinecraftType();
	}
	
	/**
	 * Returns the GlobalManager.
	 * 
	 * @return GlobalManager
	 */
	public GlobalManager getGlobalManager() {
		return this.globalManager;
	}
	
	/**
	 * Returns the EventHandler.
	 * 
	 * @return EventHandler
	 */
	public EventHandler getEventHandler() {
		return this.eventHandler;
	}
	
	/**
	 * Returns the BridgeServiceManager.
	 * 
	 * @return BridgeServiceManager
	 */
	public BridgeServiceManager getServiceManager() {
		return this.serviceManager;
	}
	
	/**
	 * Returns the TeamSpeakMain.
	 * 
	 * @return TeamSpeakMain
	 */
	public TeamSpeakMain getTS3() {
		return this.ts3main;
	}
	
	/**
	 * Returns the DiscordMain.
	 * 
	 * @return DiscordMain
	 */
	public DiscordMain getDiscord() {
		return this.dcmain;
	}
	
	/**
	 * Returns the MinecraftType.
	 * 
	 * @return MinecraftType.
	 */
	public MinecraftType getMinecraftType() {
		return this.type;
	}
	
	/**
	 * Returns a Boolean whether ts3 is enabled or not.
	 * 
	 * @return Boolean whether ts3 is enabled or not.
	 */
	public Boolean usesTS3() {
		return this.serviceManager.isServiceRegistered(TeamSpeakMain.class);
	}
	
	/**
	 * Returns a Boolean whether discord is enabled or not.
	 * 
	 * @return Boolean whether discord is enabled or not.
	 */
	public Boolean usesDiscord() {
		return this.serviceManager.isServiceRegistered(DiscordMain.class);
	}
}
