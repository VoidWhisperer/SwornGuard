package com.minesworn.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;

import com.minesworn.Permission;

public class CmdHelp extends SGCommand {

	public CmdHelp() {
		this.name = "help";
		this.aliases.add("h");
		this.description = "Shows SwornGuard help.";
		this.permission = Permission.HELP;
	}
	
	@Override
	public void perform() {
		for (String s : getPageLines()) {
			confirm(s);
		}
	}
	
	public ArrayList<String> getPageLines() {
		ArrayList<String> pageLines = new ArrayList<String>();
		pageLines.add(ChatColor.GOLD + "SwornJail Help:");
		pageLines.add(SGCommandRoot.CMD_HELP.getUsageTemplate(true));
		
		for (SGCommand command : SGCommandRoot.commands) {
			if (Permission.hasPermission(player, command.permission))
				pageLines.add(command.getUsageTemplate(true));
		}
		
		return pageLines;
	}
	
}
