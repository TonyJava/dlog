//package org.slf4j.impl;
//
//import org.slf4j.ILoggerFactory;
//import org.slf4j.spi.LoggerFactoryBinder;
//
//import com.czp.opensrource.dlog.log.LoggerFactory;
//
///**
// * Function:实现SLF4J API<br>
// *
// * Date :2016年1月9日 <br>
// * Author :coder_czp@126.com<br>
// * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
// * http://www.cnblogs.com/xing901022/p/4149524.html
// */
//public class StaticLoggerBinder implements LoggerFactoryBinder {
//
//	public static String REQUESTED_API_VERSION = "1.6";
//	private final ILoggerFactory loggerFactory = new LoggerFactory();
//	private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();
//	private static final String logFactoryClsStr = LoggerFactory.class.getName();
//	
//	public static final StaticLoggerBinder getSingleton() {
//		return SINGLETON;
//	}
//	
//	public ILoggerFactory getLoggerFactory() {
//		return loggerFactory;
//	}
//
//	public String getLoggerFactoryClassStr() {
//		return logFactoryClsStr;
//	}
//
//}
