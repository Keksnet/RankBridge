package de.neo.rankbridge.teamspeak;

import org.bukkit.configuration.file.FileConfiguration;

import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;

import de.neo.rankbridge.SyncService;
import de.neo.rankbridge.minecraft.MinecraftService;
import de.neo.rankbridge.minecraft.bungeecord.BungeeMain;
import de.neo.rankbridge.minecraft.bungeecord.BungeeService;
import de.neo.rankbridge.minecraft.spigot.SpigotService;
import de.neo.rankbridge.shared.event.events.MinecraftLoadEvent.MinecraftType;
import de.neo.rankbridge.shared.event.events.TeamSpeakLoadEvent;
import de.neo.rankbridge.shared.manager.GlobalManager;
import de.neo.rankbridge.shared.manager.services.BridgeService;
import net.md_5.bungee.config.Configuration;

public class TeamSpeakMain extends BridgeService {
	
	public TeamSpeakMain() {
		super("TeamSpeakBot");
		GlobalManager manager = SyncService.isGlobalManagerRegistered() ? GlobalManager.getInstance() : new GlobalManager();
		SyncService.registerGlobalManager();
		TeamSpeakLoadEvent loadEvent = new TeamSpeakLoadEvent(TeamSpeakMain.class);
		manager.getEventHandler().executeEvent(loadEvent);
		if(loadEvent.isCancelled()) {
			return;
		}
		manager.getServiceManager().register(this);
		if(manager.getServiceManager().isServiceRegistered(MinecraftService.class)) {
			MinecraftService mcService = (MinecraftService) manager.getServiceManager().getService(MinecraftService.class);
			String user = "";
			String password = "";
			String host = "";
			Integer vserver = 0;
			String nickname = "";
			Integer port = 0;
			if(mcService.getMinecraftType().equals(MinecraftType.SPIGOT)) {
				SpigotService spigotService = (SpigotService) manager.getServiceManager().getService(SpigotService.class);
				FileConfiguration config = spigotService.getMain().getConfig();
				user = config.getString("teamspeak.user");
				password = config.getString("teamspeak.password");
				host = config.getString("teamspeak.host");
				vserver = config.getInt("teamspeak.vserver");
				nickname = config.getString("teamspeak.nickname");
				port = config.getInt("teamspeak.port");
			}else if(mcService.getMinecraftType().equals(MinecraftType.BUNGEECORD)) {
				BungeeService bungeeService = (BungeeService) manager.getServiceManager().getService(BungeeService.class);
				Configuration config = ((BungeeMain) bungeeService.getMain()).getConfig();
				user = config.getString("teamspeak.user");
				password = config.getString("teamspeak.password");
				host = config.getString("teamspeak.host");
				vserver = config.getInt("teamspeak.vserver");
				nickname = config.getString("teamspeak.nickname");
				port = config.getInt("teamspeak.port");
			}
			try {
				TS3Config config = new TS3Config();
				config.setHost(host);
				config.setQueryPort(port);
				
				TS3Query query = new TS3Query(config);
				query.connect();
				
				TS3ApiAsync api = query.getAsyncApi();
				api.login(user, password).await();
				api.selectVirtualServerById(vserver).await();
				api.setNickname(nickname).await();
				api.sendChannelMessage(nickname + " is online!");
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
