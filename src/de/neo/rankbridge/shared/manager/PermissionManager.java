package de.neo.rankbridge.shared.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import de.neo.rankbridge.minecraft.MinecraftService;
import de.neo.rankbridge.minecraft.bungeecord.BungeeMain;
import de.neo.rankbridge.minecraft.bungeecord.BungeeService;
import de.neo.rankbridge.shared.event.events.MinecraftLoadEvent.MinecraftType;
import de.neo.rankbridge.shared.manager.services.BridgeService;
import de.neo.rankbridge.shared.manager.services.ExternalService;

/**
 * The Manager for Discord and Teamspeak permissions.
 * 
 * @author Neo8
 * @version 1.0
 */
public class PermissionManager {
	
	private HashMap<String, Long> discord_groups;
	private HashMap<Long, String> group_discord;
	
	private HashMap<String, Integer> teamspeak_group;
	private HashMap<Integer, String> group_teamspeak;
	
	private MinecraftManager mgr;
	
	private static PermissionManager INSTANCE;
	
	/**
	 * New Instance.
	 * 
	 * @param mgr the MinecraftManager.
	 */
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
	
	/**
	 * Returns a permission by role.
	 * 
	 * @param l the role.
	 * @return the permission.
	 */
	public String getDiscordGroup(Long l) {
		return this.group_discord.get(l);
	}
	
	/**
	 * Returns a role by permission.
	 * 
	 * @param s the permission.
	 * @return the role.
	 */
	public Long getDiscordGroup(String s) {
		return this.discord_groups.get(s);
	}
	
	/**
	 * Returns a permission by group.
	 * 
	 * @param l the group.
	 * @return the permission.
	 */
	public String getTeamspeakGroup(Integer l) {
		return this.group_teamspeak.get(l);
	}
	
	/**
	 * Returns a group by permission.
	 * 
	 * @param s the permission.
	 * @return the group.
	 */
	public Integer getTeamspeakGroup(String s) {
		return this.teamspeak_group.get(s);
	}
	
	/**
	 * Is the role synchronized?
	 * 
	 * @param l the role
	 * @return Boolean whether the role is synchronized or not.
	 */
	public Boolean isDiscordRole(Long l) {
		return this.discord_groups.containsValue(l);
	}
	
	/**
	 * Is the group synchronized?
	 * 
	 * @param l the group
	 * @return Boolean whether the group is synchronized or not.
	 */
	public Boolean isTeamspeakGroup(Integer l) {
		return this.teamspeak_group.containsValue(l);
	}
	
	/**
	 * Formats raw values and invokes {@link de.neo.rankbridge.shared.manager.PermissionManager#checkTeamspeak(String, String)}
	 * 
	 * @param groups array of integers with groups.
	 * @param uuid the uuid of the player. only used to give this to {@link de.neo.rankbridge.shared.manager.PermissionManager#checkTeamspeak(String, String)}
	 * @return return value of {@link de.neo.rankbridge.shared.manager.PermissionManager#checkTeamspeak(String, String)}
	 */
	public Boolean checkTeamspeak(int[] groups, String uuid) {
		String group = "";
		for(int i : groups) {
			group += String.valueOf(i);
			if(i != groups[groups.length - 1]) {
				group += ",";
			}
		}
		return checkTeamspeak(group, uuid);
	}
	
	/**
	 * Updates the Teamspeak groups.
	 * 
	 * @param s1 the Groups as String.
	 * @param uuid the Minecraft uuid.
	 * @return Boolean whether the verification is up to date or not.
	 */
	public Boolean checkTeamspeak(String s1, String uuid) {
		for(String s : s1.split(",")) {
			if(this.teamspeak_group.containsValue(Integer.valueOf(s))) {
				if(!this.mgr.hasPermission(uuid, getTeamspeakGroup(Integer.valueOf(s)))) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Formats raw values and invokes {@link de.neo.rankbridge.shared.manager.PermissionManager#checkMinecraft(String, String)}
	 * 
	 * @param groups array of integers with groups.
	 * @param uuid the uuid of the player. only used to give this to {@link de.neo.rankbridge.shared.manager.PermissionManager#checkMinecraft(String, String)}
	 * @return return value of {@link de.neo.rankbridge.shared.manager.PermissionManager#checkMinecraft(String, String)}
	 */
	public Boolean checkMinecraft(int[] groups, String uuid) {
		String group = "";
		for(int i : groups) {
			group += String.valueOf(i);
			if(i != groups[groups.length - 1]) {
				group += ",";
			}
		}
		return checkMinecraft(group, uuid);
	}
	
	/**
	 * Updates the Minecraft permission.
	 * 
	 * @param s1 the Groups as String.
	 * @param uuid the Minecraft uuid.
	 * @return Boolean whether the verification is up to date or not.
	 */
	public Boolean checkMinecraft(String s1, String uuid) {
		List<String> groups = (List<String>) Arrays.asList(s1.split(","));
		for(int group : this.group_teamspeak.keySet()) {
			if(!groups.contains(String.valueOf(group))) {
				if(this.mgr.hasPermission(uuid, getTeamspeakGroup(group))) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Returns the Instance.
	 * 
	 * @return the instance.
	 */
	public static PermissionManager getInstance() {
		return INSTANCE;
	}
}
