package de.neo.rankbridge.minecraft.spigot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import de.neo.rankbridge.SyncService;
import de.neo.rankbridge.minecraft.MinecraftService;
import de.neo.rankbridge.shared.event.events.MinecraftLoadEvent;
import de.neo.rankbridge.shared.event.events.MinecraftReadyEvent;
import de.neo.rankbridge.shared.event.events.MinecraftLoadEvent.MinecraftType;
import de.neo.rankbridge.shared.manager.GlobalManager;

public class SpigotMain extends JavaPlugin {
	
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
		if(!Bukkit.getPluginManager().isPluginEnabled(this)) {
			return;
		}
		/* Start Plugin */
		
		MinecraftReadyEvent readyEvent = new MinecraftReadyEvent(SpigotService.class, MinecraftType.SPIGOT);
		manager.getEventHandler().executeEvent(readyEvent);
	}
	
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
				config.set("discord.token", "BOT_TOKEN_HERE");
				config.set("discord.activity", "verifing players");
				config.set("discord.guild", 0l);
				ArrayList<String> discord_group = new ArrayList<>();
				discord_group.add("group.admin, 0");
				config.set("discord.groups", discord_group);
				
				config.set("teamspeak.enable", false);
				config.set("teamspeak.user", "username");
				config.set("teamspeak.password", "password");
				config.set("teamspeak.host", "127.0.0.1");
				config.set("teamspeak.port", 10011);
				config.set("teamspeak.vserver", 1);
				config.set("teamspeak.nickname", "Verify Bot");
				ArrayList<String> teamspeak_group = new ArrayList<>();
				teamspeak_group.add("group.admin, 6");
				config.set("teamspeak.groups", teamspeak_group);
				Bukkit.getPluginManager().disablePlugin(this);
				System.out.println("Bitte füllen sie die Config aus.");
				return;
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
