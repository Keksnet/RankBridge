package de.neo.rankbridge.discord;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.neo.rankbridge.SyncService;
import de.neo.rankbridge.minecraft.MinecraftService;
import de.neo.rankbridge.minecraft.bungeecord.BungeeMain;
import de.neo.rankbridge.minecraft.bungeecord.BungeeService;
import de.neo.rankbridge.minecraft.spigot.SpigotService;
import de.neo.rankbridge.shared.event.events.DiscordLoadEvent;
import de.neo.rankbridge.shared.event.events.MinecraftLoadEvent.MinecraftType;
import de.neo.rankbridge.shared.manager.GlobalManager;
import de.neo.rankbridge.shared.manager.services.BridgeService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class DiscordMain extends BridgeService {
	
	private JDA jda;
	
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
				JDABuilder builder = JDABuilder.createDefault(TOKEN);
				builder.setActivity(Activity.playing(activity));
				builder.setStatus(OnlineStatus.ONLINE);
				builder.setEnabledIntents(GatewayIntent.GUILD_MEMBERS);
				builder.setMemberCachePolicy(MemberCachePolicy.ALL);
				jda = builder.build();
			}
		}catch(LoginException e) {
			Logger logger = LoggerFactory.getLogger("Discord");
			logger.error("please chekc your discord token.");
		}
	}
	
	public JDA getJDA() {
		return this.jda;
	}
}
