package de.neo.rankbridge.discord.listener;

import org.bukkit.configuration.file.FileConfiguration;

import de.neo.rankbridge.shared.manager.MinecraftManager;
import de.neo.rankbridge.shared.manager.PermissionManager;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.md_5.bungee.config.Configuration;

/**
 * Listen for the RoleUpdates on Discord.
 * 
 * @author Neo8
 * @version 1.0
 */
public class RoleUpdateListener extends ListenerAdapter {
	
	/**
	 * Handles the Event.
	 */
	@Override
	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent e) {
		MinecraftManager mm = MinecraftManager.getInstance();
		if(mm.isMinecraftServiceRegistered()) {
			if(mm.isRunningOnBungeecord()) {
				Configuration config = (Configuration) mm.getConfig();
				if(config.getBoolean("discord.external_sync")) {
					PermissionManager permmgr = PermissionManager.getInstance();
					for(Role r : e.getRoles()) {
						if(permmgr.isDiscordRole(r.getIdLong())) {
							if(config.contains("users.verified.discord." + e.getMember().getId())) {
								mm.setPermission(config.getString("users.verified.discord." + e.getMember().getId()), permmgr.getDiscordGroup(r.getIdLong()));
							}
						}
					}
				}
			}else {
				FileConfiguration config = (FileConfiguration) mm.getConfig();
				if(config.getBoolean("discord.external_sync")) {
					PermissionManager permmgr = PermissionManager.getInstance();
					for(Role r : e.getRoles()) {
						if(permmgr.isDiscordRole(r.getIdLong())) {
							if(config.contains("users.verified.discord." + e.getMember().getId())) {
								mm.setPermission(config.getString("users.verified.discord." + e.getMember().getId()), permmgr.getDiscordGroup(r.getIdLong()));
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Handles the Event.
	 */
	@Override
	public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent e) {
		MinecraftManager mm = MinecraftManager.getInstance();
		if(mm.isMinecraftServiceRegistered()) {
			if(mm.isRunningOnBungeecord()) {
				Configuration config = (Configuration) mm.getConfig();
				if(config.getBoolean("discord.external_sync")) {
					PermissionManager permmgr = PermissionManager.getInstance();
					for(Role r : e.getRoles()) {
						if(permmgr.isDiscordRole(r.getIdLong())) {
							mm.unsetPermission(config.getString("users.verified.discord." + e.getMember().getId()), permmgr.getDiscordGroup(r.getIdLong()));
						}
					}
				}
			}else {
				FileConfiguration config = (FileConfiguration) mm.getConfig();
				if(config.getBoolean("discord.external_sync")) {
					PermissionManager permmgr = PermissionManager.getInstance();
					for(Role r : e.getRoles()) {
						if(permmgr.isDiscordRole(r.getIdLong())) {
							mm.unsetPermission(config.getString("users.verified.discord." + e.getMember().getId()), permmgr.getDiscordGroup(r.getIdLong()));
						}
					}
				}
			}
		}
	}
}
