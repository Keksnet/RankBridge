package de.neo.rankbridge.minecraft.bungeecord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import org.bstats.bungeecord.Metrics;

import de.neo.rankbridge.SyncService;
import de.neo.rankbridge.discord.DiscordMain;
import de.neo.rankbridge.minecraft.MinecraftService;
import de.neo.rankbridge.minecraft.bungeecord.cmd.BungeeVerify;
import de.neo.rankbridge.minecraft.bungeecord.listener.JoinQuitListener;
import de.neo.rankbridge.shared.event.events.MinecraftLoadEvent;
import de.neo.rankbridge.shared.event.events.MinecraftReadyEvent;
import de.neo.rankbridge.shared.event.events.message.BridgeMessageSendEvent;
import de.neo.rankbridge.shared.event.events.MinecraftLoadEvent.MinecraftType;
import de.neo.rankbridge.shared.manager.GlobalManager;
import de.neo.rankbridge.shared.manager.MinecraftManager;
import de.neo.rankbridge.shared.manager.PermissionManager;
import de.neo.rankbridge.shared.message.BridgeMessage;
import de.neo.rankbridge.shared.message.BridgeMessage.ConversationMember;
import de.neo.rankbridge.teamspeak.TeamSpeakMain;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

/**
 * The mainclass for the BungeeCord plugin.
 * 
 * @author Neo8
 * @version 1.0
 */
public class BungeeMain extends Plugin{
	
	private Configuration config;
	private HashMap<ProxiedPlayer, String> codes;
	private HashMap<UUID, Long> delay;
	
	/**
	 * Runs when the Plugin is enabled.
	 */
	@SuppressWarnings("unused")
	public void onEnable() {
		GlobalManager manager = SyncService.isGlobalManagerRegistered() ? GlobalManager.getInstance() : new GlobalManager();
		SyncService.registerGlobalManager();
		MinecraftLoadEvent loadEvent = new MinecraftLoadEvent(BungeeService.class, MinecraftType.BUNGEECORD);
		manager.getEventHandler().executeEvent(loadEvent);
		if(loadEvent.isCancelled()) {
			this.onDisable();
			return;
		}
		MinecraftService mcService = new MinecraftService(MinecraftType.BUNGEECORD, this.getClass());
		manager.getServiceManager().register(mcService);
		BungeeService service = new BungeeService(this);
		manager.getServiceManager().register(service);
		
		loadConfig();
		MinecraftManager mgr = new MinecraftManager();
		if(this.config.getBoolean("teamspeak.enable") || this.config.getBoolean("discord.enable")) {
			new PermissionManager(mgr);
		}
		if(this.config.getBoolean("discord.enable")) {
			new DiscordMain();
		}
		if(this.config.getBoolean("teamspeak.enable")) {
			new TeamSpeakMain();
		}
		
		this.codes = new HashMap<>();
		this.delay = new HashMap<>();
		
		getProxy().getPluginManager().registerCommand(this, new BungeeVerify());
		getProxy().getPluginManager().registerListener(this, new JoinQuitListener());
		
		MinecraftReadyEvent readyEvent = new MinecraftReadyEvent(BungeeService.class, MinecraftType.BUNGEECORD);
		manager.getEventHandler().executeEvent(readyEvent);
		
		Integer pluginId = 10688;
		Metrics metrics = new Metrics(this, pluginId);
	}
	
	/**
	 * Returns the Configuration.
	 * 
	 * @return the Configuration.
	 */
	public Configuration getConfig() {
		return this.config;
	}
	
	/**
	 * Loads the Configuration out of the File.
	 */
	public void loadConfig() {
		try {
			if(!getDataFolder().exists()) {
				getDataFolder().mkdir();
			}
			File f = new File(getDataFolder(), "config.yml");
			Boolean setup = false;
			if(!f.exists()) {
				Files.copy(getResourceAsStream("bungee_config.yml"), f.toPath());
				setup = true;
			}
			this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(f);
			if(setup) {
				this.config.set("discord.enable", false);
				this.config.set("discord.external_sync", false);
				this.config.set("discord.token", "BOT_TOKEN_HERE");
				this.config.set("discord.activity", "verifing players");
				this.config.set("discord.guild", 0l);
				this.config.set("discord.verified_group", 0l);
				ArrayList<String> discord_group = new ArrayList<>();
				discord_group.add("group.admin, 0");
				this.config.set("discord.groups", discord_group);
				
				this.config.set("teamspeak.enable", false);
				this.config.set("teamspeak.external_sync", false);
				this.config.set("teamspeak.external_sync.delay", 30l);
				this.config.set("teamspeak.user", "username");
				this.config.set("teamspeak.password", "password");
				this.config.set("teamspeak.host", "127.0.0.1");
				this.config.set("teamspeak.port", 10011);
				this.config.set("teamspeak.vserver", 1);
				this.config.set("teamspeak.nickname", "Verify Bot");
				this.config.set("teamspeak.verified_group", 0);
				ArrayList<String> teamspeak_group = new ArrayList<>();
				teamspeak_group.add("group.admin, 6");
				this.config.set("teamspeak.groups", teamspeak_group);
				
				this.config.set("messages.discord.verified", "verified message");
				this.config.set("messages.discord.verify_info", "verify_info message");
				
				this.config.set("messages.teamspeak.verify_info", "verify_info message");
				this.config.set("messages.teamspeak.verified", "verified message");
				
				this.config.set("messages.minecraft.verify_info", "verify_info message");
				this.config.set("messages.minecraft.code_info", "code_info message");
				this.config.set("messages.minecraft.verified", "verified message");
				ConfigurationProvider.getProvider(YamlConfiguration.class).save(this.config, f);
				getProxy().getLogger().log(Level.WARNING, "config generated.");
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a Verficationcode.
	 * 
	 * @param code the code.
	 * @param uuid the uuid of the player.
	 */
	public void addCode(String code, String uuid) {
		if(this.codes.containsKey(getProxy().getPlayer(UUID.fromString(uuid)))) {
			removeCode(uuid);
		}
		this.codes.put(getProxy().getPlayer(UUID.fromString(uuid)), code);
	}
	
	/**
	 * Removes a code in this class.
	 * 
	 * @param code the code.
	 * @param uuid the uuid of the player.
	 */
	public void removeCodeSingle(String code, String uuid) {
		this.codes.remove(getProxy().getPlayer(UUID.fromString(uuid)), code);
	}
	
	/**
	 * Sends a message to all services to remove the code.
	 * 
	 * @param uuid the uuid of the player.
	 */
	public void removeCode(String uuid) {
		BridgeMessage<String> msg = new BridgeMessage<>(ConversationMember.MINECRAFT);
		msg.setContent("INVOKE;" + this.codes.get(getProxy().getPlayer(UUID.fromString(uuid))));
		BridgeMessageSendEvent sendEvent = new BridgeMessageSendEvent(BungeeService.class, msg);
		GlobalManager.getInstance().getEventHandler().executeEvent(sendEvent);
		this.codes.remove(getProxy().getPlayer(UUID.fromString(uuid)), this.codes.get(getProxy().getPlayer(UUID.fromString(uuid))));
	}
	
	/**
	 * Has this player an delay?
	 * 
	 * @param uuid the uuid of the player.
	 * @return Boolean whether the players delay is done or not.
	 */
	public Boolean isDelayDone(UUID uuid) {
		return (System.currentTimeMillis() / 1000) >= this.delay.get(uuid) + this.config.getLong("teamspeak.external_sync_delay");
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
