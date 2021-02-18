package de.neo.rankbridge.minecraft.bungeecord;

import de.neo.rankbridge.shared.manager.services.ExternalService;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeService extends ExternalService<Plugin> {

	public BungeeService(BungeeMain main) {
		super("Bungee-" + main.getProxy().getVersion(), main);
	}

}
