package de.neo.rankbridge.shared.manager;

import java.util.ArrayList;
import java.util.HashMap;

import de.neo.rankbridge.minecraft.MinecraftService;
import de.neo.rankbridge.minecraft.bungeecord.BungeeMain;
import de.neo.rankbridge.minecraft.bungeecord.BungeeService;
import de.neo.rankbridge.shared.event.events.MinecraftLoadEvent.MinecraftType;
import de.neo.rankbridge.shared.manager.services.BridgeService;
import de.neo.rankbridge.shared.manager.services.ExternalService;

public class PermissionManager {
	
	private HashMap<String, Long> discord_groups;
	private HashMap<Long, String> group_discord;
	
	private HashMap<String, Integer> teamspeak_group;
	private HashMap<Integer, String> group_teamspeak;
	
	private MinecraftManager mgr;
	
	private static PermissionManager INSTANCE;
	
	@SuppressWarnings("unchecked")
	public PermissionManager(MinecraftManager mgr) {
		INSTANCE = this;
		this.mgr = mgr;
		this.discord_groups = new HashMap<>();
		this.teamspeak_group = new HashMap<>();
		this.group_discord = new HashMap<>();
		this.group_teamspeak = new HashMap<>();
		GlobalManager manager = GlobalManager.getInstance();
		MinecraftService mcService = (MinecraftService) manager.getServiceManager().getService(MinecraftService.class);
		if(mcService.getMinecraftType().equals(MinecraftType.BUNGEECORD)) {
			BridgeService service = (BridgeService) manager.getServiceManager().getService(BungeeService.class);
			ExternalService<?> ext = service.getExternalService();
			BungeeMain main = (BungeeMain) ext.getMain();
			for(String val : (ArrayList<String>) main.getConfig().getList("teamspeak.groups")) {
				String k = (String) val.split(",")[0];
				Integer v = Integer.valueOf((String) val.split(",")[1].replace(" ", ""));
				this.teamspeak_group.put(k, v);
				this.group_teamspeak.put(v, k);
			}
			for(String val : (ArrayList<String>) main.getConfig().getList("discord.groups")) {
				String k = (String) val.split(",")[0];
				Long v = Long.valueOf((String) val.split(",")[1].replace(" ", ""));
				this.discord_groups.put(k, v);
				this.group_discord.put(v, k);
			}
		}
	}
	
	public String getDiscordGroup(Long l) {
		return this.group_discord.get(l);
	}
	
	public Long getDiscordGroup(String s) {
		return this.discord_groups.get(s);
	}
	
	public String getTeamspeakGroup(Integer l) {
		return this.group_teamspeak.get(l);
	}
	
	public Integer getTeamspeakGroup(String s) {
		return this.teamspeak_group.get(s);
	}
	
	public Boolean isTeamspeakGroup(Integer s) {
		return this.teamspeak_group.containsValue(s);
	}
	
	public Boolean checkTeamspeak(String s1, String uuid) {
		for(String s : s1.split(",")) {
			if(this.teamspeak_group.containsValue(Integer.valueOf(s))) {
				if(this.mgr.hasPermission(uuid, getTeamspeakGroup(Integer.valueOf(s)))) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static PermissionManager getInstance() {
		return INSTANCE;
	}
}
