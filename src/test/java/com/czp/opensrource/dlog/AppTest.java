package com.czp.opensrource.dlog;

import java.util.Scanner;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

public class AppTest extends TestCase {

	public static void main(String[] args) {
		sendLog();
	}

	public static void sendLog() {
		Logger log = Logger.getLogger(AppTest.class);
		Scanner sc = new Scanner(System.in);
		String line = null;
		while (!(line = sc.nextLine()).equals("exit")) {
			for (int i = 0; i < 1000; i++) {
				log.error(line+i);
			}
		}
		sc.close();
	}

}
