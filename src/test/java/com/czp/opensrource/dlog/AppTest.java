package com.czp.opensrource.dlog;

import java.util.Scanner;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import com.czp.code.dlog.view.LogViewer;
import com.czp.code.dlog.view.WebViewer;

public class AppTest extends TestCase {
    
    public void testWebView() {
        LogViewer view = new LogViewer("127.0.0.1:9092", "127.0.0.1:2181","dlog");
        WebViewer handler = new WebViewer(8080);
        view.addHandler(handler);
        handler.start();
        view.start();
        view.stop();
    }
    
    public void testSender() {
        Logger log = Logger.getLogger(AppTest.class);
        Scanner sc = new Scanner(System.in);
        String line = null;
        while (!(line = sc.nextLine()).equals("exit")) {
            for (int i = 0; i < 100; i++) {
                log.error(line + i);
            }
        }
        sc.close();
    }
}
