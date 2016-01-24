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
			log.error(line);
		}
		sc.close();
	}

}
