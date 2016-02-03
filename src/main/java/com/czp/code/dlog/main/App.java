package com.czp.code.dlog.main;

import java.io.IOException;

import com.czp.code.dlog.analysis.StatisticsMsgHandler;

public class App {

	public static void main(String[] args) throws IOException {
//		if (args.length < 4) {
//			System.out.println("Usage:java  -jar  mqServer  topic webPort");
//			return;
//		}
//		String mqServer = args[0];
//		String topic = args[1];
//		int webPort = Integer.valueOf(args[2]);

		StatisticsMsgHandler hd = new StatisticsMsgHandler();
		hd.setZkser("127.0.0.1:2181");
		hd.init();
		System.in.read();
		hd.shutDown();
	}
}
