package com.minesworn.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.minesworn.PlayerDatabase;
import com.minesworn.PlayerInfo;
import com.minesworn.detectors.CombatLogDetector;

public class PlayerJoinQuitListener implements Listener {
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		PlayerInfo i = PlayerDatabase.getPlayer(e.getPlayer().getName());
		long now = System.currentTimeMillis();
		if (i.getFirstOnlineTime() == 0) {
			i.setFirstOnlineTime(now);
		}
		
		i.setLoginCount(i.getLoginCount() + 1);
		String ip = e.getPlayer().getAddress().getAddress().getHostAddress();
		i.setIpAddress(ip);
		if (!i.getIpAddressList().contains(ip))
			i.getIpAddressList().add(ip);
		i.setLastOnlineTime(now);
		i.setOnlineNow(true);
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent e) {
		playerDisconnected(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayerKickEvent(PlayerKickEvent e) {
		playerDisconnected(e.getPlayer());
	}
	
	public void playerDisconnected(Player p) {
		CombatLogDetector.check(p);
		PlayerInfo i = PlayerDatabase.getPlayer(p.getName());
		long now = System.currentTimeMillis();
		i.updateSpentTime();
		i.setLastOnlineTime(now);
		i.setOnlineNow(false);
	}
	
}
