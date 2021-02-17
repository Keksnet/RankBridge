package de.neo.rankbridge.minecraft.spigot;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.neo.rankbridge.shared.manager.services.ExternalService;

public class SpigotService extends ExternalService<JavaPlugin> {

	public SpigotService(SpigotMain main) {
		super("Spigot-" + Bukkit.getVersion(), main);
	}
}
