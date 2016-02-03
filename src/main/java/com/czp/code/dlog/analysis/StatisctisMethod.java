package com.czp.code.dlog.analysis;

import java.util.Map;

import com.czp.code.dlog.store.IDao;

/**
 * Function: 将分析结果存储到数据库
 * 
 * @author: jeff.cao@aoliday.com
 * @date: 2016年2月3日
 * 
 *        <pre>
 * CREATE TABLE if not exists logs(
 *          `id` int(11) NOT NULL AUTO_INCREMENT,
 *          server VARCHAR(255), 
 *          class VARCHAR(255),
 *          method VARCHAR(255),
 *          start VARCHAR(255),
 *          calls int(11),
 *          times int(11),
 *           PRIMARY KEY (id));
 * </pre>
 */
public class StatisctisMethod extends StaticsBase {
    
    public StatisctisMethod(IDao dao) {
        super(dao);
    }
    
    protected StringBuilder createSQL(Map<String, Object> map) {
        StringBuilder sql = new StringBuilder();
        sql.append("insert into logs values(null,'");
        sql.append(map.get("server")).append("','");
        sql.append(map.get("class")).append("','");
        sql.append(map.get("method")).append("','");
        sql.append(map.get("start")).append("',");
        sql.append(map.get("call_count")).append(",");
        sql.append(map.get("time_const")).append(");");
        return sql;
    }
    
    @Override
    public String statisticsSQL() {
        return "select count(id) as call_count,sum(times) as time_const,server,class,method,start from logs  group by method,start";
    }
    
}
