package com.minesworn.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.minesworn.Permission;
import com.minesworn.lang.CommandError;

public abstract class SGCommand {

	public CommandSender sender;
	public Player player;
	public String[] args;
	public boolean isPlayer = false;
	public boolean mustBePlayer = false;
	
	public Permission permission;
	public String name;
	public String description;
	
	public List<String> requiredArgs = new ArrayList<String>(2);
	public List<String> optionalArgs = new ArrayList<String>(2);
	public List<String> aliases = new ArrayList<String>(2);
		
	public void perform() {}
	
	public void execute(CommandSender sender, String[] args) {
		this.sender = sender;
		this.args = args;
		if (sender instanceof Player) {
			this.player = (Player) sender;
			this.isPlayer = true;
		}
		
		if (mustBePlayer && !isPlayer) {
			error(CommandError.MUST_BE_PLAYER);
			return;
		}
		
		if (requiredArgs.size() > args.length) {
			error(CommandError.ARG_COUNT + getUsageTemplate(false));
			return;
		}
		
		if (hasPermission()) {
			perform();
		} else
			error(CommandError.PERMISSION);
			
	}
	
	public boolean hasPermission() {return (Permission.hasPermission(sender, permission)) ? true : false;}
	
	public void msg(String msg) {
		sender.sendMessage(msg);
	}
	
	public void error(String msg) {
		sender.sendMessage(ChatColor.RED + "Error: " + msg);
	}
	
	public void confirm(String msg) {
		sender.sendMessage(ChatColor.YELLOW + msg);
	}
	
	
	public String getUsageTemplate(boolean help) {
		StringBuilder ret = new StringBuilder();
		ret.append(ChatColor.RED);
		ret.append("/sg " + name);
		
		if (aliases.size() != 0) {
			ret.append(ChatColor.GOLD + "(");
			for (int i = 0; i < aliases.size(); i++) {
				ret.append(aliases.get(i) + ((i == aliases.size() - 1) ? "" : ", "));
			}
			ret.append(")");
		}
		
		ret.append(ChatColor.DARK_RED + " ");
		
		for (String s : requiredArgs) {
			ret.append("<" + s + "> ");
		}
		
		for (String s : optionalArgs) {
			ret.append("[" + s + "] ");
		}
		
		if (help) {
			ret.append(ChatColor.YELLOW + description);
		}
		
		return ret.toString();
	}
	
}
