package com.minesworn.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import com.minesworn.Permission;
import com.minesworn.PlayerDatabase;
import com.minesworn.PlayerInfo;
import com.minesworn.lang.CommandError;
import com.minesworn.misc.Util;

public class CmdNote extends SGCommand {

	public CmdNote() {
		this.name = "note";
		this.aliases.add("n");
		this.description = "Adds a note to a player's profile.";
		this.permission = Permission.NOTE;
		this.requiredArgs.add("player");
		this.requiredArgs.add("note");
	}
	
	public void perform() {
		OfflinePlayer target = Util.matchPlayer(args[0]);
		if (target == null)
			target = Bukkit.getOfflinePlayer(args[0]);
			
		PlayerInfo i = PlayerDatabase.getPlayer(target.getName(), false);
		if (i == null) {
			error(CommandError.PLAYER_NOT_FOUND);
			return;
		}
		
		StringBuilder ln = new StringBuilder();
		ln.append("[" + Util.getLongDateCurr() + " GMT] " + ChatColor.GREEN + player.getName() + ": ");
		for (int x = 1; x < args.length; x++) {
			ln.append(args[x] + " ");
		}
		
		i.getProfilerList().add(ln.toString());
		confirm("Note added!");
	}
	
}
