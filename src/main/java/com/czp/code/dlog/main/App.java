package com.czp.code.dlog.main;

import com.czp.code.dlog.view.LogViewer;
import com.czp.code.dlog.view.WebViewer;

public class App {
    
    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Usage:java  -jar  mqServer zkServer topic webPort");
            return;
        }
        String mqServer = args[0];
        String zkServer = args[1];
        String topic = args[2];
        int webPort = Integer.valueOf(args[3]);
        
        LogViewer view = new LogViewer(mqServer, zkServer, topic);
        WebViewer handler = new WebViewer(webPort);
        view.addHandler(handler);
        handler.start();
        view.start();
        view.stop();
        
    }
}
