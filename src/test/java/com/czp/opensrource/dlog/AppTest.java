package com.czp.opensrource.dlog;

import java.io.File;
import java.util.Scanner;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

import com.czp.code.dlog.LogConsumer;
import com.czp.code.dlog.LogProducer;
import com.czp.code.dlog.monitor.FileMonitor;
import com.czp.code.dlog.monitor.IFileListener;
import com.czp.code.dlog.monitor.SimpleFileListener;
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
				new String[] { "log_main_INFO" }, "webp");
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

	public static void main(String[] args) throws Exception {
		final LogProducer producer = new LogProducer("127.0.0.1:9092");
		IFileListener l = new SimpleFileListener(){

			@Override
			public void onChange(File file, String line) {
				producer.send("log_main_INFO", line+"\n");
				System.out.println(line);
			}
			
			
		};
		FileMonitor fm = new FileMonitor("G:\\tomcat\\logs","gbk",l, 100);
		fm.start();
		System.in.read();
		fm.stop();
		producer.close();
	}
}
