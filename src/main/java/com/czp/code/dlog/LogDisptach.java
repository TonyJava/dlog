//package com.czp.code.dlog;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//import java.util.concurrent.CopyOnWriteArraySet;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import kafka.consumer.Consumer;
//import kafka.consumer.ConsumerConfig;
//import kafka.consumer.ConsumerIterator;
//import kafka.consumer.KafkaStream;
//import kafka.javaapi.consumer.ConsumerConnector;
//import kafka.message.MessageAndMetadata;
//
///**
// * Function: 基础类
// * 
// * @author: jeff.cao@aoliday.com
// * @date: 2016年1月23日<br>
// *        https://gist.github.com/rmoff/c18fd2b4a734576d7289<br>
// *        http://kafka.apache.org/documentation.html#producerapi
// */
//public class LogDisptach implements Runnable {
//    
//    protected CopyOnWriteArraySet<IMessageHandler> handlers = new CopyOnWriteArraySet<IMessageHandler>();
//    
//    protected ConsumerConnector consumer;
//    
//    protected String[] topics;
//    
//    protected ExecutorService pool;
//    
//    public LogDisptach(String servers, String[] topics, String group) {
//        this.topics = topics;
//        Properties props = new Properties();
//        props.put("group.id", group);
//        props.put("zookeeper.connect", servers);
//        props.put("zookeeper.sync.time.ms", "200");
//        props.put("auto.commit.interval.ms", "1000");
//        props.put("zookeeper.session.timeout.ms", "400");
//        props.put("zookeeper.session.timeout.ms", "400");
//        pool = Executors.newFixedThreadPool(topics.length);
//        consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
//        System.out.println("connected:" + servers + ",topics:" + Arrays.toString(topics));
//    }
//    
//    public boolean addHandler(IMessageHandler handler) {
//        return handlers.add(handler);
//    }
//    
//    public void stop() {
//        consumer.shutdown();
//    }
//    
//    @Override
//    public void run() {
//        Map<String, Integer> topicMap = new HashMap<String, Integer>();
//        for (String topic : topics) {
//            topicMap.put(topic, 1);
//        }
//        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicMap);
//        for (final List<KafkaStream<byte[], byte[]>> its : consumerMap.values()) {
//            for (final KafkaStream<byte[], byte[]> st : its) {
//                pool.execute(new Runnable() {
//                    
//                    @Override
//                    public void run() {
//                        ConsumerIterator<byte[], byte[]> iter = st.iterator();
//                        while (iter.hasNext()) {
//                            MessageAndMetadata<byte[], byte[]> msg = iter.next();
//                            for (IMessageHandler handler : handlers) {
//                                try {
//                                    String topic = handler.getTopic();
//                                    if (topic == null || topic.matches(msg.topic())) {
//                                        handler.onMessage(msg.topic(), new String(msg.message()));
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                    }
//                });
//                
//            }
//        }
//    }
//}
