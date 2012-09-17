package com.minesworn.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.massivecraft.factions.event.FPlayerJoinEvent;
import com.massivecraft.factions.event.FPlayerLeaveEvent;
import com.minesworn.PlayerDatabase;
import com.minesworn.PlayerInfo;
import com.minesworn.detectors.FactionBetrayalDetector;
import com.minesworn.detectors.factionbetrayal.Kick;

public class NationsListener implements Listener {

	@EventHandler
	public void onPlayerJoinFaction(FPlayerJoinEvent e) {
		PlayerInfo i = PlayerDatabase.getPlayer(e.getFPlayer().getName());
		i.setFactionCount(i.getFactionCount() + 1);
		i.setMostRecentFaction(e.getFaction().getTag());
	}
	
	@EventHandler
	public void onPlayerLeaveFaction(FPlayerLeaveEvent e) {
		if (e.getReason() == FPlayerLeaveEvent.PlayerLeaveReason.KICKED) {
			FactionBetrayalDetector.addPossibleBetrayedPlayer(e.getFPlayer().getPlayer(), 
					new Kick(e.getFaction().getTag(), System.currentTimeMillis()));
		}
	}
	
}
