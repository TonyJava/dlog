package com.czp.code.dlog.api;

import java.util.Properties;

/**
 * Function:服务接口<br>
 *
 * Date :2016年1月10日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public interface Server extends Constants {

	/**
	 * 初始化MQ服务
	 * @param cfg
	 * @throws Exception
	 */
	void init(Properties cfg) throws Exception;

	void start() throws Exception;

	void stop() throws Exception;
}
