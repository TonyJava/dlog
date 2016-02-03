package com.czp.code.dlog.analysis;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * Function: 按分钟统计调用次数
 * 
 * @author: jeff.cao@aoliday.com
 * @date: 2016年2月1日
 * 
 */
public class StatisticsMsgHandler extends AbstractHandler {

	public StatisticsMsgHandler() {
		this.statictisPeroid = 60 * 1000;
		this.statictisThreshold = 500000;
		this.topic="topic_dubbo_01";
		this.tableSql= "CREATE TABLE if not exists logs("
				+ " id int(11) NOT NULL AUTO_INCREMENT,"
				+ " server VARCHAR(255),class VARCHAR(255),"
				+ " method VARCHAR(255), start VARCHAR(255),"
				+ " calls int(11), times int(11), PRIMARY KEY (id));";
		this.statictisSQL="select count(id) as call_count,sum(times) as time_const,"
				+ "server,class,method,start from logs  group by method,start";
		
	}

	@Override
	public void processStaticResult(List<Map<String, Object>> query) {
       System.out.println(query);
	}

	@Override
	protected String parseMessage(int id, String topic, String message) {
		JSONObject json = (JSONObject) JSONObject.parse(message);
		String beginTime = json.getString("beginTime");
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
		return sql.toString();
	}

}
