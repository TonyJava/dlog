package com.czp.opensrource.dlog.main;

import com.czp.opensrource.dlog.log.view.BaseLogViewer;
import com.czp.opensrource.dlog.log.view.ws.WebLogViewer;

public class App {
    public static void main(String[] args) {
        String servers = "127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094";
        // BaseLogViewer viewer = new ConsoleLogViewer(servers, "127.0.0.1:2181", "test");
        BaseLogViewer viewer = new WebLogViewer(8555, servers, "127.0.0.1:2181", "test");
        viewer.start();
        viewer.stop();
    }
}
