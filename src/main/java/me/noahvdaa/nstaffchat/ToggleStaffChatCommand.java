package me.noahvdaa.nstaffchat;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ToggleStaffChatCommand extends Command {

	public ToggleStaffChatCommand() {
		super("togglestaffchat", "nstaffchat.use", "togglesc", "sctoggle", "staffchattoggle");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)) {
			sender.sendMessage(new TextComponent(
					ChatColor.translateAlternateColorCodes('&', "&cYou must be a player.")));
			return;
		}
		ProxiedPlayer p = (ProxiedPlayer) sender;
		boolean newState = !NStaffChat.getInstance().hasStaffChatToggled(p);
		NStaffChat.getInstance().setStaffChatToggled(p, newState);
		if(newState) {
			p.sendMessage(NStaffChat.getInstance().getMessage("enabled"));
		}else {
			p.sendMessage(NStaffChat.getInstance().getMessage("disabled"));
		}
	}

}
