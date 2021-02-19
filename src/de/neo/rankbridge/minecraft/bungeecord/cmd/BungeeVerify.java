package de.neo.rankbridge.minecraft.bungeecord.cmd;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import de.neo.rankbridge.minecraft.bungeecord.BungeeMain;
import de.neo.rankbridge.minecraft.bungeecord.BungeeService;
import de.neo.rankbridge.shared.event.events.message.BridgeMessageSendEvent;
import de.neo.rankbridge.shared.manager.BridgeServiceManager;
import de.neo.rankbridge.shared.manager.GlobalManager;
import de.neo.rankbridge.shared.manager.services.BridgeService;
import de.neo.rankbridge.shared.manager.services.ExternalService;
import de.neo.rankbridge.shared.message.BridgeMessage;
import de.neo.rankbridge.shared.message.BridgeMessage.ConversationMember;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BungeeVerify extends Command {

	public BungeeVerify() {
		super("verify", "system.verify", new String[] {"verify-ts", "verify-dc"});
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) sender;
			BridgeMessage<String> msg = new BridgeMessage<>(ConversationMember.MINECRAFT);
			Integer groupTS = 0;
			Long groupDC = 0l;
			String code = String.valueOf(ThreadLocalRandom.current().nextInt(999, 9999));
			GlobalManager manager = GlobalManager.getInstance();
			BridgeServiceManager services = manager.getServiceManager();
			BridgeService service = (BridgeService) services.getService(BungeeService.class);
			ExternalService<?> ext = service.getExternalService();
			BungeeMain main = (BungeeMain) ext.getMain();
			for(String val : (ArrayList<String>) main.getConfig().getList("teamspeak.groups")) {
				String k = (String) val.split(",")[0];
				String v = (String) val.split(",")[1].replace(" ", "");
				if(p.hasPermission(k)) {
					groupTS = Integer.getInteger(v);
				}
			}
			for(String val : (ArrayList<String>) main.getConfig().getList("teamspeak.groups")) {
				String k = (String) val.split(",")[0];
				String v = (String) val.split(",")[1].replace(" ", "");
				if(p.hasPermission(k)) {
					groupDC = Long.valueOf(v);
				}
			}
			msg.setContent("ADD_CODE;" + code + ";" + String.valueOf(groupTS) + ";" + String.valueOf(groupDC) + ";" + p.getUniqueId().toString());
			BridgeMessageSendEvent sendEvent = new BridgeMessageSendEvent(BungeeService.class, msg);
			GlobalManager.getInstance().getEventHandler().executeEvent(sendEvent);
			p.sendMessage(new TextComponent("§2[§6RankBridge§2] §aDein Verifizierungcode: " + code + ". Gib diesen Code im Discord oder im Teamspeak als Nickname ein um verifiziert."));
		}
	}

}
