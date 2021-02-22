package de.neo.rankbridge.minecraft.listener;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import de.neo.rankbridge.minecraft.bungeecord.BungeeMain;
import de.neo.rankbridge.minecraft.bungeecord.BungeeService;
import de.neo.rankbridge.shared.event.BridgeEventListener;
import de.neo.rankbridge.shared.event.events.BridgeEvent;
import de.neo.rankbridge.shared.event.events.MinecraftLoadEvent.MinecraftType;
import de.neo.rankbridge.shared.event.events.message.BridgeMessageSendEvent;
import de.neo.rankbridge.shared.manager.GlobalManager;
import de.neo.rankbridge.shared.manager.MinecraftManager;
import de.neo.rankbridge.shared.message.BridgeMessage.ConversationMember;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MessageSendListener implements BridgeEventListener {
	
	private MinecraftType type;
	
	public MessageSendListener(MinecraftType type) {
		this.type = type;
	}

	@Override
	public void execute(BridgeEvent event) {
		BridgeMessageSendEvent e = (BridgeMessageSendEvent) event;
		Boolean allowed = false;
		if(e.getMessage().getSender().equals(ConversationMember.MINECRAFT)) {
			return;
		}
		for(ConversationMember mem : e.getMessage().getReceiver()) {
			if(mem.equals(ConversationMember.MINECRAFT)) {
				allowed = true;
				break;
			}
		}
		if(!allowed) {
			return;
		}
		GlobalManager manager = GlobalManager.getInstance();
		if(e.getMessage().getContentUniversal().getAsString().startsWith("REQUEST_CODE;")) {
			String[] args = e.getMessage().getContentUniversal().getAsString().split(";");
			if(args.length == 3) {
				MinecraftManager mgr = MinecraftManager.getInstance();
				String code = args[1];
				String uuid = args[2];
				if(type.equals(MinecraftType.SPIGOT)) {
					OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
					if(p.isOnline()) {
						p.getPlayer().sendMessage(mgr.getString("messages.minecraft.code_info").replace("%code%", code));
					}
				}else if(type.equals(MinecraftType.BUNGEECORD)) {
					BungeeMain main = (BungeeMain) manager.getServiceManager().getService(BungeeService.class).getExternalService().getMain();
					ProxiedPlayer p = main.getProxy().getPlayer(UUID.fromString(uuid));
					if(p != null && p.isConnected()) {
						p.sendMessage(new TextComponent(mgr.getString("messages.minecraft.code_info").replace("%code%", code)));
					}
				}
			}
		}else if(e.getMessage().getContentUniversal().getAsString().startsWith("VERIFIED;")) {
			String[] args = e.getMessage().getContentUniversal().getAsString().split(";");
			if(args.length == 4) {
				MinecraftManager mgr = MinecraftManager.getInstance();
				String id = args[2];
				String uuid = args[3];
				if(type.equals(MinecraftType.SPIGOT)) {
					OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
					if(p.isOnline()) {
						p.getPlayer().sendMessage(mgr.getString("messages.minecraft.verified").replace("%user%", id));
					}
				}else if(type.equals(MinecraftType.BUNGEECORD)) {
					BungeeMain main = (BungeeMain) manager.getServiceManager().getService(BungeeService.class).getExternalService().getMain();
					ProxiedPlayer p = main.getProxy().getPlayer(UUID.fromString(uuid));
					if(p != null && p.isConnected()) {
						p.sendMessage(new TextComponent(mgr.getString("messages.minecraft.verified").replace("%user%", id)));
					}
				}
			}
		}
	}

}
