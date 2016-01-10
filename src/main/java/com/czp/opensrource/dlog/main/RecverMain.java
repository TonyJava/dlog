package com.czp.opensrource.dlog.main;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.czp.opensrource.dlog.api.Constants;

/**
 * Function:启动日志消费者<br>
 *
 * Date :2016年1月10日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public class RecverMain {

	public static void main(String[] args) {
		try {
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(
					"tcp://127.0.0.1:61616");
			Connection connection = factory.createConnection();
			Session session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			Topic topic = session.createTopic(Constants.CMD_TOPIC);
			MessageConsumer consumer = session.createConsumer(topic);
			consumer.setMessageListener(new MessageListener() {

				public void onMessage(Message arg0) {
					try {
						TextMessage msg = (TextMessage) arg0;
						System.out.println(msg.getText());
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});
			connection.start();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
