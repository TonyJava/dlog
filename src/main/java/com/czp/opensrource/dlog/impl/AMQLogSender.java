package com.czp.opensrource.dlog.impl;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.czp.opensrource.dlog.api.Constants;
import com.czp.opensrource.dlog.api.LogProvider;

/**
 * Function:通过AMQ的Producer发送日志<br>
 *
 * Date :2016年1月10日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public class AMQLogSender implements LogProvider {

	ConnectionFactory connFactry;
	Destination destination;
	MessageProducer producer;
	Connection connection;
	Session session;

	public void send(String log) throws Exception {
		producer.send(session.createTextMessage(log));
	}

	public void init(Properties cfg) throws Exception {
		connFactry = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
		connection = connFactry.createConnection();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		destination = session.createTopic(Constants.CMD_TOPIC);
		producer = session.createProducer(destination);
		producer.setDeliveryMode(DeliveryMode.PERSISTENT);
		connection.start();

	}

	public void start() throws Exception {
	}

	public void stop() throws Exception {
		session.commit();
		session.close();
		connection.close();
	}

}
