package com.minesworn.swornguard.patrol.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.minesworn.swornguard.SwornGuard;

public class CmdVanishList extends SPCommand {

	public CmdVanishList() {
		this.name = "vanishlist";
		this.description = "Shows a list of all vanished players on the server.";
	}
	
	@Override
	public void perform() {
		StringBuilder ret = new StringBuilder();
		ret.append(ChatColor.AQUA + "Vanish list: " + ChatColor.YELLOW);
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (SwornGuard.playerdatabase.getPlayer(p.getName()).isVanished())
				ret.append(p.getName() + ", ");
		}
		if (ret.lastIndexOf(",") != -1)
			ret.deleteCharAt(ret.lastIndexOf(","));
		
		sender.sendMessage(ret.toString());
	}

}
