package com.minesworn.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.minesworn.PlayerDatabase;
import com.minesworn.PlayerInfo;
import com.minesworn.detectors.XrayDetector;

public class BlockListener implements Listener {

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		PlayerInfo i = PlayerDatabase.getPlayer(e.getPlayer().getName());
		i.setBlockBuildCount(i.getBlockBuildCount() + 1);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		PlayerInfo i = PlayerDatabase.getPlayer(e.getPlayer().getName());
		i.setBlockDeleteCount(i.getBlockDeleteCount() + 1);
		XrayDetector.addBlock(e.getBlock().getType(), e.getPlayer());
	}
}
