package com.minesworn.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.minesworn.Config;
import com.minesworn.Permission;
import com.minesworn.PlayerDatabase;
import com.minesworn.PlayerInfo;
import com.minesworn.SwornGuard;
import com.minesworn.detectors.ChatManager;

public class ChatListener implements Listener {

	@EventHandler (priority = EventPriority.LOWEST)
	public void onPlayerChatEvent(final AsyncPlayerChatEvent e) {
		if (!Permission.hasPermission(e.getPlayer(), Permission.CAN_SPAM) 
				&& ChatManager.isSpam(e.getPlayer().getName(), e.getMessage(), "messages")) {
			e.setCancelled(true);
			return;
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(SwornGuard.p, new Runnable() {
			public void run() {
				PlayerInfo i = PlayerDatabase.getPlayer(e.getPlayer().getName());
				i.setMessageCount(i.getMessageCount() + 1);
			}
		});
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void onPlayerCommandPreprocessEvent (PlayerCommandPreprocessEvent e) {		
		if (!Permission.hasPermission(e.getPlayer(), Permission.CAN_SPAM) 
				&& ChatManager.isSpam(e.getPlayer().getName(), e.getMessage(), "commands")) {
			e.setCancelled(true);
			return;
		}
		
		String command = e.getMessage().toLowerCase().split(" ")[0].replace("/", "");
		
		for (String s : Config.blockedCommands) {
			if (command.equals(s) && !Permission.hasPermission(e.getPlayer(), Permission.CAN_USE_BLOCKED_CMDS)) {
				e.setCancelled(true);				
				return;
			}
		}
				
		ChatManager.checkCommand(e.getPlayer(), command, e.getMessage());
	}
	
}
