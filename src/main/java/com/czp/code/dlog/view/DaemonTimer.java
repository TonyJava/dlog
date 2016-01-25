package com.czp.code.dlog.view;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Function: XXX
 * 
 * @author: jeff.cao@aoliday.com
 * @date: 2016年1月25日
 * 
 */
public class DaemonTimer implements Runnable {
    
    private CopyOnWriteArraySet<Runnable> tasks = new CopyOnWriteArraySet<Runnable>();
    
    private static final DaemonTimer TIMER = new DaemonTimer();
    
    private Thread timerThread;
    
    private DaemonTimer() {
        timerThread = new Thread(this, "DaemonTimer");
        timerThread.setDaemon(true);
    }
    
    public static DaemonTimer getInstance() {
        return TIMER;
    }
    
    public void addTask(Runnable runnable) {
        tasks.add(runnable);
        if (!timerThread.isAlive())
            timerThread.start();
    }
    
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                for (Runnable runnable : tasks) {
                    runnable.run();
                }
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.interrupted();
                e.printStackTrace();
            }
        }
    }
}
