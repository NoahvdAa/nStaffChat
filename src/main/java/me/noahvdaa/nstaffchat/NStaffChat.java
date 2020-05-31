package me.noahvdaa.nstaffchat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.UUID;

import org.bstats.bungeecord.Metrics;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class NStaffChat extends Plugin {

	private Configuration config;
	private static NStaffChat instance;
	private ArrayList<UUID> toggled = new ArrayList<UUID>();

	@Override
	public void onEnable() {
		instance = this;

		// Does the data folder exist?
		if (!getDataFolder().exists())
			getDataFolder().mkdir();

		File file = new File(getDataFolder(), "config.yml");

		// Does the config file exist?
		if (!file.exists()) {
			// Make it.
			try (InputStream in = getResourceAsStream("config.yml")) {
				Files.copy(in, file.toPath());
			} catch (IOException e) {
				// Something went terribly wrong!
				getLogger().warning(
						"Something went wrong while saving the default configuration file. Please report the exception below on this page: http://github.com/NoahvdAa/nStaffChat.");
				e.printStackTrace();
				return;
			}
		}

		// Load the config.
		try {
			reloadConfig();
		} catch (IOException e) {
			getLogger().warning(
					"Something went wrong while loading the configuration file. Please report the exception below on this page: http://github.com/NoahvdAa/nStaffChat.");
			e.printStackTrace();
			return;
		}

		// Register events.
		getProxy().getPluginManager().registerListener(this, new Events());
		
		// Register commands.
		getProxy().getPluginManager().registerCommand(this, new NStaffChatCommand());
		getProxy().getPluginManager().registerCommand(this, new StaffChatCommand());
		getProxy().getPluginManager().registerCommand(this, new ToggleStaffChatCommand());

		// Initialize bStats.
		Metrics metrics = new Metrics(this, 7654);
		if (!metrics.isEnabled()) {
			getLogger().info(
					"You have metrics disabled. Please consider enabling them as this helps developers stay motivated. Thanks :)");
		}
		metrics.addCustomChart(
				new Metrics.SimplePie("staff_join", () -> instance.hasStaffJoinEnabled() ? "Yes" : "No"));
		metrics.addCustomChart(
				new Metrics.SimplePie("staff_move", () -> instance.hasStaffMoveEnabled() ? "Yes" : "No"));
		metrics.addCustomChart(
				new Metrics.SimplePie("staff_leave", () -> instance.hasStaffLeaveEnabled() ? "Yes" : "No"));
		metrics.addCustomChart(
				new Metrics.SimplePie("staffchat_prefix", () -> instance.hasStaffChatPrefixEnabled() ? "Yes" : "No"));
	}

	public static NStaffChat getInstance() {
		if (instance == null) {
			instance = new NStaffChat();
		}
		return instance;
	}

	public boolean hasStaffChatToggled(ProxiedPlayer p) {
		return toggled.contains(p.getUniqueId());
	}

	public void setStaffChatToggled(ProxiedPlayer p, boolean newVal) {
		// Do we need to change?
		if (hasStaffChatToggled(p) != newVal) {
			if (newVal) {
				// Add them to the list off toggled players.
				toggled.add(p.getUniqueId());
			} else {
				// Remove them from the list off toggled players
				toggled.remove(p.getUniqueId());
			}
		}
	}
	
	public void reloadConfig() throws IOException {
		config = ConfigurationProvider.getProvider(YamlConfiguration.class)
				.load(new File(getDataFolder(), "config.yml"));
	}

	public boolean hasStaffJoinEnabled() {
		return !getMessage("staffjoin").isEmpty();
	}

	public boolean hasStaffMoveEnabled() {
		return !getMessage("staffmove").isEmpty();
	}

	public boolean hasStaffLeaveEnabled() {
		return !getMessage("staffleave").isEmpty();
	}

	public boolean hasStaffChatPrefixEnabled() {
		return !getMessage("prefix").isEmpty();
	}

	public String getMessage(String message) {
		return ChatColor.translateAlternateColorCodes('&',
				config.contains(message) ? config.getString(message) : "&cMessage not found!");
	}

	public String getMessage(String message, boolean translateColors) {
		if (!translateColors) {
			return config.contains(message) ? config.getString(message) : "&cMessage not found!";
		}
		return getMessage(message);
	}

	@SuppressWarnings("deprecation")
	public void tellStaff(String message, String permissionRequired) {
		for (ProxiedPlayer p : getProxy().getPlayers()) {
			if (p.hasPermission(permissionRequired)) {
				p.sendMessage(message);
			}
		}
	}

}
