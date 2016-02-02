package com.czp.code.dlog.store;

import java.util.List;
import java.util.Map;

/**
 * Function: 分析结果存储
 * 
 * @author: jeff.cao@aoliday.com
 * @date: 2016年1月28日
 * 
 */
public interface IDao {

	void init();

	void shutdown();

	boolean add(String sql);

	void batchAdd(String sql);

	void flush();

	boolean del(String sql);

	List<Map<String,Object>> query(String sql);

}
