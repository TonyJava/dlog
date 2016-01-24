package com.czp.opensrource.dlog.log.appender;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.producer.async.MissingConfigException;

import org.apache.log4j.AppenderSkeleton;
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
 *        log4j.appender.KAFKA.servers=127.0.0.1:9092,127.0.0.1:9093 log4j.appender.KAFKA.topic=test
 *        log4j.appender.KAFKA.Threshold=INFO log4j.appender.KAFKA.layout=org.apache.log4j.PatternLayout
 *        log4j.appender.KAFKA.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L-%m%n
 */
public class MQAppender extends AppenderSkeleton {
    
    private String serializer = "kafka.serializer.StringEncoder";
    
    private Producer<String, String> producer;
    
    private String servers;
    
    private String topic;
    
    public void setTopic(String topic) {
        this.topic = topic;
    }
    
    public void setServers(String servers) {
        this.servers = servers;
    }
    
    public void activateOptions() {
        Properties props = new Properties();
        if (servers == null)
            throw new MissingConfigException("servers must be specified by the Kafka log4j appender");
        if (topic == null)
            throw new MissingConfigException("topic must be specified by the Kafka log4j appender");
        
        props.put("compression.type", "true");
        props.put("serializer.class", serializer);
        props.put("metadata.broker.list", servers);
        producer = new Producer<String, String>(new ProducerConfig(props));
        
        LogLog.debug(String.format("Kafka producer connected to %s topic:%s", servers, topic));
    }
    
    public String subAppend(LoggingEvent event) {
        return ((this.layout == null) ? event.getRenderedMessage() : this.layout.format(event));
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
            if (event.getLoggerName().startsWith("kafka"))
                return;
            String data = subAppend(event);
            String key = String.valueOf(event.timeStamp);
          //  producer.send(new KeyedMessage<String, String>(topic, key, data));
        } catch (Throwable e) {
            LogLog.error("kafka send log message error:" + event.getMessage(), e);
        }
    }
}
