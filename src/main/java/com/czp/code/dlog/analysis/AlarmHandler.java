package com.czp.code.dlog.analysis;

import java.util.Map;

/**
 * Function: 告警策略
 * 
 * @author: jeff.cao@aoliday.com
 * @date: 2016年2月3日
 * 
 */
public interface AlarmHandler {
    
    /**
     * 当前统计是否需要发送告警
     * 
     * @param map
     * @return
     */
    public boolean hasAlarm(Map<String, Object> map);
    
    /**
     * 发送告警
     * @param map
     */
    public void sendAlarm(Map<String, Object> map);
}
