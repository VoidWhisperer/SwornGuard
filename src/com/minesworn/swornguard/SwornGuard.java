package com.minesworn.swornguard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import com.minesworn.swornguard.PermissionsManager.Permission;
import com.minesworn.swornguard.commands.SGCommandRoot;
import com.minesworn.swornguard.core.SPlugin;
import com.minesworn.swornguard.core.util.Util;
import com.minesworn.swornguard.detectors.FlyDetector;
import com.minesworn.swornguard.events.CheatEvent;
import com.minesworn.swornguard.lang.Lang;
import com.minesworn.swornguard.listeners.BlockListener;
import com.minesworn.swornguard.listeners.ChatListener;
import com.minesworn.swornguard.listeners.EntityListener;
import com.minesworn.swornguard.listeners.JailListener;
import com.minesworn.swornguard.listeners.NationsListener;
import com.minesworn.swornguard.listeners.PlayerJoinQuitListener;
import com.minesworn.swornguard.threads.SaveRunnable;

public class SwornGuard extends SPlugin {
	public static PlayerDatabase playerdatabase;
	public static ServerInfo serverInfo;
	public static boolean autoModBotEnabled = false;
	
	@Override
	public void onEnable() {
		preEnable();
		lang = new Lang();
		Lang.load();
		Config.load();
		playerdatabase = new PlayerDatabase();
		serverInfo = new ServerInfo();
		commandRoot = new SGCommandRoot();
		
		new SaveRunnable();
		
		if (Config.enableFlyDetector)
			new FlyDetector();
		
		registerEvents();
		
		autoModBotEnabled = Config.autoModeratorAlwaysEnabled;
	}
	
	public void onDisable() {
		preDisable();
		playerdatabase.save();
	}
	
	@Override
	public void afterReload() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			playerdatabase.addPlayer(p.getName());
		}
	}
	
	void registerEvents() {
		Bukkit.getPluginManager().registerEvents(new PlayerJoinQuitListener(), p);
		Bukkit.getPluginManager().registerEvents(new BlockListener(), p);
		Bukkit.getPluginManager().registerEvents(new ChatListener(), p);
		Bukkit.getPluginManager().registerEvents(new EntityListener(), p);
		if (isPluginEnabled("SwornJail"))
			Bukkit.getPluginManager().registerEvents(new JailListener(), p);
		if (Config.enableFactionBetrayalDetector && (isPluginEnabled("SwornNations") || isPluginEnabled("Factions")))
			Bukkit.getPluginManager().registerEvents(new NationsListener(), p);
	}
	
	public static void announceCheat(CheatEvent e) {
		Bukkit.getPluginManager().callEvent(e);
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (PermissionsManager.hasPermission(p, Permission.CAN_SEE_CHEAT_REPORTS.node)) {
				p.sendMessage(ChatColor.RED + e.getMessage());
			}
		}
		
		if ((AutoModerator.isOnlyModeratorOnline() || autoModBotEnabled) && Config.enableAutoModeratorBot)
			AutoModerator.manageCheatEvent(e);
		
		PlayerInfo i = playerdatabase.getPlayer(e.getPlayer().getName());
		if (e.getType() != Cheat.XRAY || (System.currentTimeMillis() - i.getLastWarnedForXray() > 432000000L)) {
			i.getProfilerList().add("[" + Util.getLongDateCurr() + " GMT] " + ChatColor.RED + "pinged the cheat detector for: " + ChatColor.GOLD + e.getType().toString());
		} 
		
		log("Cheatevent player: " + e.getPlayer().getName());
		log("Cheatevent type: " + e.getType().toString());
	}
	
}
