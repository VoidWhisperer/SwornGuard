package com.minesworn.threads;

import org.bukkit.Bukkit;

import com.minesworn.PlayerDatabase;
import com.minesworn.SwornGuard;

public class SaveRunnable implements Runnable {
	Thread t;

	public SaveRunnable() {
		t = new Thread(this);
		t.start();
	}
	
	@Override
	public void run() {
		try {
			while (SwornGuard.p.isEnabled()) {
				Thread.sleep(600000);
				Bukkit.getScheduler().scheduleSyncDelayedTask(SwornGuard.p, new Runnable() {

					@Override
					public void run() {
						PlayerDatabase.saveDB();
					}
					
				});
			}
		} catch (InterruptedException e) {}
	}

}
