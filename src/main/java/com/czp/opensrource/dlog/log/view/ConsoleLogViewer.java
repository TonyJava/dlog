package com.czp.opensrource.dlog.log.view;

import java.util.HashMap;
import java.util.Map;

import kafka.consumer.ConsumerIterator;

/**
 * Function: 控制台消费者
 * 
 * @author: jeff.cao@aoliday.com
 * @date: 2016年1月23日
 * 
 */
public class ConsoleLogViewer extends BaseLogViewer {
    
    public ConsoleLogViewer(String servers, String zkServes, String topic) {
        super(servers, zkServes, topic);
    }
    
    public void start() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put(topic, new Integer(1));
        ConsumerIterator<byte[], byte[]> it = consumer.createMessageStreams(map).get(topic).get(0).iterator();
        while (it.hasNext()) {
            System.out.println(new String(it.next().message()));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void stop() {
        consumer.shutdown();
    }
}
