package de.neo.rankbridge.discord.listener;

import de.neo.rankbridge.shared.manager.MinecraftManager;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Listen for the MemberJoinEvent.
 * 
 * @author Neo8
 * @version 1.0
 */
public class MemberJoinListener extends ListenerAdapter {
	
	/**
	 * Executes the Event.
	 */
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent e) {
		try {
			MinecraftManager mm = MinecraftManager.getInstance();
			e.getJDA().openPrivateChannelById(e.getMember().getId()).complete().sendMessage(mm.getString("messages.discord.verify_info")).complete();
		}catch(ErrorResponseException e1) {
			//nothing to do...
		}
	}
}
