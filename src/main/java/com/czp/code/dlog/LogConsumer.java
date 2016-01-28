package com.czp.code.dlog;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * Function: 基础类
 * 
 * @author: jeff.cao@aoliday.com
 * @date: 2016年1月23日<br>
 *        https://gist.github.com/rmoff/c18fd2b4a734576d7289<br>
 *        http://kafka.apache.org/documentation.html#producerapi
 */
public class LogConsumer {
    
    protected CopyOnWriteArraySet<IMessageHandler> handlers = new CopyOnWriteArraySet<IMessageHandler>();
    
    protected Consumer<String, String> consumer;
    
    public LogConsumer(String servers, List<String> topic, String group) {
        Properties props = new Properties();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getCanonicalName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getCanonicalName());
        
        consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(topic);
    }
    
    public boolean addHandler(IMessageHandler handler) {
        return handlers.add(handler);
    }
    
    public void start() {
        while (!Thread.interrupted()) {
            ConsumerRecords<String, String> msgs = consumer.poll(100);
            for (ConsumerRecord<String, String> msg : msgs) {
                for (IMessageHandler handler : handlers) {
                    try {
                        handler.onMessage(msg.value());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    public void stop() {
        consumer.close();
    }
}
