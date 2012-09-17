package com.minesworn;

import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.minesworn.commands.CmdHelp;
import com.minesworn.commands.SGCommand;
import com.minesworn.commands.SGCommandRoot;
import com.minesworn.detectors.FlyDetector;
import com.minesworn.events.CheatEvent;
import com.minesworn.listeners.BlockListener;
import com.minesworn.listeners.ChatListener;
import com.minesworn.listeners.EntityListener;
import com.minesworn.listeners.JailListener;
import com.minesworn.listeners.NationsListener;
import com.minesworn.listeners.PlayerJoinQuitListener;
import com.minesworn.misc.Util;
import com.minesworn.threads.SaveRunnable;

public class SwornGuard extends JavaPlugin {
	public static SwornGuard p;
	public static boolean autoModBotEnabled = false;
	
	@Override
	public void onEnable() {
		p = this;
		Config.load();
		
		new SaveRunnable();
		new FlyDetector();
		Bukkit.getPluginManager().registerEvents(new PlayerJoinQuitListener(), p);
		Bukkit.getPluginManager().registerEvents(new BlockListener(), p);
		Bukkit.getPluginManager().registerEvents(new ChatListener(), p);
		Bukkit.getPluginManager().registerEvents(new EntityListener(), p);
		Bukkit.getPluginManager().registerEvents(new JailListener(), p);
		Bukkit.getPluginManager().registerEvents(new NationsListener(), p);
		
		autoModBotEnabled = Config.autoModeratorAlwaysEnabled;
	}
	
	public void onDisable() {
		PlayerDatabase.saveDB();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("p") || cmd.getName().equalsIgnoreCase("sg") || cmd.getName().equalsIgnoreCase("swornguard")) {
			for (SGCommand command : SGCommandRoot.commands) {
				if (args.length == 0) {
					new CmdHelp().execute(sender, args);
					return true;
				}
				
				String cmdlbl = args[0];
				if (cmdlbl.equalsIgnoreCase(command.name) || command.aliases.contains(cmdlbl.toLowerCase())) {
					ArrayList<String> argList = new ArrayList<String>();
					for (int i = 1; i < args.length; i++) {
						argList.add(args[i]);
					}
					
					args = argList.toArray(new String[0]);				
					
					command.execute(sender, args);
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static void announceCheat(CheatEvent e) {
		Bukkit.getPluginManager().callEvent(e);
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (Permission.hasPermission(p, Permission.CAN_SEE_CHEAT_REPORTS)) {
				p.sendMessage(ChatColor.RED + e.getMessage());
			}
		}
		
		if (AutoModerator.isOnlyModeratorOnline() || autoModBotEnabled)
			AutoModerator.manageCheatEvent(e);
		
		PlayerInfo i = PlayerDatabase.getPlayer(e.getPlayer().getName());
		i.getProfilerList().add("[" + Util.getLongDateCurr() + " GMT] " + ChatColor.RED + e.getPlayer().getName() + " pinged the cheat detector for: " + ChatColor.GOLD + e.getType().toString());
		
		log("Cheatevent player: " + e.getPlayer().getName());
		log("Cheatevent type: " + e.getType().toString());
	}
	
	public static void log(String msg) {
		log(Level.INFO, msg);
	}
	
	public static void log(Level lvl, String msg) {
		Bukkit.getLogger().log(lvl, "[SwornGuard] " + msg);
	}
	
}
