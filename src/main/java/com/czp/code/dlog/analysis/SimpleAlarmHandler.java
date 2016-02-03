package com.czp.code.dlog.analysis;

import java.util.Map;

/**
 * Function: 检查和发送告警
 * 
 * @author: jeff.cao@aoliday.com
 * @date: 2016年2月3日
 * 
 */
public class SimpleAlarmHandler implements AlarmHandler {
    
    @Override
    public boolean hasAlarm(Map<String, Object> map) {
        Integer callMethods = (Integer)map.get("call_methods");
        Integer timeConst = (Integer)map.get("time_const");
        Integer callCount = (Integer)map.get("call_count");
        
        return (callMethods != null && callMethods > 200) || (timeConst != null && timeConst > 50000)
            || (callCount != null && callCount > 500);
    }
    
    @Override
    public void sendAlarm(Map<String, Object> map) {
        System.out.println("Send Alarm:" + map);
    }
    
}
