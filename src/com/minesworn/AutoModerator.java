package com.minesworn;

import me.t7seven7t.swornjail.Jail;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.minesworn.events.CheatEvent;

public class AutoModerator {

	public static void manageCheatEvent(CheatEvent e) {
		PlayerInfo i = PlayerDatabase.getPlayer(e.getPlayer().getName());
		String reason = new String();
		
		switch (e.getType()) {
		case FLYING:
			reason = Config.autoModeratorkickReasonFlying;
			break;
		case AUTO_ATTACK:
			reason = Config.autoModeratorkickReasonAutoAttack;
			break;
		case COMBAT_LOG:
			i.getProfilerList().add(e.getPlayer().getName() + " pinged the cheat detector for: " + ChatColor.GOLD + e.getType().toString());
			break;
		case KICK_AND_KILL:
			Jail.jail(e.getPlayer(), Config.autoModeratorFactionBetrayalJailTime, "Kick and kill players", "AutoModBot");
			break;
		case SPAM:
			reason = Config.autoModeratorkickReasonSpam;
			break;
		case XRAY:
			Jail.jail(e.getPlayer(), Config.autoModeratorXrayJailTime, "Xray", "AutoModBot");
			reason = Config.autoModeratorkickReasonXray;
			break;
		default:
			break;
		}
		
		if (reason != "") {
			i.setLastKick("AutoModBot", reason, System.currentTimeMillis());
			e.getPlayer().kickPlayer(reason);
			SwornGuard.log("Player " + e.getPlayer() + " was kicked by AutoModBot for: " + reason);
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (Permission.hasPermission(p, Permission.CAN_SEE_CHEAT_REPORTS))
					p.sendMessage(ChatColor.YELLOW + "AutoModBot kicked " + e.getPlayer().getName() + " for " + reason);
			}
		}
		
	}
	
	public static boolean isOnlyModeratorOnline() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (Permission.hasPermission(p, Permission.CAN_SEE_CHEAT_REPORTS))
				return Config.autoModeratorAlwaysEnabled;
		}
		return true;
	}
	
}
