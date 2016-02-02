package com.czp.code.dlog.main;


import com.czp.code.dlog.LogDispatch;

public class App {
    
    public static void main(String[] args) {
        if (args.length < 4) {
            System.out.println("Usage:java  -jar  mqServer  topic webPort");
            return;
        }
        String mqServer = args[0];
        String topic = args[1];
        int webPort = Integer.valueOf(args[2]);
        
        LogDispatch view = new LogDispatch(mqServer, new String[]{topic}, "webclient");
        view.start();
        view.stop();
        
    }
}
