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

public class CmdInfo extends SGCommand {

	public CmdInfo() {
		this.name = "info";
		this.aliases.add("i");
		this.aliases.add("stats");
		this.description = "Checks a player's statistics.";
		this.permission = Permission.INFO;
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
				
		if (!target.getName().equalsIgnoreCase(player.getName()) 
				&& !Permission.hasPermission(player, Permission.INFO_SEE_OTHERS)) {
			error(CommandError.PERMISSION);
			return;
		}
		
		PlayerInfo i = PlayerDatabase.getPlayer(target.getName(), false);
		if (i == null) {
			error(CommandError.PLAYER_NOT_FOUND);
			return;
		}
		
		ArrayList<String> lines = new ArrayList<String>();
		
		StringBuilder ln = new StringBuilder();
		ln.append("About " + ChatColor.GREEN + target.getName() + ChatColor.YELLOW + ": ");
		
		if (i.isOnlineNow())
			ln.append("Online now");
		else {
			ln.append("Last seen " + Util.formatTimeDifference(i.getLastOnlineTime(), 
					System.currentTimeMillis()) + " ago");
		}
		
		if (Permission.hasPermission(player, Permission.CAN_CHECK_IPS))
			ln.append(" from " + i.getIpAddress());
		else
			ln.append(".");
			
		lines.add(ln.toString());
		
		ln = new StringBuilder();
		ln.append("Logged in " + i.getLoginCount() + " time(s) since " 
				+ Util.getSimpleDate(i.getFirstOnlineTime()) + ".");
		lines.add(ln.toString());
		
		ln = new StringBuilder();
		ln.append("Built " + i.getBlockBuildCount() + " and deleted " 
				+ i.getBlockDeleteCount() + " blocks, wrote " + i.getMessageCount() + " messages.");
		lines.add(ln.toString());
		
		ln = new StringBuilder();
		ln.append("Killed " + i.getPlayerKills() + " players, " + i.getMobKills() 
				+ " monsters and " + i.getAnimalKills() + " animals.");
		lines.add(ln.toString());
		
		ln = new StringBuilder();
		ln.append("Has joined " + i.getFactionCount() + " factions. ");
		if (i.getMostRecentFaction() != "")
			ln.append("Last faction was " + i.getMostRecentFaction());
		lines.add(ln.toString());
		
		if (i.getNumPlayersKicked() != 0 || i.getNumPlayersBanned() != 0) {
			ln = new StringBuilder();
			ln.append("Kicked " + i.getNumPlayersKicked() + " and banned " + i.getNumPlayersBanned() + " players.");
			lines.add(ln.toString());
		}
		
		if (i.getKickCount() != 0) {
			ln = new StringBuilder();
			ln.append("Got kicked " + i.getKickCount() + " times. Last kick " 
					+ Util.formatTimeDifference(i.getLastKickTime(), System.currentTimeMillis()) 
					+ " ago by " + ChatColor.RED +  i.getLastKickedBy());
			lines.add(ln.toString());
			
			ln = new StringBuilder();
			ln.append("Kick reason: " + i.getLastKickReason());
			lines.add(ln.toString());
		}
		
		if (i.getJailCount() != 0) {
			ln = new StringBuilder();
			ln.append("Got jailed " + i.getJailCount() + " times. Last jail " 
					+ Util.formatTimeDifference(i.getLastJailTime(), System.currentTimeMillis()) 
					+ " ago by " + ChatColor.RED + i.getLastJailedBy());
			lines.add(ln.toString());
			
			ln = new StringBuilder();
			ln.append("Jail reason: " + i.getLastJailReason());
			lines.add(ln.toString());
		}
		
		i.updateSpentTime();
		
		ln = new StringBuilder();
		ln.append("Spent a total of " + Util.formatTimeDifference(0, i.getTotalTimeSpentOnServer()) + " here.");
		lines.add(ln.toString());
		
		for (String s : lines) {
			confirm(s);
		}
	}
	
}
