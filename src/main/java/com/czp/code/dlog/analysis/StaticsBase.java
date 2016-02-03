package com.czp.code.dlog.analysis;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.czp.code.dlog.store.IDao;


/**
 * Function: 基础类
 * 
 * @author: jeff.cao@aoliday.com
 * @date: 2016年2月3日
 * 
 */
public abstract class StaticsBase implements IResultHandler {
    
    private List<AlarmHandler> handlers = new LinkedList<AlarmHandler>();
    
    private IDao dao;
    
    public StaticsBase(IDao dao) {
        this.dao = dao;
    }
    
    public void addAlarmHandler(AlarmHandler handler) {
        handlers.add(handler);
    }
    
    public void testHasAlarm(Map<String, Object> row) {
        for (AlarmHandler alarmHandler : handlers) {
            if (alarmHandler.hasAlarm(row)) {
                alarmHandler.sendAlarm(row);
            }
        }
    }
    
    @Override
    public void onStatisticsResult(List<Map<String, Object>> result) {
        int size = 0;
        for (Map<String, Object> map : result) {
            StringBuilder sql = createSQL(map);
            dao.batchAdd(sql.toString());
            if (size++ > 100) {
                dao.flush();
                size = 0;
            }
        }
        dao.flush();
    }
    
    @Override
    public void shutDown() {
        dao.shutdown();
    }
    
    protected abstract StringBuilder createSQL(Map<String, Object> map);
}
