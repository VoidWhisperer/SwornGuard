package com.minesworn.detectors;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.minesworn.Config;

public class AutoClicker {

	public static HashMap<Player, Long> lastClick = new HashMap<Player, Long>();
	
	public static boolean check(Player p) {
		long now = System.currentTimeMillis();
		
		if ((lastClick.containsKey(p)) && (now - ((Long)lastClick.get(p)).longValue() < Config.timeBetweenAttacksInMilliseconds))
			return true;
		
		lastClick.remove(p);
		lastClick.put(p, Long.valueOf(System.currentTimeMillis()));

		return false;
	}
	
	public static boolean isHoldingWeapon(Player p) {
		ItemStack m = p.getItemInHand();
		if (m == null)
			return true;
		return (!(m.getType() == Material.DIAMOND_HOE || m.getType() == Material.IRON_HOE 
				|| m.getType() == Material.STONE_HOE || m.getType() == Material.WOOD_HOE));
	}
	
}
