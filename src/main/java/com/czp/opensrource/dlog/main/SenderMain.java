package com.czp.opensrource.dlog.main;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Function:启动日志发送端<br>
 *
 * Date :2016年1月10日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public class SenderMain {

	public static void main(String[] args) {
		Logger log = LoggerFactory.getLogger(SenderMain.class);
		Scanner sc = new Scanner(System.in);
		String line = null;
		while (!(line = sc.nextLine()).equals("exit")) {
			log.info(line);
		}
		sc.close();
		System.exit(1);
	}
}

