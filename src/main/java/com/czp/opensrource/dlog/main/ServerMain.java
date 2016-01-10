package com.czp.opensrource.dlog.main;

import java.util.Properties;

import com.czp.opensrource.dlog.api.LogServer;
import com.czp.opensrource.dlog.impl.AMQLogServer;

/**
 * Function:启动MQ服务<br>
 *
 * Date :2016年1月10日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public class ServerMain {

	public static void main(String[] args) throws Exception {
		LogServer server = new AMQLogServer();
		Properties cfg = new Properties();
		server.init(cfg);
		server.start();
	}
}
