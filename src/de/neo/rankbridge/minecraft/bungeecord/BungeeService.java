package de.neo.rankbridge.minecraft.bungeecord;

import de.neo.rankbridge.shared.manager.services.ExternalService;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * The externalservice for the BungeeCord.
 * 
 * @author Neo8
 * @version 1.0
 */
public class BungeeService extends ExternalService<Plugin> {
	
	/**
	 * New instance.
	 * 
	 * @param p the MainClass that extends Plugin.
	 */
	public BungeeService(Plugin p) {
		super("Bungee-" + p.getProxy().getVersion(), p);
	}

}
