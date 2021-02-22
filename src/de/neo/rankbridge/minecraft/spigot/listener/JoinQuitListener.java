package de.neo.rankbridge.minecraft.spigot.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import de.neo.rankbridge.minecraft.spigot.SpigotMain;
import de.neo.rankbridge.minecraft.spigot.SpigotService;
import de.neo.rankbridge.shared.manager.GlobalManager;

public class JoinQuitListener implements Listener {
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		String uuid = e.getPlayer().getUniqueId().toString();
		SpigotMain main = (SpigotMain) GlobalManager.getInstance().getServiceManager().getService(SpigotService.class).getExternalService().getMain();
		main.removeCode(uuid);
	}
}
