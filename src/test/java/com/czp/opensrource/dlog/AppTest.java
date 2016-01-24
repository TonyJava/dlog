package com.czp.opensrource.dlog;

import java.util.Scanner;

import org.apache.log4j.Logger;

import com.czp.opensrource.dlog.log.view.ws.WebLogViewer;

import junit.framework.TestCase;

public class AppTest extends TestCase {

	public static void main(String[] args) {
		WebLogViewer view = new WebLogViewer(2233, "127.0.0.1:9092", "127.0.0.1:2181", "test");
		view.start();
		
	}

	public static void sendLog() {
		Logger log = Logger.getLogger(AppTest.class);
		Scanner sc = new Scanner(System.in);
		String line = null;
		while (!(line = sc.nextLine()).equals("exit")) {
			log.info(line);
		}
		sc.close();
	}

}
