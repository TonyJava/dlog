package com.czp.code.dlog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Function:XXX<br>
 *
 * Date :2016年2月2日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public class MysqlDao implements IDao {
    
    private Connection conn;
    
    private Statement stmt;
    
    @Override
    public boolean add(String sql) {
        return del(sql);
    }
    
    @Override
    public boolean del(String sql) {
        try {
            return stmt.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public List<Map<String, Object>> query(String sql) {
        List<Map<String, Object>> res = new LinkedList<Map<String, Object>>();
        try {
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData mt = rs.getMetaData();
            int colcount = mt.getColumnCount();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<String, Object>(colcount);
                for (int i = 1; i <= colcount; i++) {
                    row.put(mt.getColumnName(i), rs.getObject(i));
                }
                res.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    
    @Override
    public void init(Properties args) {
        try {
            String charSet = args.getProperty("charset");
            String initSql = args.getProperty("init_sql");
            String dbName = args.getProperty("db");
            String mysql = args.getProperty("mysql");
            String user = args.getProperty("user");
            String pwd = args.getProperty("pwd");
            Class.forName("com.mysql.jdbc.Driver");
            String url =
                String.format("jdbc:mysql://%s/%s?useUnicode=true&characterEncoding=%s", mysql, dbName, charSet);
            conn = DriverManager.getConnection(url, user, pwd);
            stmt = conn.createStatement();
            String[] sqls = initSql.split("#");
            for (String string : sqls) {
                stmt.executeUpdate(string);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void shutdown() {
        try {
            if (conn == null)
                return;
            conn.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void batchAdd(String sql) {
        try {
            stmt.addBatch(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void flush() {
        try {
            stmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
