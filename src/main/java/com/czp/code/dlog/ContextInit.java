package com.czp.code.dlog;

import java.util.Properties;

import com.czp.code.dlog.analysis.SimpleAlarmHandler;
import com.czp.code.dlog.analysis.StatisctisMethod;
import com.czp.code.dlog.analysis.StatisctisUrl;
import com.czp.code.dlog.analysis.StatisticsMsgHandler;


/**
 * Function: 初始化上下文
 * 
 * @author: jeff.cao@aoliday.com
 * @date: 2016年2月3日
 * 
 */

public class ContextInit {
    
    private LogDisptach consumer;
    
    private String cfgFile;
    
    private IDao dao;
    
    public String getCfgFile() {
        return cfgFile;
    }
    
    public void setCfgFile(String cfgFile) {
        this.cfgFile = cfgFile;
    }
    
    public void init()
        throws Exception {
        
        Properties cfg = null;//AppConfig.config;
        dao = new MysqlDao();
        dao.init(cfg);
        
        System.out.println("dao inited");
        String topics = cfg.getProperty("mq_topics");
        String zkServer = cfg.getProperty("zk_server");
        String groupId = cfg.getProperty("mq_groupId");
        consumer = new LogDisptach(zkServer, topics.split(","), groupId);
        StatisctisMethod statisctisMethod = new StatisctisMethod(dao);
        StatisticsMsgHandler msgHandler = new StatisticsMsgHandler();
        SimpleAlarmHandler alarmHandler = new SimpleAlarmHandler();
        StatisctisUrl statisctisUrl = new StatisctisUrl(dao);
        msgHandler.addHandler(statisctisMethod);
        msgHandler.addHandler(statisctisUrl);
        
        statisctisMethod.addAlarmHandler(alarmHandler);
        statisctisUrl.addAlarmHandler(alarmHandler);
        
        consumer.addHandler(msgHandler);
        consumer.run();
        System.out.println("consumer inited");
    }
    
    public void stop() {
        dao.shutdown();
        consumer.stop();
    }
}
