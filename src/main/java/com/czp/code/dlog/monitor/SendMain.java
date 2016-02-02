package com.czp.code.dlog.monitor;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.czp.code.dlog.LogProducer;


/**
 * Function: 启动类
 * 
 * @author: jeff.cao@aoliday.com
 * @date: 2016年1月29日
 * 
 */
public class SendMain {
    
    private static Logger log = LoggerFactory.getLogger(SendMain.class);
    
    public static void main(String[] args)
        throws Exception {
        if (args.length < 1) {
            System.out.println("Usage:  config_file_path");
            return;
        }
        Properties cfg = new Properties();
        Reader reader = new FileReader(args[0]);
        cfg.load(reader);
        reader.close();
        
        String server = cfg.getProperty("kafka_server");
        String charsert = cfg.getProperty("read_charset");
        String topicQueue = cfg.getProperty("topics_queue");
        
        StringBuilder topics = new StringBuilder();
        Collection<String> dirs = new LinkedList<String>();
        final ConcurrentHashMap<String, String> topicMap = parseTopics(cfg, topics, dirs);
        System.out.println("log monitor start:" + cfg);
        log.info("log monitor start {}", cfg.toString());
        final LogProducer producer = new LogProducer(server);
        // 发送所有的topic到指定的队列
        producer.asynSend(topicQueue, "", topics.toString());
        
        IFileListener lis = new SimpleFileListener() {
            
            @Override
            public void onChange(File file, String line) {
                if (line.length() > 0) {
                    String path = file.getAbsolutePath();
                    String topic = getFileTopic(topicMap, file, path);
                    producer.asynSend(topic, "", line);
                }
            }
            
            private String getFileTopic(final Map<String, String> topicMap, File file, String path) {
                String topic = topicMap.get(path);
                if (topic == null) {
                    for (Entry<String, String> item : topicMap.entrySet()) {
                        if (path.startsWith(item.getKey())) {
                            topic = item.getValue();
                            topicMap.put(path, topic);
                            break;
                        }
                    }
                    if (topic == null) {
                        System.out.println("no config mathc,use file name as topic");
                        log.info("no config mathc,use file name as topic {}", file);
                        topicMap.put(path, file.getName());
                    }
                }
                return topic;
            }
        };
        
        JNotifyMonitor fm = new JNotifyMonitor(dirs, charsert, lis);
        fm.start();
        waitQuit();
        fm.stop();
        producer.close();
    }
    
    private static ConcurrentHashMap<String, String> parseTopics(Properties cfg, StringBuilder topics,
        Collection<String> dirs) {
        ConcurrentHashMap<String, String> topicMap = new ConcurrentHashMap<String, String>();
        for (Object object : cfg.keySet()) {
            String str = String.valueOf(object);
            if (str.startsWith("topic_")) {
                topicMap.put(cfg.getProperty(str), str);
                topics.append(str).append(",");
                dirs.add(cfg.getProperty(str));
            }
        }
        return topicMap;
    }
    
    private static void waitQuit() {
        System.out.println("Enter exit to quit");
        Scanner sc = new Scanner(System.in);
        while (!(sc.nextLine()).equals("exit")) {
            System.out.println("Enter exit to quit");
        }
        sc.close();
    }
}
