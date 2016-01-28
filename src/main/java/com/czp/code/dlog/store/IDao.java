package com.czp.code.dlog.store;

import java.util.List;

/**
 * Function: 分析结果存储
 * 
 * @author: jeff.cao@aoliday.com
 * @date: 2016年1月28日
 * 
 */
public interface IDao {
    
    boolean add(String sql);
    
    boolean del(String sql);
    
    List<String> query(String sql);
    
}
