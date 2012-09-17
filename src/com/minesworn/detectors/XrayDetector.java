package com.minesworn.detectors;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.minesworn.Cheat;
import com.minesworn.Config;
import com.minesworn.PlayerDatabase;
import com.minesworn.PlayerInfo;
import com.minesworn.SwornGuard;
import com.minesworn.events.CheatEvent;
import com.minesworn.misc.Util;

public class XrayDetector {

	public static void addBlock(Material mat, Player p) {
		PlayerInfo i = PlayerDatabase.getPlayer(p.getName());
		if (mat.equals(Material.IRON_ORE)) {
			i.setIronMined(i.getIronMined() + 1);
			if (getIronRatio(p) > Config.stoneToIronRatioBeforeWarning && i.getStoneMined() > 150 && 
					(System.currentTimeMillis() - i.getLastWarnedForXray() > 30000L)) {
				CheatEvent e = new CheatEvent(p, "[CHEATER] I think that" + p.getName() + " is xraying!", Cheat.XRAY);
				SwornGuard.announceCheat(e);
				i.setLastWarnedForXray(System.currentTimeMillis());
			}
		}
		if (mat.equals(Material.DIAMOND_ORE)) {
			i.setDiamondMined(i.getDiamondMined() + 1);
			if (getDiamondRatio(p) > Config.stoneToDiamondRatioBeforeWarning && i.getStoneMined() > 150 && 
					(System.currentTimeMillis() - i.getLastWarnedForXray() > 30000L)) {
				CheatEvent e = new CheatEvent(p, "[CHEATER] I think that" + p.getName() + " is xraying!", Cheat.XRAY);
				SwornGuard.announceCheat(e);
				i.setLastWarnedForXray(System.currentTimeMillis());
			}
		}
		if (mat.equals(Material.STONE))
			i.setStoneMined(i.getStoneMined() + 1);
	}
	
	public static double getDiamondRatio(OfflinePlayer p) {
		PlayerInfo i = PlayerDatabase.getPlayer(p.getName());
		double ratio = (double) i.getDiamondMined() / (i.getDiamondMined() + i.getStoneMined()) * 100;
		return (Util.roundTwoDecimals(ratio));
	}
	
	public static double getIronRatio(OfflinePlayer p) {
		PlayerInfo i = PlayerDatabase.getPlayer(p.getName());
		double ratio = (double) i.getIronMined() / (i.getIronMined() + i.getStoneMined()) * 100;
		return (Util.roundTwoDecimals(ratio));
	}
	
}
