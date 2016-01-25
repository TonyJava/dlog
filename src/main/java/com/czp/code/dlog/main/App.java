package com.czp.code.dlog.main;

import com.czp.code.dlog.view.LogViewer;
import com.czp.code.dlog.view.WebViewer;

public class App {
    public static void main(String[] args) {
        LogViewer view = new LogViewer("127.0.0.1:9092", "127.0.0.1:2181", "dlog");
        WebViewer handler = new WebViewer(8080);
        view.addHandler(handler);
        handler.start();
        view.start();
        view.stop();
    }
}
