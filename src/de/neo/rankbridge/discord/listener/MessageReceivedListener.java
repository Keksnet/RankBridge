package de.neo.rankbridge.discord.listener;

import de.neo.rankbridge.discord.DiscordMain;
import de.neo.rankbridge.minecraft.MinecraftService;
import de.neo.rankbridge.shared.event.events.message.BridgeMessageSendEvent;
import de.neo.rankbridge.shared.manager.BridgeServiceManager;
import de.neo.rankbridge.shared.manager.GlobalManager;
import de.neo.rankbridge.shared.manager.MinecraftManager;
import de.neo.rankbridge.shared.message.BridgeMessage;
import de.neo.rankbridge.shared.message.BridgeMessage.ConversationMember;
import de.neo.rankbridge.shared.util.MultiVar;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageReceivedListener extends ListenerAdapter {
	
	@Override
	public void onMessageReceived(MessageReceivedEvent e) {
		if(e.getChannelType().equals(ChannelType.PRIVATE)) {
			GlobalManager manager = GlobalManager.getInstance();
			BridgeServiceManager services = manager.getServiceManager();
			DiscordMain main = (DiscordMain) services.getService(DiscordMain.class);
			String code = e.getMessage().getContentRaw();
			MultiVar vars = main.getCode(code);
			if(vars != null) {
				if(services.isServiceRegistered(MinecraftService.class)) {
					MinecraftManager mgr = MinecraftManager.getInstance();
					Long guild = mgr.getLong("discord.guild");
					Guild g = main.getJDA().getGuildById(guild);
					g.addRoleToMember(g.getMember(e.getAuthor()), g.getRoleById(vars.get(0))).queue();
					try {
						String verified = mgr.getString("message.discord.verified").replace("%playername%", mgr.getName(vars.get(1)).replace("%uuid%", vars.get(1)));
						main.getJDA().openPrivateChannelById(e.getAuthor().getId()).complete().sendMessage(verified).complete();
					}catch(ErrorResponseException e1) {
						//nothing.
					}
					main.removeCode(code);
					BridgeMessage<String> msg = new BridgeMessage<>(ConversationMember.DISCORD);
					msg.setContent("VERIFIED;" + code + ";" + e.getAuthor().getId());
					BridgeMessageSendEvent sendEvent = new BridgeMessageSendEvent(DiscordMain.class, msg);
					manager.getEventHandler().executeEvent(sendEvent);
				}
			}
		}
	}
}
