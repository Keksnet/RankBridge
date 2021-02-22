package de.neo.rankbridge.minecraft.spigot;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.neo.rankbridge.shared.manager.services.ExternalService;

/**
 * The ExternalService for Spigot.
 * 
 * @author Neo8
 * @version 1.0
 */
public class SpigotService extends ExternalService<JavaPlugin> {
	
	/**
	 * New instance
	 * 
	 * @param main a class that extends SpigotMain
	 */
	public SpigotService(SpigotMain main) {
		super("Spigot-" + Bukkit.getVersion(), main);
	}
}
