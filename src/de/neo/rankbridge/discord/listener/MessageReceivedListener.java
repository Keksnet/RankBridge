package de.neo.rankbridge.discord.listener;

import org.bukkit.configuration.file.FileConfiguration;

import de.neo.rankbridge.discord.DiscordMain;
import de.neo.rankbridge.minecraft.MinecraftService;
import de.neo.rankbridge.minecraft.spigot.SpigotService;
import de.neo.rankbridge.shared.event.events.MinecraftLoadEvent.MinecraftType;
import de.neo.rankbridge.shared.manager.GlobalManager;
import de.neo.rankbridge.shared.util.MultiVar;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReceivedListener extends ListenerAdapter {
	
	@Override
	public void onMessageReceived(MessageReceivedEvent e) {
		if(e.getChannelType().equals(ChannelType.PRIVATE)) {
			DiscordMain main = (DiscordMain) GlobalManager.getInstance().getServiceManager().getService(DiscordMain.class);
			MultiVar vars = main.getCode(e.getMessage().getContentRaw());
			if(vars != null) {
				if(GlobalManager.getInstance().getServiceManager().isServiceRegistered(MinecraftService.class)) {
					MinecraftService mcService = (MinecraftService) GlobalManager.getInstance().getServiceManager().getService(MinecraftService.class);
					Long guild = 0l;
					if(mcService.getMinecraftType().equals(MinecraftType.SPIGOT)) {
						SpigotService spigot = (SpigotService) GlobalManager.getInstance().getServiceManager().getService(SpigotService.class);
						FileConfiguration config = spigot.getMain().getConfig();
					}
				}
			}
		}
	}
}
