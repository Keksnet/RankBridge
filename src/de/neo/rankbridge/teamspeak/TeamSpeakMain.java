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

public class TeamSpeakMain extends BridgeService {
	
	private TS3ApiAsync api;
	private HashMap<String, MultiVar> codes = new HashMap<>();
	
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
				
				TS3ApiAsync api = query.getAsyncApi();
				api.login(user, password).await();
				api.selectVirtualServerById(vserver).await();
				api.setNickname(nickname).await();
				System.out.println("TeamSpeakBot is online!");
				api.registerAllEvents().await();
				api.addTS3Listeners(new TSListener());
				TeamSpeakReadyEvent readyEvent = new TeamSpeakReadyEvent(TeamSpeakMain.class);
				manager.getEventHandler().executeEvent(readyEvent);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void addCode(String code, Integer group, UUID uuid) {
		this.codes.put(code, new MultiVar(String.valueOf(group), uuid.toString()));
	}
	
	public MultiVar getCode(String code) {
		MultiVar var = this.codes.get(code);
		if(var != null) {
			return var;
		}else {
			return null;
		}
	}
	
	public void removeCode(String code) {
		this.codes.remove(code);
	}
	
	public TS3ApiAsync getAPI() {
		return this.api;
	}
}
