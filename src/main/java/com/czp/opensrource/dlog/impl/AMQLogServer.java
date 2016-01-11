package com.czp.opensrource.dlog.impl;

import java.net.URI;
import java.util.Properties;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;

import com.czp.opensrource.dlog.api.Server;

/**
 * Function:分布式日志系统的AMQ实现<br>
 *
 * Date :2016年1月10日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public class AMQLogServer implements Server {

	private BrokerService broker = new BrokerService();

	public void init(Properties cfg) throws Exception {
		String brokerName = cfg.getProperty(broker_name, "dlog");
		String port = cfg.getProperty(mq_port, "61616");
		TransportConnector connector = new TransportConnector();
		connector.setUri(new URI("tcp://0.0.0.0:" + port));
		broker.setBrokerName(brokerName);
		broker.setUseShutdownHook(true);
		broker.addConnector(connector);
	}

	public void start() throws Exception {
		broker.start();
	}

	public void stop() throws Exception {
		broker.stop();
	}
}
