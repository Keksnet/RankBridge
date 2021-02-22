package de.neo.rankbridge.discord;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.UUID;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.neo.rankbridge.SyncService;
import de.neo.rankbridge.discord.listener.MessageReceivedListener;
import de.neo.rankbridge.discord.listener.MessageSendListener;
import de.neo.rankbridge.minecraft.MinecraftService;
import de.neo.rankbridge.minecraft.bungeecord.BungeeMain;
import de.neo.rankbridge.minecraft.bungeecord.BungeeService;
import de.neo.rankbridge.minecraft.spigot.SpigotService;
import de.neo.rankbridge.shared.event.events.DiscordLoadEvent;
import de.neo.rankbridge.shared.event.events.DiscordReadyEvent;
import de.neo.rankbridge.shared.event.events.MinecraftLoadEvent.MinecraftType;
import de.neo.rankbridge.shared.event.events.message.BridgeMessageSendEvent;
import de.neo.rankbridge.shared.manager.GlobalManager;
import de.neo.rankbridge.shared.manager.services.BridgeService;
import de.neo.rankbridge.shared.util.MultiVar;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

/**
 * The mainclass for the DiscordBot.
 * 
 * @author Neo8
 * @version 1.0
 */
public class DiscordMain extends BridgeService {
	
	private JDA jda;
	private HashMap<String, MultiVar> codes;
	
	/**
	 * New Instance.
	 */
	public DiscordMain() {
		super("DiscordBot");
		try {
			GlobalManager manager = SyncService.isGlobalManagerRegistered() ? GlobalManager.getInstance() : new GlobalManager();
			SyncService.registerGlobalManager();
			DiscordLoadEvent loadEvent = new DiscordLoadEvent(DiscordMain.class);
			manager.getEventHandler().executeEvent(loadEvent);
			if(loadEvent.isCancelled()) {
				return;
			}
			manager.getServiceManager().register(this);
			if(manager.getServiceManager().isServiceRegistered(MinecraftService.class)) {
				MinecraftService mcService = (MinecraftService) manager.getServiceManager().getService(MinecraftService.class);
				String TOKEN = "";
				String activity = "";
				if(mcService.getMinecraftType().equals(MinecraftType.SPIGOT)) {
					SpigotService spigotService = (SpigotService) manager.getServiceManager().getService(SpigotService.class);
					TOKEN = spigotService.getMain().getConfig().getString("discord.token");
					activity = spigotService.getMain().getConfig().getString("discord.activity");
				}else if(mcService.getMinecraftType().equals(MinecraftType.BUNGEECORD)) {
					BungeeService bungeeService = (BungeeService) manager.getServiceManager().getService(BungeeService.class);
					TOKEN = ((BungeeMain) bungeeService.getMain()).getConfig().getString("discord.token");
					activity = ((BungeeMain)bungeeService.getMain()).getConfig().getString("discord.activity");
				}
				this.codes = new HashMap<>();
				JDABuilder builder = JDABuilder.createDefault(TOKEN);
				builder.setActivity(Activity.playing(activity));
				builder.setStatus(OnlineStatus.ONLINE);
				builder.setEnabledIntents(EnumSet.allOf(GatewayIntent.class));
				builder.setMemberCachePolicy(MemberCachePolicy.ALL);
				builder.addEventListeners(new MessageReceivedListener());
				jda = builder.build();
				manager.getEventHandler().registerListener(BridgeMessageSendEvent.class, new MessageSendListener());
				DiscordReadyEvent readyEvent = new DiscordReadyEvent(DiscordMain.class);
				manager.getEventHandler().executeEvent(readyEvent);
			}
		}catch(LoginException e) {
			Logger logger = LoggerFactory.getLogger("Discord");
			logger.error("please check your discord token.");
		}
	}
	
	/**
	 * Adds a Verification Code.
	 * 
	 * @param code The code to add.
	 * @param group The role to add.
	 * @param uuid The uuid of the Player.
	 */
	public void addCode(String code, Long group, UUID uuid) {
		this.codes.put(code, new MultiVar(String.valueOf(group), uuid.toString()));
	}
	
	/**
	 * Gets the Values for a Code.
	 * 
	 * @param code The code.
	 * @return The MultiVar for the code.
	 */
	public MultiVar getCode(String code) {
		if(this.codes.containsKey(code.replace(" ", ""))) {
			return this.codes.get(code.replace(" ", ""));
		}else {
			return null;
		}
	}
	
	/**
	 * Removes a Code.
	 * 
	 * @param code The code to remove.
	 */
	public void removeCode(String code) {
		this.codes.remove(code);
	}
	
	
	/**
	 * Gets the JDA.
	 * 
	 * @return the JDA.
	 */
	public JDA getJDA() {
		return this.jda;
	}
}
