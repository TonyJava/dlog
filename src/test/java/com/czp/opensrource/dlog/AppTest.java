package com.czp.opensrource.dlog;

import java.util.Scanner;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import com.czp.code.dlog.main.App;
import com.czp.code.dlog.view.ConsoleViewer;
import com.czp.code.dlog.view.LogViewer;

public class AppTest extends TestCase {
    
    public void testWebView() {
        App.main(new String[] {"127.0.0.1:9092", "127.0.0.1:2181", "dlog", "8080"});
    }
    public void testConsoleView() {
        LogViewer view = new LogViewer("127.0.0.1:9092", "127.0.0.1:2181", "log_main");
        ConsoleViewer handler = new ConsoleViewer();
        view.addHandler(handler);
        view.start();
        view.stop();
    }
    public void testSender() {
        Logger log = Logger.getLogger(AppTest.class);
        Scanner sc = new Scanner(System.in);
        String line = null;
        while (!(line = sc.nextLine()).equals("exit")) {
            for (int i = 0; i < 100000; i++) {
                log.info(line + i);
            }
        }
        sc.close();
    }
}
