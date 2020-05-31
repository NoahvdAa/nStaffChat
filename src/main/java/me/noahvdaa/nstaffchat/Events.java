package me.noahvdaa.nstaffchat;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Events implements Listener {

	@EventHandler
	public void playerJoin(ServerSwitchEvent e) {
		// This is not a join event, but a move.
		if (e.getFrom() != null)
			return;
		if (!e.getPlayer().hasPermission("nstaffchat.use"))
			return;
		if (!NStaffChat.getInstance().hasStaffJoinEnabled())
			return;

		// Tell the rest of the staff.
		String message = NStaffChat.getInstance().getMessage("staffjoin");
		message = message.replace("{player}", e.getPlayer().getName()).replace("{server}",
				e.getPlayer().getServer().getInfo().getName());
		NStaffChat.getInstance().tellStaff(message, "nstaffchat.use");
	}

	@EventHandler
	public void serverSwitch(ServerSwitchEvent e) {
		// This is not a move event, but a join.
		if (e.getFrom() == null)
			return;
		if (!e.getPlayer().hasPermission("nstaffchat.use"))
			return;
		if (!NStaffChat.getInstance().hasStaffMoveEnabled())
			return;

		// Tell the rest of the staff.
		String message = NStaffChat.getInstance().getMessage("staffmove");
		message = message.replace("{player}", e.getPlayer().getName())
				.replace("{to}", e.getPlayer().getServer().getInfo().getName())
				.replace("{from}", e.getFrom().getName());
		NStaffChat.getInstance().tellStaff(message, "nstaffchat.use");
	}

	@EventHandler
	public void playerDisconnect(PlayerDisconnectEvent e) {
		// Disable the staffchat toggle if they have it enabled.
		NStaffChat.getInstance().setStaffChatToggled(e.getPlayer(), false);

		if (!e.getPlayer().hasPermission("nstaffchat.use"))
			return;
		if (!NStaffChat.getInstance().hasStaffLeaveEnabled())
			return;

		// Tell the rest of the staff.
		String message = NStaffChat.getInstance().getMessage("staffleave");
		message = message.replace("{player}", e.getPlayer().getName()).replace("{server}",
				e.getPlayer().getServer().getInfo().getName());
		NStaffChat.getInstance().tellStaff(message, "nstaffchat.use");
	}

	@EventHandler
	public void playerChat(ChatEvent e) {
		if (e.getMessage().startsWith("/"))
			return;
		ProxiedPlayer p = (ProxiedPlayer) e.getSender();
		if (!p.hasPermission("nstaffchat.use"))
			return;
		if (!(NStaffChat.getInstance().hasStaffChatPrefixEnabled()
				&& e.getMessage().startsWith(NStaffChat.getInstance().getMessage("prefix", false)))
				&& !NStaffChat.getInstance().hasStaffChatToggled(p))
			return;

		String message;
		if (!NStaffChat.getInstance().hasStaffChatToggled(p)) {
			message = e.getMessage().substring(NStaffChat.getInstance().getMessage("prefix", false).length());
		} else {
			message = e.getMessage();
		}

		String format = NStaffChat.getInstance().getMessage("staffmessage")
				.replace("{server}", p.getServer().getInfo().getName()).replace("{player}", p.getName())
				.replace("{message}", message);
		NStaffChat.getInstance().tellStaff(format, "nstaffchat.use");
		e.setCancelled(true);
	}

}
