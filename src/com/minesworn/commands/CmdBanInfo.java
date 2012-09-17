package com.minesworn.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import com.minesworn.Permission;
import com.minesworn.PlayerDatabase;
import com.minesworn.PlayerInfo;
import com.minesworn.lang.CommandError;
import com.minesworn.misc.Util;

public class CmdBanInfo extends SGCommand {

	public CmdBanInfo() {
		this.name = "baninfo";
		this.aliases.add("bi");
		this.description = "Checks a player's ban information.";
		this.permission = Permission.BAN_INFO;
		this.optionalArgs.add("player");
		this.mustBePlayer = true;
	}
	
	public void perform() {
		OfflinePlayer target;
		if (args.length == 0)
			target = player;
		else {
			target = Util.matchPlayer(args[0]);
			if (target == null)
				target = Bukkit.getOfflinePlayer(args[0]);
		}
		
		PlayerInfo i = PlayerDatabase.getPlayer(target.getName(), false);
		if (i == null) {
			error(CommandError.PLAYER_NOT_FOUND);
			return;
		}
		
		ArrayList<String> lines = new ArrayList<String>();
		
		StringBuilder ln = new StringBuilder();
		ln.append("Player " + ChatColor.GREEN + target.getName() + ChatColor.YELLOW + " is currently ");
		if (Util.isBanned(target))
			ln.append(ChatColor.RED + "BANNED");
		else
			ln.append("not banned");
		lines.add(ln.toString());
		
		if (i.getBanCount() != 0) {
			ln = new StringBuilder();
			ln.append("Last ban by " + i.getLastBannedBy() + " on " + Util.getSimpleDate(i.getLastBanTime()) 
					+ "(" + Util.formatTimeDifference(i.getLastBanTime(), System.currentTimeMillis()) + ")");
			lines.add(ln.toString());
			
			ln = new StringBuilder();
			ln.append("Last ban reason: " + i.getLastBanReason());
			lines.add(ln.toString());
		}
		
		if (i.getLastUnbanTime() != 0) {
			ln = new StringBuilder();
			ln.append("Unbanned by " + i.getLastUnbannedBy() + " on " + Util.getSimpleDate(i.getLastUnbanTime()) 
					+ "(" + Util.formatTimeDifference(i.getLastUnbanTime(), System.currentTimeMillis()) + ")");
			lines.add(ln.toString());
		}
		
		for (String s : lines) {
			confirm(s);
		}
	}
	
}
