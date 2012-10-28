package com.minesworn.swornguard.patrol.commands;

import com.minesworn.swornguard.PlayerInfo;
import com.minesworn.swornguard.SwornGuard;
import com.minesworn.swornguard.patrol.Patrol;

public class CmdVanish extends SPCommand {

	public CmdVanish() {
		this.name = "vanish";
		this.aliases.add("unvanish");
		this.aliases.add("hide");
		this.aliases.add("unhide");
		this.mustBePlayer = true;
		this.description = "Vanishes you from other players.";
		this.optionalArgs.add("on/off");
	}
	
	@Override
	public void perform() {
		PlayerInfo i = SwornGuard.playerdatabase.getPlayer(player.getName());
		
		boolean vanish = !i.isVanished();
		
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("on"))
				vanish = true;
			else if (args[0].equalsIgnoreCase("off"))
				vanish = false;
		}
		
		Patrol.vanish(player, vanish);
	}

}
