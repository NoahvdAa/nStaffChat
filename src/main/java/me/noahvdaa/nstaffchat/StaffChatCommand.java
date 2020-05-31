package me.noahvdaa.nstaffchat;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class StaffChatCommand extends Command {

	public StaffChatCommand() {
		super("staffchat", "nstaffchat.use", "sc");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)) {
			sender.sendMessage(new TextComponent(
					ChatColor.translateAlternateColorCodes('&', "&cYou must be a player.")));
			return;
		}
		ProxiedPlayer p = (ProxiedPlayer) sender;
		String format = NStaffChat.getInstance().getMessage("staffmessage")
				.replace("{server}", p.getServer().getInfo().getName()).replace("{player}", p.getName())
				.replace("{message}", String.join(" ", args));
		NStaffChat.getInstance().tellStaff(format, "nstaffchat.use");
	}

}
