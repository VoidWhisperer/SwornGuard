package com.minesworn;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;

import com.minesworn.persist.Persist;

public class PlayerDatabase {

	static boolean savingDB = false;
	public static HashMap<String, PlayerInfo> players = new HashMap<String, PlayerInfo>();
	public static String dir = "players/";
	
	public static PlayerInfo getPlayer(String name) {
		return getPlayer(name, true);
	}
	
	public static PlayerInfo getPlayer(String name, boolean forceCreate) {
		PlayerInfo i = players.get(name);
		
		if (i == null) {
			if (playerFileExists(name) || forceCreate) {
				i = new PlayerInfo();
				Persist.load(i, PlayerInfo.class, dir + name.toLowerCase());
				players.put(name, i);
			}
		}
		
		return i;
	}
	
	public static boolean playerFileExists(String name) {
		File f = new File(SwornGuard.p.getDataFolder() + "/" + dir + name + ".yml");
		if (f.exists())
			return true;
		return false;		
	}
	
	public static void saveDB() {
		savingDB = true;
		for (Entry<String, PlayerInfo> entry : players.entrySet()) {
			Persist.save(entry.getValue(), PlayerInfo.class, dir + entry.getKey());
		}
		
		SwornGuard.log("Player database successfully saved!");
		savingDB = false;
		
		cleanupDB();
	}
	
	public static void cleanupDB() {
		if (!savingDB) {
			for (Entry<String, PlayerInfo> entry : players.entrySet()) {
				if (!entry.getValue().isOnlineNow() && 
						(System.currentTimeMillis() - entry.getValue().getLastOnlineTime()) > (1000*3600*3)) {
					players.remove(entry.getKey());
				}
			}
		} else
			Bukkit.getScheduler().scheduleSyncDelayedTask(SwornGuard.p, new Runnable() {
				@Override
				public void run() {
					cleanupDB();
				}
			}, 20L);
	}
	
}
