package de.neo.rankbridge.discord.listener;

import org.bukkit.configuration.file.FileConfiguration;

import de.neo.rankbridge.discord.DiscordMain;
import de.neo.rankbridge.minecraft.MinecraftService;
import de.neo.rankbridge.minecraft.bungeecord.BungeeMain;
import de.neo.rankbridge.minecraft.bungeecord.BungeeService;
import de.neo.rankbridge.minecraft.spigot.SpigotService;
import de.neo.rankbridge.shared.event.events.MinecraftLoadEvent.MinecraftType;
import de.neo.rankbridge.shared.event.events.message.BridgeMessageSendEvent;
import de.neo.rankbridge.shared.manager.BridgeServiceManager;
import de.neo.rankbridge.shared.manager.GlobalManager;
import de.neo.rankbridge.shared.message.BridgeMessage;
import de.neo.rankbridge.shared.message.BridgeMessage.ConversationMember;
import de.neo.rankbridge.shared.util.MultiVar;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.md_5.bungee.config.Configuration;

public class MessageReceivedListener extends ListenerAdapter {
	
	@Override
	public void onMessageReceived(MessageReceivedEvent e) {
		if(e.getChannelType().equals(ChannelType.PRIVATE)) {
			System.out.println("msg");
			GlobalManager manager = GlobalManager.getInstance();
			BridgeServiceManager services = manager.getServiceManager();
			DiscordMain main = (DiscordMain) services.getService(DiscordMain.class);
			String code = e.getMessage().getContentRaw();
			MultiVar vars = main.getCode(code);
			if(vars != null) {
				System.out.println("vars");
				if(services.isServiceRegistered(MinecraftService.class)) {
					System.out.println("durch");
					MinecraftService mcService = (MinecraftService) services.getService(MinecraftService.class);
					Long guild = 0l;
					if(mcService.getMinecraftType().equals(MinecraftType.SPIGOT)) {
						SpigotService spigot = (SpigotService) services.getService(SpigotService.class);
						FileConfiguration config = spigot.getMain().getConfig();
						guild = config.getLong("discord.guild");
					}else if(mcService.getMinecraftType().equals(MinecraftType.BUNGEECORD)) {
						BungeeMain bungee = (BungeeMain) ((BungeeService)services.getService(BungeeService.class)).getMain();
						Configuration config = bungee.getConfig();
						guild = config.getLong("discord.guild");
					}
					Guild g = main.getJDA().getGuildById(guild);
					g.addRoleToMember(g.getMember(e.getAuthor()), g.getRoleById(vars.get(0))).queue();
					main.getJDA().openPrivateChannelById(e.getAuthor().getId()).complete().sendMessage(":white_check_mark: successful verified with ```" + vars.get(1) + "``` :white_check_mark:").queue();
					BridgeMessage<String> msg = new BridgeMessage<>(ConversationMember.DISCORD);
					msg.setContent("VERIFIED;" + code + ";" + e.getAuthor().getId());
					BridgeMessageSendEvent sendEvent = new BridgeMessageSendEvent(DiscordMain.class, msg);
					manager.getEventHandler().executeEvent(sendEvent);
				}
			}
		}
	}
}
