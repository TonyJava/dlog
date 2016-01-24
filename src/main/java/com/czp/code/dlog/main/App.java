package com.czp.code.dlog.main;

import com.czp.code.dlog.view.LogViewer;
import com.czp.code.dlog.view.web.WebViewer;

public class App {
    public static void main(String[] args) {
        String servers = "127.0.0.1:9092,127.0.0.1:9093,127.0.0.1:9094";
        LogViewer viewer = new WebViewer(2233, servers, "127.0.0.1:2181");
        viewer.start();
        viewer.stop();
    }
}
