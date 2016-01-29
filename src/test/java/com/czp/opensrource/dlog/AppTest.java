package com.czp.opensrource.dlog;

import java.util.Scanner;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

import com.czp.code.dlog.LogConsumer;
import com.czp.code.dlog.view.WebViewer;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

	public AppTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	public void testWebView() {
		LogConsumer view = new LogConsumer("127.0.0.1:9092",
				new String[] { "log_main_INFO" }, "web");
		WebViewer handler = new WebViewer(8080);
		view.addHandler(handler);
		handler.start();
		view.start();
		view.stop();
	}

	public static void xtestSender() {
		Logger log = Logger.getLogger(AppTest.class);
		Scanner sc = new Scanner(System.in);
		String line = null;
		while (!(line = sc.nextLine()).equals("exit")) {
			for (int i = 0; i < 10000; i++) {
				log.info(line + i);
			}
		}
		sc.close();
	}

}
