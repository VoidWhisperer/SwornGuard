package com.minesworn;

import java.util.ArrayList;
import com.minesworn.persist.Persist;

public class Config {
	
	public static double stoneToIronRatioBeforeWarning = 15.0D;
	public static double stoneToDiamondRatioBeforeWarning = 9.0D;
	
	public static long timeBetweenAttacksInMilliseconds = 500;
	
	public static int maxNumberOfEntitiesAllowedToDamagePerSecondBeforeWarning = 4;
	
	public static boolean mobHitsCauseCombatLog = false;
	public static boolean playerHitsCauseCombatLog = true;
	public static double combatLogWindowLastsForXSecondsAfterLastAttack = 3.0D;
	
	public static String autoModeratorkickReasonFlying = "Remove your fly hack.";
	public static String autoModeratorkickReasonSpam = "Do not spam.";
	public static String autoModeratorkickReasonAutoAttack = "Remove your auto attack hack.";
	public static String autoModeratorkickReasonXray = "Remove your xray hack/texturepack.";
	
	public static int autoModeratorXrayJailTime = 10;
	public static int autoModeratorFactionBetrayalJailTime = 10;
	public static boolean autoModeratorAlwaysEnabled = false;
	
	public static int spamThresholdBeforeReport = 5;
	public static ArrayList<String> blockedCommands = new ArrayList<String>();
		
	static {
		blockedCommands.add("op");
	}
	
	public static void save() {
		Persist.save(Config.class);
	}
	
	public static void load() {
		Persist.load(Config.class);
	}
	
}
