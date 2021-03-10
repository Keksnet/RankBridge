package de.neo.rankbridge.minecraft.spigot.cmd;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.neo.rankbridge.minecraft.spigot.SpigotMain;
import de.neo.rankbridge.minecraft.spigot.SpigotService;
import de.neo.rankbridge.shared.event.events.message.BridgeMessageSendEvent;
import de.neo.rankbridge.shared.manager.BridgeServiceManager;
import de.neo.rankbridge.shared.manager.GlobalManager;
import de.neo.rankbridge.shared.manager.MinecraftManager;
import de.neo.rankbridge.shared.manager.services.BridgeService;
import de.neo.rankbridge.shared.manager.services.ExternalService;
import de.neo.rankbridge.shared.message.BridgeMessage;
import de.neo.rankbridge.shared.message.BridgeMessage.ConversationMember;

/**
 * The /verify command.
 * 
 * @author Neo8
 * @version 1.0
 */
public class SpigotVerify implements CommandExecutor {
	
	/**
	 * Executes the command.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(p.hasPermission("system.verify")) {
				BridgeMessage<String> msg = new BridgeMessage<>(ConversationMember.MINECRAFT);
				Integer groupTS = 0;
				Long groupDC = 0l;
				String code = String.valueOf(ThreadLocalRandom.current().nextInt(999, 9999));
				GlobalManager manager = GlobalManager.getInstance();
				BridgeServiceManager services = manager.getServiceManager();
				BridgeService service = (BridgeService) services.getService(SpigotService.class);
				ExternalService<?> ext = service.getExternalService();
				SpigotMain main = (SpigotMain) ext.getMain();
				for(String val : (ArrayList<String>) main.getConfig().getList("teamspeak.groups")) {
					String k = (String) val.split(",")[0];
					String v = (String) val.split(",")[1].replace(" ", "");
					if(p.hasPermission(k)) {
						groupTS = Integer.valueOf(v);
					}
				}
				for(String val : (ArrayList<String>) main.getConfig().getList("discord.groups")) {
					String k = (String) val.split(",")[0];
					String v = (String) val.split(",")[1].replace(" ", "");
					if(p.hasPermission(k)) {
						groupDC = Long.valueOf(v);
					}
				}
				main.addCode(code, p.getUniqueId().toString());
				MinecraftManager mgr = MinecraftManager.getInstance();
				p.sendMessage(mgr.getString("messages.minecraft.verify_info"));
				p.getPlayer().sendMessage(mgr.getString("messages.minecraft.code_info").replace("%code%", code));
				msg.setContent("ADD_CODE;" + code + ";" + String.valueOf(groupTS) + ";" + String.valueOf(groupDC) + ";" + p.getUniqueId().toString() + ";" + p.getAddress().getAddress().getHostAddress());
				BridgeMessageSendEvent sendEvent = new BridgeMessageSendEvent(SpigotService.class, msg);
				GlobalManager.getInstance().getEventHandler().executeEvent(sendEvent);
			}
		}
		return false;
	}

}
