package com.github.stephengardner.globalmute;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Mutechat extends JavaPlugin implements Listener, CommandExecutor {

	private ArrayList<String> muteList;
	private HashMap<String, String> lang;

	public String color(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}

	@Override
	public void onEnable() {
		saveDefaultConfig();

		lang = new HashMap<String, String>();
		lang.put("MUTED", color(getConfig().getString("NoChat.Language.Muted", "&cGlobal chat is muted. Type '/nochat' to chat in global.")));
		lang.put("MUTEON", color(getConfig().getString("NoChat.Language.MuteOn", "&eGlobal chat muted.")));
		lang.put("MUTEOFF", color(getConfig().getString("NoChat.Language.MuteOff", "&eGlobal chat unmuted.")));

		muteList = new ArrayList<String>();
		getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable() {
		muteList.clear();
		lang.clear();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("nochat")) {
			if (!(sender instanceof Player)) {
				return true;
			}

			String senderName = sender.getName();

			if (muteList.contains(senderName)) {
				muteList.remove(senderName);
				sender.sendMessage(lang.get("MUTEOFF"));
			} else {
				muteList.add(senderName);
				sender.sendMessage(lang.get("MUTEON"));
			}
		}

		return true;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		Player sender = e.getPlayer();

		if (!e.isAsynchronous() || e.isCancelled()) {
			return;
		}

		ArrayList<Player> muteList = new ArrayList<Player>();

		for (String p : this.muteList) {
			if (Bukkit.getServer().getOfflinePlayer(p).isOnline()) {
				Player player = Bukkit.getServer().getPlayer(p);

				if (player.hasPermission("nochat.toggle")) {
					muteList.add(player);
				}
			}
		}

		if (muteList.contains(sender)) {
			if (!sender.hasPermission("nochat.override")) {
				sender.sendMessage(lang.get("MUTED"));
				e.setCancelled(true);
			}

			muteList.remove(sender);
		}

		e.getRecipients().removeAll(muteList);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		String p = e.getPlayer().getName();

		if (muteList.contains(p)) {
			muteList.remove(p);
		}
	}

}