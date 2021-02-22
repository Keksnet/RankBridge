package de.neo.rankbridge.teamspeak;

import java.util.HashMap;
import java.util.UUID;

import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;

import de.neo.rankbridge.SyncService;
import de.neo.rankbridge.minecraft.MinecraftService;
import de.neo.rankbridge.shared.event.events.TeamSpeakLoadEvent;
import de.neo.rankbridge.shared.event.events.TeamSpeakReadyEvent;
import de.neo.rankbridge.shared.event.events.message.BridgeMessageSendEvent;
import de.neo.rankbridge.shared.manager.GlobalManager;
import de.neo.rankbridge.shared.manager.MinecraftManager;
import de.neo.rankbridge.shared.manager.services.BridgeService;
import de.neo.rankbridge.shared.util.MultiVar;
import de.neo.rankbridge.teamspeak.listener.MessageSendListener;
import de.neo.rankbridge.teamspeak.listener.TSListener;

/**
 * the MainClass for the TeamSpeak Bot.
 * 
 * @author Neo8
 * @version 1.0
 */
public class TeamSpeakMain extends BridgeService {
	
	private TS3ApiAsync api;
	private HashMap<String, MultiVar> codes = new HashMap<>();
	
	/**
	 * new Instance.
	 */
	public TeamSpeakMain() {
		super("TeamSpeakBot");
		GlobalManager manager = SyncService.isGlobalManagerRegistered() ? GlobalManager.getInstance() : new GlobalManager();
		SyncService.registerGlobalManager();
		TeamSpeakLoadEvent loadEvent = new TeamSpeakLoadEvent(TeamSpeakMain.class);
		manager.getEventHandler().executeEvent(loadEvent);
		if(loadEvent.isCancelled()) {
			return;
		}
		manager.getServiceManager().register(this);
		if(manager.getServiceManager().isServiceRegistered(MinecraftService.class)) {
			MinecraftManager perms = MinecraftManager.getInstance();
			String user = perms.getString("teamspeak.user");
			String password = perms.getString("teamspeak.password");
			String host = perms.getString("teamspeak.host");
			Integer vserver = perms.getInt("teamspeak.vserver");
			String nickname = perms.getString("teamspeak.nickname");
			Integer port = perms.getInt("teamspeak.port");
			manager.getEventHandler().registerListener(BridgeMessageSendEvent.class, new MessageSendListener());
			try {
				TS3Config config = new TS3Config();
				config.setHost(host);
				config.setQueryPort(port);
				
				TS3Query query = new TS3Query(config);
				query.connect();
				
				this.api = query.getAsyncApi();
				this.api.login(user, password).await();
				this.api.selectVirtualServerById(vserver).await();
				this.api.setNickname(nickname).await();
				System.out.println("TeamSpeakBot is online!");
				this.api.registerAllEvents().await();
				this.api.addTS3Listeners(new TSListener(this));
				TeamSpeakReadyEvent readyEvent = new TeamSpeakReadyEvent(TeamSpeakMain.class);
				manager.getEventHandler().executeEvent(readyEvent);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Adds a Verification Code.
	 * 
	 * @param code The code to add.
	 * @param group The role to add.
	 * @param uuid The uuid of the Player.
	 * @param ip the ip of the Player.
	 */
	public void addCode(String code, Integer group, UUID uuid, String ip) {
		this.codes.put(code, new MultiVar(String.valueOf(group), uuid.toString(), ip));
	}
	
	/**
	 * Gets the Values for a Code.
	 * 
	 * @param code The code.
	 * @return The MultiVar for the code.
	 */
	public MultiVar getCode(String code) {
		MultiVar var = this.codes.get(code);
		if(var != null) {
			return var;
		}else {
			return null;
		}
	}
	
	/**
	 * Removes a Code.
	 * 
	 * @param code The code to remove.
	 */
	public void removeCode(String code) {
		this.codes.remove(code);
	}
	
	/**
	 * Returns the TS3 API.
	 * 
	 * @return the TS3 API.
	 */
	public TS3ApiAsync getAPI() {
		return this.api;
	}
}
