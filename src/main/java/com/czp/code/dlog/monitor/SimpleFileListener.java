package com.czp.code.dlog.monitor;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * Function:记录上次读取的文件<br>
 *
 * Date :2016年1月31日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public class SimpleFileListener extends AbstractFileListener {

	private String dbPath = "./read.properties";
	private Properties db = new Properties();

	public SimpleFileListener() {
		try {
			if (new File(dbPath).exists()) {
				FileReader is = new FileReader(dbPath);
				db.load(is);
				is.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public long beforeRead(File file) {
		String pos = db.getProperty(file.getAbsolutePath(), "0");
		return Long.valueOf(pos);
	}

	@Override
	public void onClose(File file, long curReadPostion) {
		db.put(file.getAbsolutePath(), String.valueOf(curReadPostion));
	}

	@Override
	public void onShutdown() {
		try {
			FileWriter writer = new FileWriter(dbPath);
			db.store(writer, "don't edit this file");
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
