package com.czp.code.dlog.appender;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.producer.async.MissingConfigException;

import org.apache.log4j.AppenderSkeleton;
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
    
    private static final String serializer = "kafka.serializer.StringEncoder";
    
    private static final String topicPre = "log_";
    
    private Producer<String, String> producer;
    
    /** 所有的topic加前缀 :log_ ,便于后续订阅时过滤 */
    private String project;
    
    /** kafka服务列表 */
    private String servers;
    
    public void setProject(String project) {
        this.project = topicPre + project;
    }
    
    public void setServers(String servers) {
        this.servers = servers;
    }
    
    public void activateOptions() {
        Properties props = new Properties();
        if (servers == null)
            throw new MissingConfigException("servers must be specified by the Kafka log4j appender");
        if (project == null)
            throw new MissingConfigException("project must be specified by the Kafka log4j appender");
        
        props.put("compression.type", "true");
        props.put("serializer.class", serializer);
        props.put("reconnect.interval", "10000");
        props.put("metadata.broker.list", servers);
        props.put("reconnect.time.interval.ms", "10000");
        producer = new Producer<String, String>(new ProducerConfig(props));
        
        if (layout == null)
            layout = new PatternLayout(pattern);
        
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
            if (event.getLoggerName().startsWith("kafka."))
                return;
            String data = layout.format(event);
            String key = String.valueOf(event.timeStamp);
            producer.send(new KeyedMessage<String, String>(project, key, data));
        } catch (Throwable e) {
            LogLog.error("kafka send log  error:" + event.getMessage(), e);
        }
    }
}
