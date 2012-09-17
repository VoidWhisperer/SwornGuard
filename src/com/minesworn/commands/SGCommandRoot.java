package com.minesworn.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SGCommandRoot {
	
	public static Set<SGCommand> commands = new HashSet<SGCommand>();
	public static ArrayList<ArrayList<String>> helpPages = new ArrayList<ArrayList<String>>();
	
	public static CmdBanInfo CMD_BANINFO = new CmdBanInfo();
	public static CmdHelp CMD_HELP = new CmdHelp();
	public static CmdInfo CMD_INFO = new CmdInfo();
	public static CmdIP CMD_IP = new CmdIP();
	public static CmdLegit CMD_LEGIT = new CmdLegit();
	public static CmdModBot CMD_MODBOT = new CmdModBot();
	public static CmdNote CMD_NOTE = new CmdNote();
	public static CmdRatio CMD_RATIO = new CmdRatio();
	public static CmdShow CMD_SHOW = new CmdShow();
	
	static {
		commands.add(CMD_BANINFO);
		commands.add(CMD_HELP);
		commands.add(CMD_INFO);
		commands.add(CMD_IP);
		commands.add(CMD_LEGIT);
		commands.add(CMD_MODBOT);
		commands.add(CMD_NOTE);
		commands.add(CMD_RATIO);
		commands.add(CMD_SHOW);
	}

}
