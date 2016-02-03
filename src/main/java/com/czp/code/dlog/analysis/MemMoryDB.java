package com.czp.code.dlog.analysis;

import java.io.File;
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

/**
 * Function: XXX
 * 
 * @author: jeff.cao@aoliday.com
 * @date: 2016年2月3日
 * 
 */
public class MemMoryDB {

	private Connection conn;

	private Statement stmt;

	private String dbName;

	public MemMoryDB(String dbName, String sql) {
		this.dbName = dbName;
		init(dbName, sql);
	}

	public boolean excuteDML(String sql) {
		try {
			return stmt.executeUpdate(sql) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<Map<String, Object>> query(String sql) {
		List<Map<String, Object>> res = new LinkedList<Map<String, Object>>();
		try {
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData mt = rs.getMetaData();
			int colcount = mt.getColumnCount();
			while (rs.next()) {
				Map<String, Object> row = new HashMap<String, Object>(colcount);
				for (int i = 1; i <= colcount; i++) {
					row.put(mt.getColumnName(i).toLowerCase(), rs.getObject(i));
				}
				res.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public String getDbName() {
		return dbName;
	}

	private void init(String dbName, String sql) {
		try {
			// String tableSql =
			// "CREATE TABLE if not exists logs(id int,server VARCHAR(255),requset_id VARCHAR(255),class VARCHAR(255),method VARCHAR(255),start VARCHAR(255),url VARCHAR(255),times int);";
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection("jdbc:h2:./" + dbName, "root",
					"log");
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void shutdown() {
		try {
			if (conn == null)
				return;
			conn.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void deletDBFile() {
		String[] dbFiles = { dbName + ".mv.db", dbName + ".trace.db" };
		for (String string : dbFiles) {
			File dbFile = new File(string);
			if (dbFile.exists())
				dbFile.delete();
		}
		System.out.println("delete db:" + dbName);
	}
}
