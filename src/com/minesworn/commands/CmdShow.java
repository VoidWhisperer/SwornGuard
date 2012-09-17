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

public class CmdShow extends SGCommand {
	
	public CmdShow() {
		this.name = "show";
		this.aliases.add("s");
		this.description = "Checks a player's history.";
		this.permission = Permission.SHOW;
		this.optionalArgs.add("player");
		this.optionalArgs.add("page");
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
		
		int page = 1;
		
		if (args.length == 2) {
			try {
				page = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				error(CommandError.INCORRECT_PAGE_SYNTAX);
				return;
			}
		}
		
		PlayerInfo i = PlayerDatabase.getPlayer(target.getName(), false);
		if (i == null) {
			error(CommandError.PLAYER_NOT_FOUND);
			return;
		}
		
		ArrayList<String> profilerList = i.getProfilerList();
		double plength = profilerList.size();		
		int totalPages = (int) Math.ceil(plength / 10);
		
		if (page > totalPages) {
			error(CommandError.NO_SUCH_PAGE);
			return;
		}
		
		ArrayList<String> lines = new ArrayList<String>();
		
		StringBuilder ln = new StringBuilder();
		ln.append("History for " + ChatColor.GREEN + target.getName());
		lines.add(ln.toString());
		
		for (int x = ((page * 10) - 10); x < (page * 10); x++) {
			if (x < profilerList.size())
				lines.add(profilerList.get(x));
		}
		
		ln = new StringBuilder();
		ln.append(ChatColor.RED + "Page " + page + "/" + totalPages);
		lines.add(ln.toString());
		
		for (String s : lines) {
			confirm(s);
		}
	}
	
}
