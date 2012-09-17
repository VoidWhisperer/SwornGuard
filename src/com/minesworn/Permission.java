package com.minesworn;

import org.bukkit.command.CommandSender;

public enum Permission {
	INFO("info"),
	INFO_SEE_OTHERS("info.seeothers"),
	CAN_BAN("canban"),
	CAN_FLY("canfly"),
	CAN_SPAM("canspam"),
	CAN_USE_BLOCKED_CMDS("canuseblockedcommands"),
	CAN_SEE_CHEAT_REPORTS("canseecheatreports"),
	CAN_CHECK_IPS("cancheckips"),
	BAN_INFO("baninfo"),
	SHOW("show"),
	HELP("help"), 
	NOTE("note"), 
	CAN_KICK("cankick"), 
	MOD_BOT("modbot"), 
	RATIO("ratio"), 
	LEGIT("legit");
	
	public final String node;
	
	Permission(final String node) {
		this.node = "swornguard." + node;
	}
	
	public static boolean hasPermission(CommandSender sender, Permission permission) {
		if (sender.hasPermission(permission.node) || sender.isOp()) {
			return true;
		}
		return false;
	}
	
}
