package de.neo.rankbridge.minecraft.spigot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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
				config.set("discord.token", "BOT_TOKEN_HERE");
				config.set("discord.activity", "verifing players");
				Bukkit.getPluginManager().disablePlugin(this);
				System.out.println("Bitte f�llen sie die Config aus.");
				return;
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
