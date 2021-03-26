package de.neo.rankbridge.minecraft.spigot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import de.neo.rankbridge.SyncService;
import de.neo.rankbridge.discord.DiscordMain;
import de.neo.rankbridge.minecraft.MinecraftService;
import de.neo.rankbridge.minecraft.bungeecord.BungeeService;
import de.neo.rankbridge.minecraft.spigot.cmd.SpigotVerify;
import de.neo.rankbridge.minecraft.spigot.listener.JoinQuitListener;
import de.neo.rankbridge.shared.event.events.MinecraftLoadEvent;
import de.neo.rankbridge.shared.event.events.MinecraftReadyEvent;
import de.neo.rankbridge.shared.event.events.MinecraftLoadEvent.MinecraftType;
import de.neo.rankbridge.shared.event.events.message.BridgeMessageSendEvent;
import de.neo.rankbridge.shared.manager.GlobalManager;
import de.neo.rankbridge.shared.manager.MinecraftManager;
import de.neo.rankbridge.shared.manager.PermissionManager;
import de.neo.rankbridge.shared.message.BridgeMessage;
import de.neo.rankbridge.shared.message.BridgeMessage.ConversationMember;
import de.neo.rankbridge.teamspeak.TeamSpeakMain;

/**
 * The MainClass for the Spigot plugin.
 * 
 * @author Neo8
 * @version 1.0
 */
public class SpigotMain extends JavaPlugin {
	
	private HashMap<OfflinePlayer, String> codes;
	private HashMap<UUID, Long> delay;
	
	/**
	 * Runs when the plugin is enabled.
	 */
	@SuppressWarnings("unused")
	public void onEnable() {
		GlobalManager manager = SyncService.isGlobalManagerRegistered() ? GlobalManager.getInstance() : new GlobalManager();
		SyncService.registerGlobalManager();
		MinecraftLoadEvent loadEvent = new MinecraftLoadEvent(SpigotService.class, MinecraftType.SPIGOT);
		manager.getEventHandler().executeEvent(loadEvent);
		if(loadEvent.isCancelled()) {
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		MinecraftService mcService = new MinecraftService(MinecraftType.SPIGOT, this.getClass());
		manager.getServiceManager().register(mcService);
		SpigotService service = new SpigotService(this);
		manager.getServiceManager().register(service);
		
		loadConfig();
		MinecraftManager mgr = new MinecraftManager();
		if(getConfig().getBoolean("teamspeak.enable") || getConfig().getBoolean("discord.enable")) {
			new PermissionManager(mgr);
		}
		if(getConfig().getBoolean("discord.enable")) {
			new DiscordMain();
		}
		if(getConfig().getBoolean("teamspeak.enable")) {
			new TeamSpeakMain();
		}
		
		if(!Bukkit.getPluginManager().isPluginEnabled(this)) {
			return;
		}
		
		this.codes = new HashMap<>();
		this.delay = new HashMap<>();
		
		getCommand("verify").setExecutor(new SpigotVerify());
		Bukkit.getPluginManager().registerEvents(new JoinQuitListener(), this);
		
		MinecraftReadyEvent readyEvent = new MinecraftReadyEvent(SpigotService.class, MinecraftType.SPIGOT);
		manager.getEventHandler().executeEvent(readyEvent);
		
		Integer pluginId = 10686;
		Metrics metrics = new Metrics(this, pluginId);
	}
	
	/**
	 * Loads the Configuration.
	 */
	public void loadConfig() {
		try {
			Configuration config = getConfig();
			File f = new File(getDataFolder(), "config.yml");
			if(!getDataFolder().exists()) {
				getDataFolder().mkdir();
			}
			if(!f.exists()) {
				Files.copy(getResource("spigot_config.yml"), f.toPath());
				config.set("discord.enable", false);
				config.set("discord.external_sync", false);
				config.set("discord.token", "BOT_TOKEN_HERE");
				config.set("discord.activity", "verifing players");
				config.set("discord.guild", 0l);
				config.set("discord.verified_group", 0l);
				ArrayList<String> discord_group = new ArrayList<>();
				discord_group.add("group.admin, 0");
				config.set("discord.groups", discord_group);
				
				config.set("teamspeak.enable", false);
				config.set("teamspeak.external_sync", false);
				config.set("teamspeak.external_sync_delay", 30l);
				config.set("teamspeak.user", "username");
				config.set("teamspeak.password", "password");
				config.set("teamspeak.host", "127.0.0.1");
				config.set("teamspeak.port", 10011);
				config.set("teamspeak.vserver", 1);
				config.set("teamspeak.nickname", "Verify Bot");
				config.set("teamspeak.verified_group", 0);
				ArrayList<String> teamspeak_group = new ArrayList<>();
				teamspeak_group.add("group.admin, 6");
				config.set("teamspeak.groups", teamspeak_group);
				
				config.set("messages.discord.verified", "verified message");
				
				config.set("messages.teamspeak.verify_info", "verify_info message");
				config.set("messages.teamspeak.verified", "verified message");
				
				config.set("messages.minecraft.verify_info", "verify_info message");
				config.set("messages.minecraft.code_info", "code_info message");
				config.set("messages.minecraft.verified", "verified message");
				saveConfig();
				Bukkit.getPluginManager().disablePlugin(this);
				System.out.println("Bitte füllen sie die Config aus.");
				return;
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a Verificationcode to the class.
	 * 
	 * @param code the code
	 * @param uuid the uuid of the player.
	 */
	public void addCode(String code, String uuid) {
		if(this.codes.containsKey(Bukkit.getOfflinePlayer(UUID.fromString(uuid)))) {
			removeCode(uuid);
		}
		this.codes.put(Bukkit.getOfflinePlayer(UUID.fromString(uuid)), code);
	}
	
	/**
	 * Removes a Code from the class.
	 * 
	 * @param code the code
	 * @param uuid the uuid of the player
	 */
	public void removeCodeSingle(String code, String uuid) {
		this.codes.remove(Bukkit.getOfflinePlayer(UUID.fromString(uuid)), code);
	}
	
	/**
	 * Sends a message to all services to remove the code.
	 * 
	 * @param uuid the uuid of the player.
	 */
	public void removeCode(String uuid) {
		BridgeMessage<String> msg = new BridgeMessage<>(ConversationMember.MINECRAFT);
		msg.setContent("INVOKE;" + this.codes.get(Bukkit.getOfflinePlayer(UUID.fromString(uuid))));
		BridgeMessageSendEvent sendEvent = new BridgeMessageSendEvent(BungeeService.class, msg);
		GlobalManager.getInstance().getEventHandler().executeEvent(sendEvent);
		this.codes.remove(Bukkit.getOfflinePlayer(UUID.fromString(uuid)), this.codes.get(Bukkit.getOfflinePlayer(UUID.fromString(uuid))));
	}
	
	/**
	 * Has this player an delay?
	 * 
	 * @param uuid the uuid of the player.
	 * @return Boolean whether the players delay is done or not.
	 */
	public Boolean isDelayDone(UUID uuid) {
		return (System.currentTimeMillis() / 1000) >= this.delay.get(uuid) + this.getConfig().getLong("teamspeak.external_sync_delay");
	}
	
	/**
	 * Resets the expired delay of this player.
	 * 
	 * @param uuid the uuid of the player.
	 */
	public void resetDelay(UUID uuid) {
		this.delay.put(uuid, (System.currentTimeMillis() / 1000));
	}
	
	/**
	 * Removes this player from RAM (for clean usage).
	 * 
	 * @param uuid the uuid of the player.
	 */
	public void removeDelay(UUID uuid) {
		this.delay.remove(uuid);
	}
}
