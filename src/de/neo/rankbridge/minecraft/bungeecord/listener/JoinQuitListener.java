package de.neo.rankbridge.minecraft.bungeecord.listener;

import de.neo.rankbridge.minecraft.bungeecord.BungeeMain;
import de.neo.rankbridge.minecraft.bungeecord.BungeeService;
import de.neo.rankbridge.shared.manager.GlobalManager;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Listens for disconnecting players.
 * 
 * @author Neo8
 * @version 1.0
 */
public class JoinQuitListener implements Listener {
	
	/**
	 * Executes the Event.
	 * 
	 * @param e The Event
	 */
	@EventHandler
	public void onQuit(PlayerDisconnectEvent e) {
		String uuid = e.getPlayer().getUniqueId().toString();
		BungeeMain main = (BungeeMain) GlobalManager.getInstance().getServiceManager().getService(BungeeService.class).getExternalService().getMain();
		main.removeCode(uuid);
	}
}
