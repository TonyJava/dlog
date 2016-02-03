package com.czp.code.dlog.analysis;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.fastjson.JSONObject;
import com.czp.code.dlog.IMessageHandler;

/**
 * Function: 按分钟统计调用次数
 * 
 * @author: jeff.cao@aoliday.com
 * @date: 2016年2月1日
 * 
 */
public class StatisticsMsgHandler implements IMessageHandler, Runnable {
    
    private MemMoryDB dao;
    
    private AtomicInteger count;
    
    private AtomicLong startTime;
    
    private long peroid = 60 * 1000;
    
    private int eachDbSize = 500000;
    
    /** 当前需要汇总的内存数据库名称 */
    private BlockingQueue<String> dbNames = new LinkedBlockingQueue<String>();
    
    /** 结果处理器,按sql分类:结果存储/告警通知等 */
    private HashMap<String, List<IResultHandler>> handlers = new HashMap<String, List<IResultHandler>>();
    
    public StatisticsMsgHandler() {
        String dbName = String.valueOf(System.nanoTime());
        this.startTime = new AtomicLong(System.currentTimeMillis());
        this.count = new AtomicInteger();
        this.dao = new MemMoryDB(dbName);
        
        Thread th = new Thread(this, "count-thread");
        th.setDaemon(true);
        th.start();
    }
    
    public synchronized void addHandler(IResultHandler handler) {
        List<IResultHandler> hds = handlers.get(handler.statisticsSQL());
        if (hds == null) {
            hds = new LinkedList<IResultHandler>();
            handlers.put(handler.statisticsSQL(), hds);
        }
        hds.add(handler);
    }
    
    @Override
    public void onMessage(String topic, String message) {
        JSONObject json = (JSONObject)JSONObject.parse(message);
        String beginTime = json.getString("beginTime");
        int id = count.getAndIncrement();
        
        beginTime = beginTime.substring(0, beginTime.length() - 3);
        
        StringBuilder sql = new StringBuilder("insert into logs values(");
        sql.append(id).append(",'");
        sql.append(json.getString("serverId")).append("','");
        sql.append(json.getString("requestId")).append("','");
        sql.append(json.getString("serviceName")).append("','");
        sql.append(json.getString("methodName")).append("','");
        sql.append(beginTime).append("','");
        sql.append(json.getString("url")).append("',");
        sql.append(json.getIntValue("costTime")).append(");");
        
        long now = System.currentTimeMillis();
        if (id >= eachDbSize || (now - startTime.get()) >= peroid) {
            synchronized (this) {
                this.dbNames.add(dao.getDbName());
                this.startTime.set(now);
                this.dao.shutdown();
                this.count.set(0);
                String dbName = String.valueOf(System.nanoTime());
                this.dao = new MemMoryDB(dbName);
            }
        }
        dao.excuteDML(sql.toString());
    }
    
    @Override
    public String getTopic() {
        return "topic_dubbo_01";
    }
    
    @Override
    public void run() {
        MemMoryDB query;
        String finishDb;
        while (!Thread.interrupted()) {
            try {
                finishDb = dbNames.take();
                query = new MemMoryDB(finishDb);
                for (Entry<String, List<IResultHandler>> entry : handlers.entrySet()) {
                    List<Map<String, Object>> result = query.query(entry.getKey());
                    for (IResultHandler hd : entry.getValue()) {
                        hd.onStatisticsResult(result);
                    }
                }
                query.shutdown();
                query.deletDBFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void shutDown() {
        dao.shutdown();
    }
    
}
