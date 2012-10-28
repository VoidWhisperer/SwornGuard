package com.minesworn.swornguard.patrol.commands;

import com.minesworn.swornguard.PlayerInfo;
import com.minesworn.swornguard.SwornGuard;
import com.minesworn.swornguard.patrol.Patrol;

public class CmdAutoPatrol extends SPCommand {

	public CmdAutoPatrol() {
		this.name = "autopatrol";
		this.aliases.add("apat");
		this.mustBePlayer = true;
		this.description = "Teleports you to a player on the server continuously.";
		this.optionalArgs.add("interval");
	}
	
	@Override
	public void perform() {
		PlayerInfo i = SwornGuard.playerdatabase.getPlayer(player.getName());
		
		if (i.isAutoPatrolling()) {
			Patrol.unAutoPatrol(player);
			return;
		}
		
		if (i.isCheaterInspecting()) {
			errorMessage(SwornGuard.lang.getErrorMessage("alreadyinspecting"));
			return;
		}
		
		int interval = 20;
		
		if (args.length > 0)
			try {
				interval = Integer.parseInt(args[0]);
				if (interval < 5 || interval > 60)
					interval = 20;
			} catch (NumberFormatException e) {
				errorMessage(SwornGuard.lang.getErrorMessage("incorrectinterval"));
				return;
			}
		
		Patrol.autoPatrol(player, interval);
	}

}
