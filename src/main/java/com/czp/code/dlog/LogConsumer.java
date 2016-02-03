package com.czp.code.dlog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

/**
 * Function: 基础类
 * 
 * @author: jeff.cao@aoliday.com
 * @date: 2016年1月23日<br>
 *        https://gist.github.com/rmoff/c18fd2b4a734576d7289<br>
 *        http://kafka.apache.org/documentation.html#producerapi
 */
public class LogConsumer {

	protected ConsumerConnector consumer;

	protected String topics;

	public LogConsumer(String servers, String topics, String group) {
		this.topics = topics;
		Properties props = new Properties();
		props.put("group.id", group);
		props.put("zookeeper.connect", servers);
		props.put("zookeeper.sync.time.ms", "200");
		props.put("auto.commit.interval.ms", "1000");
		props.put("zookeeper.session.timeout.ms", "400");
		props.put("zookeeper.session.timeout.ms", "400");
		consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(
				props));
	}

	public void start() {
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(topics, 1);
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer
				.createMessageStreams(topicCountMap);
		KafkaStream<byte[], byte[]> stream = consumerMap.get(topics).get(0);
		ConsumerIterator<byte[], byte[]> it = stream.iterator();
		while (it.hasNext())
			System.out.println(it.next().message());
	}

	public void stop() {
		consumer.shutdown();
	}
}
