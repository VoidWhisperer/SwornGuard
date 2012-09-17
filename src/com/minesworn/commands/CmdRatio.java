package com.minesworn.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import com.minesworn.Permission;
import com.minesworn.PlayerDatabase;
import com.minesworn.PlayerInfo;
import com.minesworn.detectors.XrayDetector;
import com.minesworn.lang.CommandError;
import com.minesworn.misc.Util;

public class CmdRatio extends SGCommand {

	public CmdRatio() {
		this.name = "ratio";
		this.description = "Checks a player's mining ratio.";
		this.permission = Permission.RATIO;
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
		
		confirm("Mining statistics for: " + ChatColor.GREEN + target.getName());
		if (i.getDiamondMined() == 0)
			confirm("Hasn't mined any diamonds.");
		else
			confirm("Diamond ratio: " + XrayDetector.getDiamondRatio(target) + "(%)");
		if (i.getIronMined() == 0)
			confirm("Hasn't mined any iron ore.");
		else
			confirm("Iron ratio: " + XrayDetector.getIronRatio(target) + "(%)");
	}
	
}
