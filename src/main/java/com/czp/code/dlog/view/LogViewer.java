package com.czp.code.dlog.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.javaapi.consumer.ConsumerConnector;

/**
 * Function: 基础类
 * 
 * @author: jeff.cao@aoliday.com
 * @date: 2016年1月23日
 * https://gist.github.com/rmoff/c18fd2b4a734576d7289
 */
public class LogViewer {

	protected IMessageHandler handler;
	protected ConsumerConnector consumer;

	public LogViewer(String servers, String zkServes) {
		Properties props = new Properties();
		props.put("group.id", "loggroup");
		props.put("zookeeper.connect", zkServes);
		props.put("metadata.broker.list", servers);
		props.put("auto.offset.reset", "smallest");
		props.put("auto.commit.interval.ms", "1000");
		props.put("zookeeper.connectiontimeout.ms", "3000");
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(
				props));

	}

	public void setHandler(IMessageHandler handler) {
		this.handler = handler;
	}

	public void start() {
		String topic = "dlog";
		int threadEachTopic = 1;
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put(topic, threadEachTopic);
		ConsumerIterator<byte[], byte[]> it = consumer
				.createMessageStreams(map).get(topic).get(0).iterator();
		while (it.hasNext()) {
			try {
				handler.onMessage(it.next().message());
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void stop() {
		consumer.shutdown();
	}
}
