package com.czp.code.dlog;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.helpers.LogLog;

/**
 * Function: 日志发送类
 * 
 * @author: jeff.cao@aoliday.com
 * @date: 2016年1月29日
 * 
 */
public class LogProducer {
    
    /** kafka服务列表 */
    protected String servers;
    
    protected Properties props = new Properties();
    
    protected KafkaProducer<String, String> producer;
    
    public LogProducer(String servers) {
        this.servers = servers;
        props.put("acks", "0");
        props.put("bootstrap.servers", servers);
        props.put("reconnect.backoff.ms", "3000");
        props.put("metadata.fetch.timeout.ms", "1000");
        props.put("key.serializer", StringSerializer.class.getCanonicalName());
        props.put("value.serializer", StringSerializer.class.getCanonicalName());
        producer = new KafkaProducer<String, String>(props);
        LogLog.debug(String.format("Kafka producer connected to %s", servers));
    }
    
    /**
     * 异步发送消息
     * 
     * @param topic
     * @param message
     * @param key 
     */
    public void asynSend(String topic, String key,String message) {
        try {
            producer.send(new ProducerRecord<String, String>(topic, key,message));
        } catch (Exception e) {
            LogLog.error("kafka send log  error:" + message, e);
        }
    }
    
    public void close() {
        try {
            producer.close();
        } catch (Exception e) {
            LogLog.error("kafka close  error:", e);
        }
    }
}
