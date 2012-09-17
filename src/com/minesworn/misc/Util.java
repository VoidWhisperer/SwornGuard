package com.minesworn.misc;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Util {

	public static Player matchPlayer(String p) {
		List<Player> players = Bukkit.matchPlayer(p);
		
		if (players.size() >= 1) {
			return players.get(0);
		}
		
		return null;
	}
	
	public static String formatTimeDifference(long t1, long t2) {
		return formatTime(getTimeDifference(t1, t2));
	}
	
	public static long getTimeDifference(long t1, long t2) {
		return (t2 - t1);
	}
	
	public static String formatTime(long time) {
		StringBuilder ret = new StringBuilder();
		int days = (int) Math.floor(time / (1000*3600*24));
		int hours = (int) Math.floor((time % (1000*3600*24)) / (1000*3600));
		int minutes = (int) Math.floor((time % (1000*3600*24)) % (1000*3600) / (1000*60));
		int seconds = (int) Math.floor(time % (1000*3600*24) % (1000*3600) % (1000*60) / (1000));
		
		if (days != 0)
			ret.append(days + "d");
		if (hours != 0 || days != 0)
			ret.append(hours + "h");
		if (minutes != 0 || hours != 0 || days != 0)
			ret.append(minutes + "m");
		ret.append(seconds + "s");
		
		return ret.toString();
	}
	
	public static String getLongDateCurr() {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm");
		Date date = new Date();
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		return dateFormat.format(date);
	}
	
	public static String getSimpleDate(long time) {
		DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
		Date date = new Date(time);
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		return dateFormat.format(date);
	}
	
	public static boolean isBanned(OfflinePlayer p) {
		for (OfflinePlayer banned : Bukkit.getBannedPlayers()) {
			if (p.getName().equalsIgnoreCase(banned.getName()))
				return true;
		}
		return false;
	}
	
	public static double roundTwoDecimals(double d) {
        DecimalFormat f = new DecimalFormat("#.##");
        return Double.valueOf(f.format(d));
	}
	
}
