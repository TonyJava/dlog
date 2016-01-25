package com.czp.code.dlog.view;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Function: 守护进程timer
 * 
 * @author: jeff.cao@aoliday.com
 * @date: 2016年1月25日
 * 
 */
public class DaemonTimer implements Runnable {

	private static class TimerTask {

		Runnable task;
		int peroid;
		int times;

		TimerTask(Runnable task, int peroid) {
			this.task = task;
			this.peroid = peroid;
		}

	}

	private CopyOnWriteArraySet<TimerTask> tasks = new CopyOnWriteArraySet<TimerTask>();

	private static final DaemonTimer TIMER = new DaemonTimer();

	private Thread timerThread;

	private DaemonTimer() {
		timerThread = new Thread(this, "DaemonTimer");
		timerThread.setDaemon(true);
	}

	public static DaemonTimer getInstance() {
		return TIMER;
	}

	public void addTask(Runnable runnable, int peroid) {
		tasks.add(new TimerTask(runnable, peroid));
		if (!timerThread.isAlive())
			timerThread.start();
	}

	public void removeTask(Runnable runnable) {
		for (TimerTask item : tasks) {
			if (item.task.equals(runnable)) {
				tasks.remove(runnable);
				return;
			}
		}
	}

	@Override
	public void run() {
		int sleepTime = 300;
		while (!Thread.interrupted()) {
			try {
				for (TimerTask item : tasks) {
					if (item.times < item.peroid) {
						item.times += sleepTime;
						continue;
					}
					item.task.run();
					item.times = 0;
				}
				Thread.sleep(sleepTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
