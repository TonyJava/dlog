package com.czp.opensrource.dlog;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

/**
 * Function:日志工厂<br>
 *
 * Date :2016年1月9日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public class DLoggerFactory implements ILoggerFactory {

	private DLogger dLogger = new DLogger();

	public DLoggerFactory() {
	}

	public Logger getLogger(String name) {
		return dLogger;
	}

}
