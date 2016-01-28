package com.czp.code.dlog;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;


/**
 * Function: 基于kafka的Appender
 * 
 * @author: jeff.cao@aoliday.com
 * @date: 2016年1月23日 <br>
 *        usage:<br>
 *        # appender kafka <br>
 *        log4j.appender.KAFKA=com.aoliday.com.log.MQAppender <br>
 *        log4j.appender.KAFKA.servers=127.0.0.1:9092,127.0.0.1:9093<br>
 *        log4j.appender.KAFKA.layout=org.apache.log4j.PatternLayout<br>
 *        log4j.appender.KAFKA.topic=test log4j.appender.KAFKA.Threshold=INFO<br>
 *        log4j.appender.KAFKA.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %t %c{1}:%L-%m%n
 */
public class MQAppender extends AppenderSkeleton {
    
    private static final String pattern = "[%d{yyyy-MM-dd HH:mm:ss}] [%-5p] [%t] [%c{1}.%M:%L] [%m]%n";
    
    private static final String topicPre = "log";
    
    private KafkaProducer<String, String> producer;
    
    /** 所有的topic加前缀 :log_ ,便于后续订阅时过滤 */
    private String project;
    
    /** kafka服务列表 */
    private String servers;
    
    public void setProject(String project) {
        this.project = project;
    }
    
    public void setServers(String servers) {
        this.servers = servers;
    }
    
    public void activateOptions() {
        Properties props = new Properties();
        if (servers == null)
            throw new RuntimeException("servers must be specified by the Kafka log4j appender");
        if (project == null)
            throw new RuntimeException("project must be specified by the Kafka log4j appender");
        
        if (layout == null)
            layout = new PatternLayout(pattern);
        
        props.put("acks", "0");
        props.put("bootstrap.servers", servers);
        props.put("reconnect.backoff.ms", "3000");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<String, String>(props);
        LogLog.debug(String.format("Kafka producer connected to %s topic:%s", servers, project));
    }
    
    public void close() {
        if (this.closed)
            return;
        this.closed = true;
        producer.close();
    }
    
    public boolean requiresLayout() {
        return true;
    }
    
    @Override
    protected void append(LoggingEvent event) {
        try {
            Level level = event.getLevel();
            String data = layout.format(event);
            String topic = String.format("%s_%s_%s", topicPre, project, level);
            producer.send(new ProducerRecord<String, String>(topic, data));
        } catch (Throwable e) {
            LogLog.error("kafka send log  error:" + event.getMessage(), e);
        }
    }
}
