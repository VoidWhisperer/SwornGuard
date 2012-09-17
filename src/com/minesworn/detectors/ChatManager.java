package com.minesworn.detectors;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import com.minesworn.Cheat;
import com.minesworn.Config;
import com.minesworn.PlayerDatabase;
import com.minesworn.PlayerInfo;
import com.minesworn.SwornGuard;
import com.minesworn.detectors.chatmanager.Message;
import com.minesworn.detectors.chatmanager.Messages;
import com.minesworn.events.CheatEvent;
import com.minesworn.misc.Util;

public class ChatManager {

	public static ConcurrentHashMap<String, Messages> players = new ConcurrentHashMap<String, Messages>();
	
	public static boolean isSpam(final String p, String msg, final String type) {
		boolean isCancelled = false;
		
		Messages messages = players.get(p);
		long now = System.currentTimeMillis();
		
		if (messages == null) {
			messages = new Messages();
			players.put(p, messages);
		}
		
		for (Message m : messages.getMessages()) {
			if (now - m.getTimestamp() > 1000L) {
				messages.removeMessage(m);
			}
		}
		
		if (messages.getMessages().length > Config.spamThresholdBeforeReport) {
			isCancelled = true;
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(SwornGuard.p, new Runnable() {
				public void run() {
					if (System.currentTimeMillis() - PlayerDatabase.getPlayer(p).getLastSpamReport() > 2000L) {
						CheatEvent e = new CheatEvent(Bukkit.getPlayer(p), "[SPAMMER] " + p + " is trying to spam " + type + "!", Cheat.SPAM);
						SwornGuard.announceCheat(e);
					}
				}
			});
			
		}
		
		if (compareMessages(msg, messages))
			isCancelled = true;
		
		messages.addMessage(msg, now);
		
		return isCancelled;
	}
	
	public static boolean compareMessages(String msg, Messages messages) {
		msg = msg.toLowerCase();
		
		for (Message m : messages.getMessages()) {
			String s = m.getMessage().toLowerCase();
			if (msg.length() <= 2 && s.length() <= 2)
				return true;
			
			if (msg.matches(s))
				return true;
			
			if (s.length() >= 2)
				if (msg.startsWith(s.substring(0, 2)))
					return true;
			
			if (msg.length() >= 3 && s.length() >= 3) {
				if (msg.length() >= 6 && s.length() >= 6) {
					for (int i = 0; i < 3; i++) {
						if (compareStrings(msg, s, i))
							return true;
					}
				}
				else
					if (compareStrings(msg, s, 2))
						return true;
			}
			
		}
		
		return false;
	}
	
	public static boolean compareStrings(String a, String b, int offset) {
		int sublen = (b.length() / 3) * offset;
		String s = b.substring(sublen, sublen + (b.length() / 3)); // causes divide by zero, needs fix; nvm fixed
		
		if (a.matches(".*" + s + ".*"))
			return true;
		
		return false;
	}	
	
	public static void checkCommand(CommandSender p, String cmd, String s) {
		int x = 0;
		if (cmd.equals("ban") || cmd.equals("eban"))
			x = 1;
		else if (cmd.equals("unban") || cmd.equals("eunban"))
			x = 2;
		else if (cmd.equals("kick"))
			x = 3;
		
		if (x > 0) {
			String[] ss = s.split(" ");
			StringBuilder ln = new StringBuilder();
			StringBuilder reason = new StringBuilder();
			if (ss.length >= 2) {
				OfflinePlayer target = Util.matchPlayer(ss[1]);
				if (target == null)
					target = Bukkit.getOfflinePlayer(ss[1]);
				
				PlayerInfo i = PlayerDatabase.getPlayer(target.getName(), false);
				if (i == null)
					return;
				ln.append("[" + Util.getLongDateCurr() + " GMT] " + ChatColor.AQUA + target.getName() + " was " + ((x > 2) ? "kicked" : (x > 1) ? "unbanned" : "banned") + " by " + p.getName());
			
				if (ss.length >= 3) {
					ln.append(" for: ");
					for (int y = 2; y < ss.length; y++) {
						reason.append(ss[y] + " ");
					}
					ln.append(reason.toString());
				}
				
				i.getProfilerList().add(ln.toString());

				PlayerInfo i2;
				
				switch (x) {
				case 1:
					i.setBanCount(i.getBanCount() + 1);
					i.setLastBannedBy(p.getName());
					i.setLastBanReason(reason.toString());
					i.setLastBanTime(System.currentTimeMillis());
					i2 = PlayerDatabase.getPlayer(p.getName());
					i2.setNumPlayersBanned(i2.getNumPlayersBanned() + 1);
					break;
				case 2:
					i.setLastUnbannedBy(p.getName());
					i.setLastUnbanTime(System.currentTimeMillis());
					break;
				case 3:
					i.setLastKick(p.getName(), reason.toString(), System.currentTimeMillis());
					i2 = PlayerDatabase.getPlayer(p.getName());
					i2.setNumPlayersKicked(i2.getNumPlayersKicked() + 1);
					break;
				}
			}
		}
	}
	
}
