package de.neo.rankbridge.shared.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;
import javax.net.ssl.HttpsURLConnection;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import de.neo.rankbridge.minecraft.MinecraftService;
import de.neo.rankbridge.minecraft.bungeecord.BungeeMain;
import de.neo.rankbridge.minecraft.bungeecord.BungeeService;
import de.neo.rankbridge.minecraft.spigot.SpigotService;
import de.neo.rankbridge.shared.event.events.MinecraftLoadEvent.MinecraftType;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedPermissionData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.query.QueryOptions;
import net.luckperms.api.util.Tristate;
import net.md_5.bungee.config.Configuration;

/**
 * The Manager for Minecraft.
 * 
 * @author Neo8
 * @version 1.0
 */
public class MinecraftManager {
	
	private static MinecraftManager INSTANCE;
	
	/**
	 * New Instance.
	 */
	public MinecraftManager() {
		INSTANCE = this;
	}
	
	/**
	 * Returns the instance of the MinecraftManager.
	 * 
	 * @return the Instance.
	 */
	public static MinecraftManager getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Returns the Name of an player by uuid.
	 * 
	 * @param uuid the uuid of the Player.
	 * @return the name of the Player.
	 */
	public String getName(String uuid) {
		GlobalManager manager = GlobalManager.getInstance();
		if(manager.getServiceManager().isServiceRegistered(MinecraftService.class)) {
			MinecraftService mcService = (MinecraftService) manager.getServiceManager().getService(MinecraftService.class);
			if(mcService.getMinecraftType().equals(MinecraftType.SPIGOT)) {
				return Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
			}else if(mcService.getMinecraftType().equals(MinecraftType.BUNGEECORD)) {
				return getBungeeName(uuid);
			}
		}
		return null;
	}
	
	/**
	 * Checks if a players has a Permission.
	 * 
	 * @param uuid the uuid of the player.
	 * @param perm the permission to check.
	 * @return Boolean whether the player has the permission or not.
	 */
	public Boolean hasPermission(String uuid, String perm) {
		GlobalManager manager = GlobalManager.getInstance();
		if(manager.getServiceManager().isServiceRegistered(MinecraftService.class)) {
			MinecraftService mcService = (MinecraftService) manager.getServiceManager().getService(MinecraftService.class);
			if(mcService.getMinecraftType().equals(MinecraftType.SPIGOT)) {
				if(Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
					LuckPerms lp = LuckPermsProvider.get();
					User u = lp.getUserManager().getUser(UUID.fromString(uuid));
					QueryOptions options = lp.getContextManager().getQueryOptions(u).orElse(lp.getContextManager().getStaticQueryOptions());
					CachedPermissionData perms = u.getCachedData().getPermissionData(options);
					Tristate result = perms.checkPermission(perm);
					return result.asBoolean();
				}
			}else if(mcService.getMinecraftType().equals(MinecraftType.BUNGEECORD)) {
				BungeeMain bungee = (BungeeMain) manager.getServiceManager().getService(BungeeService.class).getExternalService().getMain();
				if(bungee.getProxy().getPluginManager().getPlugin("LuckPerms") != null) {
					LuckPerms lp = LuckPermsProvider.get();
					User u = lp.getUserManager().getUser(UUID.fromString(uuid));
					QueryOptions options = lp.getContextManager().getQueryOptions(u).orElse(lp.getContextManager().getStaticQueryOptions());
					CachedPermissionData perms = u.getCachedData().getPermissionData(options);
					Tristate result = perms.checkPermission(perm);
					return result.asBoolean();
				}
			}
		}
		return false;
	}
	
	/**
	 * Sets a permision to a player.
	 * 
	 * @param uuid the uuid of the player.
	 * @param perm the permission to set.
	 */
	public void setPermission(String uuid, String perm) {
		if(this.isRunningOnBungeecord()) {
			BungeeMain bungee = (BungeeMain) GlobalManager.getInstance().getServiceManager().getService(BungeeService.class).getExternalService().getMain();
			if(bungee.getProxy().getPluginManager().getPlugin("LuckPerms") != null) {
				LuckPerms lp = LuckPermsProvider.get();
				QueryOptions options = lp.getContextManager().getQueryOptions(lp.getUserManager().getUser(UUID.fromString(uuid))).orElse(lp.getContextManager().getStaticQueryOptions());
				if(!lp.getUserManager().getUser(UUID.fromString(uuid)).getCachedData().getPermissionData(options).checkPermission(perm).asBoolean()) {
					lp.getUserManager().modifyUser(UUID.fromString(uuid), (User u) -> {
						u.data().add(Node.builder(perm).build());
					});
				}
			}
		}else {
			if(Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
				LuckPerms lp = LuckPermsProvider.get();
				QueryOptions options = lp.getContextManager().getQueryOptions(lp.getUserManager().getUser(UUID.fromString(uuid))).orElse(lp.getContextManager().getStaticQueryOptions());
				if(!lp.getUserManager().getUser(UUID.fromString(uuid)).getCachedData().getPermissionData(options).checkPermission(perm).asBoolean()) {
					lp.getUserManager().modifyUser(UUID.fromString(uuid), (User u) -> {
						u.data().add(Node.builder(perm).build());
					});
				}
			}
		}
	}
	
	/**
	 * Unsets a permision to a player.
	 * 
	 * @param uuid the uuid of the player.
	 * @param perm the permission to unset.
	 */
	public void unsetPermission(String uuid, String perm) {
		if(this.isRunningOnBungeecord()) {
			BungeeMain bungee = (BungeeMain) GlobalManager.getInstance().getServiceManager().getService(BungeeService.class).getExternalService().getMain();
			if(bungee.getProxy().getPluginManager().getPlugin("LuckPerms") != null) {
				LuckPerms lp = LuckPermsProvider.get();
				QueryOptions options = lp.getContextManager().getQueryOptions(lp.getUserManager().getUser(UUID.fromString(uuid))).orElse(lp.getContextManager().getStaticQueryOptions());
				if(lp.getUserManager().getUser(UUID.fromString(uuid)).getCachedData().getPermissionData(options).checkPermission(perm).asBoolean()) {
					lp.getUserManager().modifyUser(UUID.fromString(uuid), (User u) -> {
						u.data().remove(Node.builder(perm).build());
					});
				}
			}
		}else {
			if(Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
				LuckPerms lp = LuckPermsProvider.get();
				QueryOptions options = lp.getContextManager().getQueryOptions(lp.getUserManager().getUser(UUID.fromString(uuid))).orElse(lp.getContextManager().getStaticQueryOptions());
				if(lp.getUserManager().getUser(UUID.fromString(uuid)).getCachedData().getPermissionData(options).checkPermission(perm).asBoolean()) {
					lp.getUserManager().modifyUser(UUID.fromString(uuid), (User u) -> {
						u.data().remove(Node.builder(perm).build());
					});
				}
			}
		}
	}
	
	/**
	 * Returns a String out of the config.
	 * 
	 * @param path the path to look at.
	 * @return the value of the path.
	 */
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
	
	/**
	 * Returns an Integer out of the config.
	 * 
	 * @param path the path to look at.
	 * @return the value of the path.
	 */
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
	
	/**
	 * Returns a Long out of the config.
	 * 
	 * @param path the path to look at.
	 * @return the value of the path.
	 */
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
	
	/**
	 * Returns the List out of the config.
	 * 
	 * @param path the path to look at.
	 * @return the value of the path.
	 */
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
	
	/**
	 * Checks if the {@link de.neo.rankbridge.minecraft.MinecraftService} is registered.
	 * If false you should not continue using this class.
	 * 
	 * @return Boolean whether the {@link de.neo.rankbridge.minecraft.MinecraftService} is registered or not.
	 */
	public Boolean isMinecraftServiceRegistered() {
		return GlobalManager.getInstance().getServiceManager().isServiceRegistered(MinecraftService.class);
	}
	
	/**
	 * Checks if this plugin is running on Bungeecord.
	 * If false, this plugin is running on Spigot.
	 * 
	 * @return Boolean whether this plugin is running on Bungeecord or not.
	 */
	public Boolean isRunningOnBungeecord() {
		GlobalManager manager = GlobalManager.getInstance();
		if(manager.getServiceManager().isServiceRegistered(MinecraftService.class)) {
			MinecraftService mcService = (MinecraftService) manager.getServiceManager().getService(MinecraftService.class);
			if(mcService.getMinecraftType().equals(MinecraftType.SPIGOT)) {
				return false;
			}else if(mcService.getMinecraftType().equals(MinecraftType.BUNGEECORD)) {
				return true;
			}
		}
		return null;
	}
	
	/**
	 * Returns the Configuration of the plugin.
	 * Uses {@link de.neo.rankbridge.shared.manager.MinecraftManager#getConfig(Boolean)}
	 * 
	 * @return The return of {@link de.neo.rankbridge.shared.manager.MinecraftManager#getConfig(Boolean)}
	 */
	public Object getConfig() {
		return this.getConfig(this.isRunningOnBungeecord());
	}
	
	/**
	 * Returns the Configuration of the plugin.
	 * 
	 * @param isRunningOnBungeecord Boolean whether this plugin is running on Bungeecord or not.
	 * @return An Object which is a {@link net.md_5.bungee.config.Configuration} or a {@link org.bukkit.configuration.file.FileConfiguration}
	 */
	public Object getConfig(Boolean isRunningOnBungeecord) {
		if(isRunningOnBungeecord) {
			BungeeMain bungee = (BungeeMain) GlobalManager.getInstance().getServiceManager().getService(BungeeService.class).getExternalService().getMain();
			Configuration config = bungee.getConfig();
			return config;
		}else {
			SpigotService spigot = (SpigotService) GlobalManager.getInstance().getServiceManager().getService(SpigotService.class);
			FileConfiguration config = spigot.getMain().getConfig();
			return config;
		}
	}
	
	/**
	 * Makes a GET Request to get the name of an OfflinePlayer in BungeeCord.
	 * 
	 * @param uuid the uuid of the player.
	 * @return the name of the player.
	 */
	private String getBungeeName(String uuid) {
		try {
			URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.replace("-", ""));
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			String response = parse(con.getInputStream());
			Gson gson = new Gson();
			JsonObject obj = gson.fromJson(response, JsonObject.class);
			return obj.get("name").getAsString();
		}catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * parses the response.
	 * 
	 * @param is the InputStream to parse.
	 * @return the parsed String.
	 * @throws IOException something went wrong.
	 */
	private String parse(InputStream is) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		
		String az;
		while((az = br.readLine()) != null) {
			sb.append(az);
			sb.append("\n");
		}
		br.close();
		
		return sb.toString().trim();
	}
}
