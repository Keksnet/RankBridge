package de.neo.rankbridge.minecraft.spigot.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import de.neo.rankbridge.minecraft.spigot.SpigotMain;
import de.neo.rankbridge.minecraft.spigot.SpigotService;
import de.neo.rankbridge.shared.manager.GlobalManager;

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
	 * @param e the Event.
	 */
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		String uuid = e.getPlayer().getUniqueId().toString();
		SpigotMain main = (SpigotMain) GlobalManager.getInstance().getServiceManager().getService(SpigotService.class).getExternalService().getMain();
		main.removeCode(uuid);
	}
}
