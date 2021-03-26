package de.neo.rankbridge.minecraft.spigot.listener;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import de.neo.rankbridge.minecraft.spigot.SpigotMain;
import de.neo.rankbridge.minecraft.spigot.SpigotService;
import de.neo.rankbridge.shared.manager.GlobalManager;
import de.neo.rankbridge.shared.manager.MinecraftManager;
import de.neo.rankbridge.shared.manager.PermissionManager;
import de.neo.rankbridge.teamspeak.TeamSpeakMain;

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
	public void onJoin(PlayerJoinEvent e) {
		GlobalManager manager = GlobalManager.getInstance();
		String uuid = e.getPlayer().getUniqueId().toString();
		SpigotMain main = (SpigotMain) GlobalManager.getInstance().getServiceManager().getService(SpigotService.class).getExternalService().getMain();
		if(main.isDelayDone(e.getPlayer().getUniqueId())) {
			main.resetDelay(e.getPlayer().getUniqueId());
			if(manager.getServiceManager().isServiceRegistered(TeamSpeakMain.class)) {
				TeamSpeakMain ts = (TeamSpeakMain) manager.getServiceManager().getService(TeamSpeakMain.class);
				MinecraftManager mm = MinecraftManager.getInstance();
				PermissionManager permmgr = PermissionManager.getInstance();
				FileConfiguration config = (FileConfiguration) mm.getConfig();
				try {
					Client c = null;
					for(Client c1 : ts.getAPI().getClients().get()) {
						if(config.contains("users.verified.teamspeak." + c1.getUniqueIdentifier())) {
							if(config.getString("users.verified.teamspeak." + c1.getUniqueIdentifier()).equalsIgnoreCase(uuid)) {
								c = c1;
								break;
							}
						}
					}
					if(c != null) {
						if(!permmgr.checkTeamspeak(c.getServerGroups(), uuid)) {
							for(int i : c.getServerGroups()) {
								if(permmgr.isTeamspeakGroup(i)) {
									if(!mm.hasPermission(uuid, permmgr.getTeamspeakGroup(i))) {
										mm.setPermission(uuid, permmgr.getTeamspeakGroup(i));
									}
								}
							}
						}
					}else {
						return;
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
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
		Bukkit.getScheduler().runTaskLater(main, new Runnable() {
			
			@Override
			public void run() {
				if(Bukkit.getPlayer(UUID.fromString(uuid)) == null) {
					main.removeDelay(UUID.fromString(uuid));
				}
			}
		}, 100l);
	}
}
