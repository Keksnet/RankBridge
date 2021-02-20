package de.neo.rankbridge.shared.manager;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import de.neo.rankbridge.minecraft.MinecraftService;
import de.neo.rankbridge.minecraft.bungeecord.BungeeMain;
import de.neo.rankbridge.minecraft.bungeecord.BungeeService;
import de.neo.rankbridge.minecraft.spigot.SpigotService;
import de.neo.rankbridge.shared.event.events.MinecraftLoadEvent.MinecraftType;
import net.md_5.bungee.config.Configuration;

public class MinecraftManager {
	
	private static MinecraftManager INSTANCE;
	
	public MinecraftManager() {
		INSTANCE = this;
	}
	
	public static MinecraftManager getInstance() {
		return INSTANCE;
	}
	
	public String getName(String uuid) {
		GlobalManager manager = GlobalManager.getInstance();
		if(manager.getServiceManager().isServiceRegistered(MinecraftService.class)) {
			MinecraftService mcService = (MinecraftService) manager.getServiceManager().getService(MinecraftService.class);
			if(mcService.getMinecraftType().equals(MinecraftType.SPIGOT)) {
				return Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
			}else if(mcService.getMinecraftType().equals(MinecraftType.BUNGEECORD)) {
				BungeeMain bungee = (BungeeMain) manager.getServiceManager().getService(BungeeService.class).getExternalService().getMain();
				return bungee.getProxy().getPlayer(UUID.fromString(uuid)).getName();
			}
		}
		return null;
	}
	
	public Boolean hasPermission(String uuid, String perm) {
		GlobalManager manager = GlobalManager.getInstance();
		if(manager.getServiceManager().isServiceRegistered(MinecraftService.class)) {
			MinecraftService mcService = (MinecraftService) manager.getServiceManager().getService(MinecraftService.class);
			if(mcService.getMinecraftType().equals(MinecraftType.SPIGOT)) {
				return Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getPlayer().hasPermission(perm);
			}else if(mcService.getMinecraftType().equals(MinecraftType.BUNGEECORD)) {
				BungeeMain bungee = (BungeeMain) manager.getServiceManager().getService(BungeeService.class).getExternalService().getMain();
				return bungee.getProxy().getPlayer(UUID.fromString(uuid)).hasPermission(perm);
			}
		}
		return false;
	}
	
	public String getString(String path) {
		GlobalManager manager = GlobalManager.getInstance();
		if(manager.getServiceManager().isServiceRegistered(MinecraftService.class)) {
			MinecraftService mcService = (MinecraftService) manager.getServiceManager().getService(MinecraftService.class);
			if(mcService.getMinecraftType().equals(MinecraftType.SPIGOT)) {
				SpigotService spigot = (SpigotService) manager.getServiceManager().getService(SpigotService.class);
				FileConfiguration config = spigot.getMain().getConfig();
				return config.getString(path);
			}else if(mcService.getMinecraftType().equals(MinecraftType.BUNGEECORD)) {
				BungeeMain bungee = (BungeeMain) manager.getServiceManager().getService(BungeeService.class).getExternalService().getMain();
				Configuration config = bungee.getConfig();
				return config.getString(path);
			}
		}
		return null;
	}
	
	public Integer getInt(String path) {
		GlobalManager manager = GlobalManager.getInstance();
		if(manager.getServiceManager().isServiceRegistered(MinecraftService.class)) {
			MinecraftService mcService = (MinecraftService) manager.getServiceManager().getService(MinecraftService.class);
			if(mcService.getMinecraftType().equals(MinecraftType.SPIGOT)) {
				SpigotService spigot = (SpigotService) manager.getServiceManager().getService(SpigotService.class);
				FileConfiguration config = spigot.getMain().getConfig();
				return config.getInt(path);
			}else if(mcService.getMinecraftType().equals(MinecraftType.BUNGEECORD)) {
				BungeeMain bungee = (BungeeMain) manager.getServiceManager().getService(BungeeService.class).getExternalService().getMain();
				Configuration config = bungee.getConfig();
				return config.getInt(path);
			}
		}
		return null;
	}
	
	public Long getLong(String path) {
		GlobalManager manager = GlobalManager.getInstance();
		if(manager.getServiceManager().isServiceRegistered(MinecraftService.class)) {
			MinecraftService mcService = (MinecraftService) manager.getServiceManager().getService(MinecraftService.class);
			if(mcService.getMinecraftType().equals(MinecraftType.SPIGOT)) {
				SpigotService spigot = (SpigotService) manager.getServiceManager().getService(SpigotService.class);
				FileConfiguration config = spigot.getMain().getConfig();
				return config.getLong(path);
			}else if(mcService.getMinecraftType().equals(MinecraftType.BUNGEECORD)) {
				BungeeMain bungee = (BungeeMain) manager.getServiceManager().getService(BungeeService.class).getExternalService().getMain();
				Configuration config = bungee.getConfig();
				return config.getLong(path);
			}
		}
		return null;
	}
	
	public ArrayList<?> getList(String path) {
		GlobalManager manager = GlobalManager.getInstance();
		if(manager.getServiceManager().isServiceRegistered(MinecraftService.class)) {
			MinecraftService mcService = (MinecraftService) manager.getServiceManager().getService(MinecraftService.class);
			if(mcService.getMinecraftType().equals(MinecraftType.SPIGOT)) {
				SpigotService spigot = (SpigotService) manager.getServiceManager().getService(SpigotService.class);
				FileConfiguration config = spigot.getMain().getConfig();
				return (ArrayList<?>) config.getList(path);
			}else if(mcService.getMinecraftType().equals(MinecraftType.BUNGEECORD)) {
				BungeeMain bungee = (BungeeMain) manager.getServiceManager().getService(BungeeService.class).getExternalService().getMain();
				Configuration config = bungee.getConfig();
				return (ArrayList<?>) config.getList(path);
			}
		}
		return null;
	}
}
