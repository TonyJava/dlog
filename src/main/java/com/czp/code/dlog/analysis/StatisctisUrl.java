package com.czp.code.dlog.analysis;

import java.util.Map;

import com.czp.code.dlog.store.IDao;

/**
 * Function: 将Url分析结果存储到数据库
 * 
 * @author: jeff.cao@aoliday.com
 * @date: 2016年2月3日
 * 
 *        <pre>
 * CREATE TABLE if not exists log_url_st(
 *          `id` int(11) NOT NULL AUTO_INCREMENT,
 *          server VARCHAR(255), 
 *          url VARCHAR(255),
 *          start VARCHAR(255),
 *          avg_time int(11),
 *          call_methods int(11),
 *          PRIMARY KEY (id));
 * </pre>
 */
public class StatisctisUrl extends StaticsBase {
    
    public StatisctisUrl(IDao dao) {
        super(dao);
    }
    
    protected StringBuilder createSQL(Map<String, Object> map) {
        StringBuilder sql = new StringBuilder();
        sql.append("insert into  log_url_st values(null,'");
        sql.append(map.get("server")).append("','");
        sql.append(map.get("url")).append("','");
        sql.append(map.get("start")).append("',");
        sql.append(map.get("avg_time")).append(",");
        sql.append(map.get("call_methods")).append(");");
        return sql;
    }
    
    @Override
    public String statisticsSQL() {
        return "select count(method) as call_methods,avg(times) as avg_time,server,url,start from logs  group by url,start";
    }
    
}
