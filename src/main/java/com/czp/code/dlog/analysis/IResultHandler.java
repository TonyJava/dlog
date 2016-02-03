package com.czp.code.dlog.analysis;

import java.util.List;
import java.util.Map;

/**
 * Function: 处理统计结果：存储数据库,发送告警等
 * 
 * @author: jeff.cao@aoliday.com
 * @date: 2016年2月3日
 * 
 */
public interface IResultHandler {
    
    void onStatisticsResult(List<Map<String, Object>> result);
    
    String statisticsSQL();
    
    void shutDown();
}
