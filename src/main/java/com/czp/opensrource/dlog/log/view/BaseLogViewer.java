package com.czp.opensrource.dlog.log.view;

import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.javaapi.consumer.ConsumerConnector;

/**
 * Function: 基础类
 * 
 * @author: jeff.cao@aoliday.com
 * @date: 2016年1月23日
 * 
 */
public abstract class BaseLogViewer {

	protected ConsumerConnector consumer;

	protected String topic;

	public BaseLogViewer(String servers, String zkServes, String topic) {
		Properties props = new Properties();
		props.put("group.id", "group1");
		props.put("zookeeper.connect", zkServes);
		props.put("zookeeper.connect", zkServes);
		props.put("metadata.broker.list", servers);
		props.put("auto.commit.interval.ms", "1000");
		props.put("auto.offset.reset", "smallest");
		props.put("zookeeper.connectiontimeout.ms", "3000");
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(
				props));
		this.topic = topic;

	}

	public abstract void start();

	public abstract void stop();
}
