package com.minesworn.listeners;

import me.t7seven7t.swornjail.events.JailEvent;
import me.t7seven7t.swornjail.events.UnjailEvent;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.minesworn.PlayerDatabase;
import com.minesworn.PlayerInfo;
import com.minesworn.misc.Util;

public class JailListener implements Listener {

	@EventHandler
	public void onJailEvent(JailEvent e) {
		PlayerInfo i = PlayerDatabase.getPlayer(e.getInmate().getName());
		i.setJailCount(i.getJailCount() + 1);
		i.setLastJailedBy(e.getJailer());
		i.setLastJailReason(e.getReason());
		i.setLastJailTime(System.currentTimeMillis());
		i.getProfilerList().add("[" + Util.getLongDateCurr() + " GMT]" + ChatColor.DARK_PURPLE + " Jailed by " + e.getJailer() + " for: " + e.getReason());
	}
	
	@EventHandler
	public void onUnjailEvent(UnjailEvent e) {
		PlayerInfo i = PlayerDatabase.getPlayer(e.getInmate().getName());
		i.getProfilerList().add("[" + Util.getLongDateCurr() + " GMT]" + ChatColor.DARK_PURPLE + " Unjailed by " + e.getJailer());
	}
	
}
