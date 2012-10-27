package com.minesworn.swornguard.lang;

import com.minesworn.swornguard.core.util.SLang;

public class Lang extends SLang {
	static {
		messages.add("logreload: %s reloaded SwornJail.");
		messages.add("confirmreload: SwornJail reloaded!");
		
		errorMessages.add("playernotfound: That player was not found in the database.");
		errorMessages.add("incorrectpagesyntax: Incorrect page number format.");
		errorMessages.add("nosuchpage: There is no page with that index.");
	}	
}
