package com.czp.code.dlog.api;

/**
 * Function:日志发送器<br>
 *
 * Date :2016年1月10日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public interface LogProvider extends Server {

	void send(String log) throws Exception;
}
