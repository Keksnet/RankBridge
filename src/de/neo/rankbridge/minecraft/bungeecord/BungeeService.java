package de.neo.rankbridge.minecraft.bungeecord;

import de.neo.rankbridge.shared.manager.services.ExternalService;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeService extends ExternalService<Plugin> {

	public BungeeService(Plugin p) {
		super("Bungee-" + p.getProxy().getVersion(), p);
	}

}
