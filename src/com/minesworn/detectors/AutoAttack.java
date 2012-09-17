package com.minesworn.detectors;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.minesworn.Cheat;
import com.minesworn.Config;
import com.minesworn.PlayerDatabase;
import com.minesworn.PlayerInfo;
import com.minesworn.SwornGuard;
import com.minesworn.events.CheatEvent;

public class AutoAttack {

	public static void check(Player p, Entity e) {
		ArrayList<Long> cleanup = new ArrayList<Long>();
		boolean add = false;
		long now = System.currentTimeMillis();
		PlayerInfo i = PlayerDatabase.getPlayer(p.getName());
		for (Entry<Long, Entity> entry : i.getRecentAttacks().entrySet()) {
			if (now - entry.getKey() < 1000L) {
				if (!e.equals(entry.getValue()))
					add = true;
			} else
				cleanup.add(entry.getKey());
		}
		if (cleanup.size() != 0)
			cleanup(i, cleanup);
		if (add)
			addEntity(i, e, now);
		
		if (i.getRecentAttacks().size() >= Config.maxNumberOfEntitiesAllowedToDamagePerSecondBeforeWarning) {
			CheatEvent ce = new CheatEvent(p, "[CHEATER] I think that" + p.getName() + " has AutoAttack!", Cheat.AUTO_ATTACK);
			SwornGuard.announceCheat(ce);
		}
	}
	
	public static void addEntity(PlayerInfo i, Entity e, long now) {
		i.getRecentAttacks().put(now, e);
	}
	
	public static void cleanup(PlayerInfo i, ArrayList<Long> cleanup) {
		for (long l : cleanup) {
			i.getRecentAttacks().remove(l);
		}
	}
	
}
