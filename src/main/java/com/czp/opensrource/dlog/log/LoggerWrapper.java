package com.czp.opensrource.dlog.log;

import org.slf4j.Logger;
import org.slf4j.Marker;

import com.czp.opensrource.dlog.api.LogProvider;
import com.czp.opensrource.dlog.impl.AMQLogSender;

/**
 * Function:日志包装器,在这里拦截日志发送到服务端<br>
 *
 * Date :2016年1月9日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public class LoggerWrapper implements Logger {

	private LogProvider sender = new AMQLogSender();

	public LoggerWrapper() {
		try {
			sender.init(System.getProperties());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getName() {
		return "ROOT";
	}

	public boolean isTraceEnabled() {
		return false;
	}

	public void trace(String paramString) {
		System.err.println(paramString);

	}

	public void trace(String paramString, Object paramObject) {

	}

	public void trace(String paramString, Object paramObject1,
			Object paramObject2) {

	}

	public void trace(String paramString, Object[] paramArrayOfObject) {

	}

	public void trace(String paramString, Throwable paramThrowable) {

	}

	public boolean isTraceEnabled(Marker paramMarker) {
		return false;
	}

	public void trace(Marker paramMarker, String paramString) {

	}

	public void trace(Marker paramMarker, String paramString, Object paramObject) {

	}

	public void trace(Marker paramMarker, String paramString,
			Object paramObject1, Object paramObject2) {

	}

	public void trace(Marker paramMarker, String paramString,
			Object[] paramArrayOfObject) {

	}

	public void trace(Marker paramMarker, String paramString,
			Throwable paramThrowable) {

	}

	public boolean isDebugEnabled() {

		return true;
	}

	public void debug(String paramString) {
	}

	public void debug(String paramString, Object paramObject) {

	}

	public void debug(String paramString, Object paramObject1,
			Object paramObject2) {

	}

	public void debug(String paramString, Object[] paramArrayOfObject) {

	}

	public void debug(String paramString, Throwable paramThrowable) {

	}

	public boolean isDebugEnabled(Marker paramMarker) {

		return false;
	}

	public void debug(Marker paramMarker, String paramString) {

	}

	public void debug(Marker paramMarker, String paramString, Object paramObject) {

	}

	public void debug(Marker paramMarker, String paramString,
			Object paramObject1, Object paramObject2) {

	}

	public void debug(Marker paramMarker, String paramString,
			Object[] paramArrayOfObject) {

	}

	public void debug(Marker paramMarker, String paramString,
			Throwable paramThrowable) {

	}

	public boolean isInfoEnabled() {

		return false;
	}

	public void info(String paramString) {
		try {
			sender.send(paramString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void info(String paramString, Object paramObject) {

	}

	public void info(String paramString, Object paramObject1,
			Object paramObject2) {

	}

	public void info(String paramString, Object[] paramArrayOfObject) {

	}

	public void info(String paramString, Throwable paramThrowable) {

	}

	public boolean isInfoEnabled(Marker paramMarker) {

		return false;
	}

	public void info(Marker paramMarker, String paramString) {

	}

	public void info(Marker paramMarker, String paramString, Object paramObject) {

	}

	public void info(Marker paramMarker, String paramString,
			Object paramObject1, Object paramObject2) {

	}

	public void info(Marker paramMarker, String paramString,
			Object[] paramArrayOfObject) {

	}

	public void info(Marker paramMarker, String paramString,
			Throwable paramThrowable) {

	}

	public boolean isWarnEnabled() {

		return false;
	}

	public void warn(String paramString) {

	}

	public void warn(String paramString, Object paramObject) {

	}

	public void warn(String paramString, Object[] paramArrayOfObject) {

	}

	public void warn(String paramString, Object paramObject1,
			Object paramObject2) {

	}

	public void warn(String paramString, Throwable paramThrowable) {

	}

	public boolean isWarnEnabled(Marker paramMarker) {

		return false;
	}

	public void warn(Marker paramMarker, String paramString) {

	}

	public void warn(Marker paramMarker, String paramString, Object paramObject) {

	}

	public void warn(Marker paramMarker, String paramString,
			Object paramObject1, Object paramObject2) {

	}

	public void warn(Marker paramMarker, String paramString,
			Object[] paramArrayOfObject) {

	}

	public void warn(Marker paramMarker, String paramString,
			Throwable paramThrowable) {

	}

	public boolean isErrorEnabled() {

		return false;
	}

	public void error(String paramString) {

	}

	public void error(String paramString, Object paramObject) {

	}

	public void error(String paramString, Object paramObject1,
			Object paramObject2) {

	}

	public void error(String paramString, Object[] paramArrayOfObject) {

	}

	public void error(String paramString, Throwable paramThrowable) {

	}

	public boolean isErrorEnabled(Marker paramMarker) {

		return false;
	}

	public void error(Marker paramMarker, String paramString) {

	}

	public void error(Marker paramMarker, String paramString, Object paramObject) {

	}

	public void error(Marker paramMarker, String paramString,
			Object paramObject1, Object paramObject2) {

	}

	public void error(Marker paramMarker, String paramString,
			Object[] paramArrayOfObject) {

	}

	public void error(Marker paramMarker, String paramString,
			Throwable paramThrowable) {

	}

}
