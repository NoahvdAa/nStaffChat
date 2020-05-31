package me.noahvdaa.nstaffchat;

import java.io.IOException;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class NStaffChatCommand extends Command {

	public NStaffChatCommand() {
		super("nstaffchat", "", "nsc");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length != 0 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("nstaffchat.reload")) {
			sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&6Reloading...")));
			try {
				NStaffChat.getInstance().reloadConfig();
			} catch (IOException e) {
				sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&',
						"&cSomething went wrong while reloading the config! Check the console for more information. Try to fix this as soon as possible, as broken configs may cause issues.")));
				e.printStackTrace();
				return;
			}
			sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&aReload successful!")));
		} else {
			String v = NStaffChat.getInstance().getDescription().getVersion();
			sender.sendMessage(new TextComponent(
					ChatColor.translateAlternateColorCodes('&', "&enStaffChat v" + v + " by &bNoahvdAa&e.")));
		}
	}

}
