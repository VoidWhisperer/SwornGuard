package com.minesworn.swornguard.core.util;

public abstract class SThread implements Runnable {
	Thread t;
	public SThread() {
		t = new Thread(this);
		t.start();
	}
}
