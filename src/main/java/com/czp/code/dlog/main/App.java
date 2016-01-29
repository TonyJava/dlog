package com.czp.code.dlog.main;


import com.czp.code.dlog.LogConsumer;
import com.czp.code.dlog.view.WebViewer;

public class App {
    
    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Usage:java  -jar  mqServer  topic webPort");
            return;
        }
        String mqServer = args[0];
        String topic = args[1];
        int webPort = Integer.valueOf(args[2]);
        
        LogConsumer view = new LogConsumer(mqServer, new String[]{topic}, "webclient");
        WebViewer handler = new WebViewer(webPort);
        view.addHandler(handler);
        handler.start();
        view.start();
        view.stop();
        
    }
}
