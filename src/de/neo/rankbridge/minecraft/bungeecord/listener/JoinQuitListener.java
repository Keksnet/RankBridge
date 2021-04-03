package de.neo.rankbridge.minecraft.bungeecord.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import de.neo.rankbridge.minecraft.bungeecord.BungeeMain;
import de.neo.rankbridge.minecraft.bungeecord.BungeeService;
import de.neo.rankbridge.shared.manager.GlobalManager;
import de.neo.rankbridge.shared.manager.MinecraftManager;
import de.neo.rankbridge.shared.manager.PermissionManager;
import de.neo.rankbridge.teamspeak.TeamSpeakMain;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent.Reason;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
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
	public void onSwitch(ServerConnectEvent e) {
		Logger l = LoggerFactory.getLogger("TS-VER");
		l.warn("HANDLE");
		GlobalManager manager = GlobalManager.getInstance();
		String uuid = e.getPlayer().getUniqueId().toString();
		BungeeMain main = (BungeeMain) GlobalManager.getInstance().getServiceManager().getService(BungeeService.class).getExternalService().getMain();
		if(e.getReason().equals(Reason.JOIN_PROXY)) {
			main.resetDelay(e.getPlayer().getUniqueId());
			if(manager.getServiceManager().isServiceRegistered(TeamSpeakMain.class)) {
				TeamSpeakMain ts = (TeamSpeakMain) manager.getServiceManager().getService(TeamSpeakMain.class);
				MinecraftManager mm = MinecraftManager.getInstance();
				PermissionManager permmgr = PermissionManager.getInstance();
				if(mm.isRunningOnBungeecord()) {
					Configuration config = (Configuration) mm.getConfig();
					try {
						Client c = null;
						for(Client c1 : ts.getAPI().getClients().get()) {
							if(config.contains("users.verified.teamspeak." + c1.getUniqueIdentifier())) {
								if(config.getString("users.verified.teamspeak." + c1.getUniqueIdentifier()).equalsIgnoreCase(uuid)) {
									c = c1;
									l.warn("found " + c1.getUniqueIdentifier());
									break;
								}
							}
						}
						if(c != null) {
							l.warn("NN");
							if(!permmgr.checkMinecraft(c.getServerGroups(), uuid)) {
								l.warn("U2D");
								List<Integer> groups = new ArrayList<>();
								for(int i : c.getServerGroups()) {
									groups.add(i);
								}
								for(int i : permmgr.getTSGroups()) {
									if(groups.contains(i)) {
										if(!mm.hasPermission(uuid, permmgr.getTeamspeakGroup(i))) {
											mm.setPermission(uuid, permmgr.getTeamspeakGroup(i));
										}
									}else {
										if(mm.hasPermission(uuid, permmgr.getTeamspeakGroup(i))) {
											mm.unsetPermission(uuid, permmgr.getTeamspeakGroup(i));
										}
									}
								}
							}else {
								l.warn("!U2D");
							}
						}else {
							l.warn("N");
							return;
						}
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
		}else {
			if(main.isDelayDone(e.getPlayer().getUniqueId())) {
				main.resetDelay(e.getPlayer().getUniqueId());
				if(manager.getServiceManager().isServiceRegistered(TeamSpeakMain.class)) {
					TeamSpeakMain ts = (TeamSpeakMain) manager.getServiceManager().getService(TeamSpeakMain.class);
					MinecraftManager mm = MinecraftManager.getInstance();
					PermissionManager permmgr = PermissionManager.getInstance();
					if(mm.isRunningOnBungeecord()) {
						Configuration config = (Configuration) mm.getConfig();
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
		}
	}
	
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
		main.getProxy().getScheduler().schedule(main, new Runnable() {
			
			@Override
			public void run() {
				if(main.getProxy().getPlayer(UUID.fromString(uuid)) == null) {
					main.removeDelay(UUID.fromString(uuid));
				}
			}
		}, 5, TimeUnit.MINUTES);
	}
}
