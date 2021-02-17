package de.neo.rankbridge.minecraft.spigot;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.neo.rankbridge.SyncService;
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
		SpigotService service = new SpigotService(this);
		manager.getServiceManager().register(service);
		
		/* Start Plugin... */
		
		MinecraftReadyEvent readyEvent = new MinecraftReadyEvent(SpigotService.class, MinecraftType.SPIGOT);
		manager.getEventHandler().executeEvent(readyEvent);
	}
}
