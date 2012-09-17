package com.minesworn.detectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.minesworn.Cheat;
import com.minesworn.Permission;
import com.minesworn.PlayerDatabase;
import com.minesworn.PlayerInfo;
import com.minesworn.SwornGuard;
import com.minesworn.events.CheatEvent;

public class FlyDetector {

	public FlyDetector() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(SwornGuard.p, new Runnable() {

			@Override
			public void run() {
				step();
			}
			
		}, 20L, 10L);
	}
	
	private void step() {
		for (final Player p : Bukkit.getOnlinePlayers()) {
			if (isVeloStrange(p) && getDistToGround(p) > 6 
					&& !Permission.hasPermission(p, Permission.CAN_FLY) 
					&& !p.getAllowFlight()
					) {
				final PlayerInfo i = PlayerDatabase.getPlayer(p.getName());
				if (System.currentTimeMillis() - i.getLastWarnedForFlying() > 10000L) {
					Bukkit.getScheduler().scheduleSyncDelayedTask(SwornGuard.p, new Runnable() {
						
						@Override
						public void run() {
							i.setLastWarnedForFlying(System.currentTimeMillis());
							checkFlying(p, p.getLocation().toVector());
							SwornGuard.log("rawr!");
						}
						
					}, 10L);
				}
			}
		}
	}
	
	private void checkFlying(Player p, Vector prevL) {
		Vector l = p.getLocation().toVector();
		PlayerInfo i = PlayerDatabase.getPlayer(p.getName());
		
		if (!p.getLocation().equals(prevL) && l.getY() >= prevL.getY()) {
			CheatEvent e = new CheatEvent(p, "[CHEATER] I think that " + p.getName() + " is flying!", Cheat.FLYING);
			SwornGuard.announceCheat(e);
			i.setLastWarnedForFlying(System.currentTimeMillis());
		} else
			i.setLastWarnedForFlying(0);
	}
	
	private boolean isVeloStrange(Player p) {
		if (p.getVelocity().getY() < -2.5)
			return true;	
		return false;
	}
	
	private int getDistToGround(Player p) {
		Location loc = p.getLocation();
		Material m;
		int count = 0;
		while(loc.getBlockY() > 0 && ((m = loc.getWorld().getBlockAt(loc).getType()) == Material.AIR 
				|| m == Material.WATER)) {
			loc = loc.subtract(0, 1, 0);
			count++;
		}
		return count;
	}
	
}
