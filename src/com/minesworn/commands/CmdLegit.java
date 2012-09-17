package com.minesworn.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import com.minesworn.Permission;
import com.minesworn.PlayerDatabase;
import com.minesworn.PlayerInfo;
import com.minesworn.lang.CommandError;
import com.minesworn.misc.Util;

public class CmdLegit extends SGCommand {

	public CmdLegit() {
		this.name = "legit";
		this.description = "Clears a player's mined ore count.";
		this.permission = Permission.LEGIT;
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
		
		i.setStoneMined(0);
		i.setIronMined(0);
		i.setDiamondMined(0);
		
		confirm(ChatColor.GREEN + target.getName() + ChatColor.YELLOW + "'s mining stats have been cleared.");
	}
	
}
