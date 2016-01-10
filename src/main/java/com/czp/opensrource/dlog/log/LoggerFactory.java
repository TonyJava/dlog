package com.czp.opensrource.dlog.log;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

/**
 * Function:日志工厂<br>
 *
 * Date :2016年1月9日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public class LoggerFactory implements ILoggerFactory {

	private LoggerWrapper dLogger = new LoggerWrapper();

	public LoggerFactory() {
	}

	public Logger getLogger(String name) {
		return dLogger;
	}

}
