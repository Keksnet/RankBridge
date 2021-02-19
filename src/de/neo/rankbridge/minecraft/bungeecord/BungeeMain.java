package de.neo.rankbridge.minecraft.bungeecord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.logging.Level;

import de.neo.rankbridge.SyncService;
import de.neo.rankbridge.discord.DiscordMain;
import de.neo.rankbridge.minecraft.MinecraftService;
import de.neo.rankbridge.minecraft.bungeecord.cmd.BungeeVerify;
import de.neo.rankbridge.shared.event.events.MinecraftLoadEvent;
import de.neo.rankbridge.shared.event.events.MinecraftReadyEvent;
import de.neo.rankbridge.shared.event.events.MinecraftLoadEvent.MinecraftType;
import de.neo.rankbridge.shared.manager.GlobalManager;
import de.neo.rankbridge.teamspeak.TeamSpeakMain;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class BungeeMain extends Plugin{
	
	private Configuration config;
	
	public void onEnable() {
		GlobalManager manager = SyncService.isGlobalManagerRegistered() ? GlobalManager.getInstance() : new GlobalManager();
		SyncService.registerGlobalManager();
		MinecraftLoadEvent loadEvent = new MinecraftLoadEvent(BungeeService.class, MinecraftType.BUNGEECORD);
		manager.getEventHandler().executeEvent(loadEvent);
		if(loadEvent.isCancelled()) {
			this.onDisable();
			return;
		}
		MinecraftService mcService = new MinecraftService(MinecraftType.BUNGEECORD, this.getClass());
		manager.getServiceManager().register(mcService);
		BungeeService service = new BungeeService(this);
		manager.getServiceManager().register(service);
		
		loadConfig();
		if(this.config.getBoolean("discord.enable")) {
			new DiscordMain();
		}
		if(this.config.getBoolean("teamspeak.enable")) {
			new TeamSpeakMain();
		}
		
		getProxy().getPluginManager().registerCommand(this, new BungeeVerify());
		
		MinecraftReadyEvent readyEvent = new MinecraftReadyEvent(BungeeService.class, MinecraftType.BUNGEECORD);
		manager.getEventHandler().executeEvent(readyEvent);
	}
	
	public Configuration getConfig() {
		return this.config;
	}
	
	public void loadConfig() {
		try {
			if(!getDataFolder().exists()) {
				getDataFolder().mkdir();
			}
			File f = new File(getDataFolder(), "config.yml");
			Boolean setup = false;
			if(!f.exists()) {
				Files.copy(getResourceAsStream("bungee_config.yml"), f.toPath());
				setup = true;
			}
			this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(f);
			if(setup) {
				this.config.set("discord.enable", false);
				this.config.set("discord.token", "BOT_TOKEN_HERE");
				this.config.set("discord.activity", "verifing players");
				this.config.set("discord.guild", 0l);
				ArrayList<String> discord_group = new ArrayList<>();
				discord_group.add("group.admin, 0");
				this.config.set("discord.groups", discord_group);
				
				this.config.set("teamspeak.enable", false);
				this.config.set("teamspeak.user", "username");
				this.config.set("teamspeak.password", "password");
				this.config.set("teamspeak.host", "127.0.0.1");
				this.config.set("teamspeak.port", 10011);
				this.config.set("teamspeak.vserver", 1);
				this.config.set("teamspeak.nickname", "Verify Bot");
				ArrayList<String> teamspeak_group = new ArrayList<>();
				teamspeak_group.add("group.admin, 6");
				this.config.set("teamspeak.groups", teamspeak_group);
				ConfigurationProvider.getProvider(YamlConfiguration.class).save(this.config, f);
				getProxy().getLogger().log(Level.WARNING, "config generated.");
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
