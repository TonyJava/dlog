//package com.czp.code.dlog;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//import java.util.concurrent.CopyOnWriteArraySet;
//
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.apache.kafka.common.serialization.StringDeserializer;
//
//
///**
// * Function: 基础类
// * 
// * @author: jeff.cao@aoliday.com
// * @date: 2016年1月23日<br>
// *        https://gist.github.com/rmoff/c18fd2b4a734576d7289<br>
// *        http://kafka.apache.org/documentation.html#producerapi
// */
//public class LogDispatch {
//
//	protected CopyOnWriteArraySet<IMessageHandler> handlers = new CopyOnWriteArraySet<IMessageHandler>();
//
//	protected KafkaConsumer<String, String> consumer;
//
//	public LogDispatch(String servers, String[] topic, String group) {
//		String clsName = StringDeserializer.class.getCanonicalName();
//		Properties props = new Properties();
//		props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
//		props.put(ConsumerConfig.SESSION_TIMEOUT_MS, "30000");
//		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
//		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
//		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
//		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, clsName);
//		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, clsName);
//		props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY, "roundrobin");
//
//		consumer = new KafkaConsumer<String, String>(props);
//		consumer.subscribe(topic);
//	}
//
//	public boolean addHandler(IMessageHandler handler) {
//		return handlers.add(handler);
//	}
//
//	public void start() {
//		while (!Thread.interrupted()) {
//			Map<String, ConsumerRecords<String, String>> msgs = consumer
//					.poll(100);
//			for (ConsumerRecords<String, String> msgset : msgs.values()) {
//				List<ConsumerRecord<String, String>> records = msgset.records();
//				for (ConsumerRecord<String, String> msg : records) {
//					for (IMessageHandler handler : handlers) {
//						try {
//							handler.onMessage(msg.topic(), msg.value());
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			}
//		}
//	}
//
//	public void stop() {
//		consumer.close();
//	}
//}
