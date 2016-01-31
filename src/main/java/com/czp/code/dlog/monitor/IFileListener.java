package com.czp.code.dlog.monitor;

import java.io.File;

/**
 * Function:处理文件变化事件<br>
 *
 * Date :2016年1月31日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public interface IFileListener {

	/**
	 * 如果返回大于0,则跳到Math.min(pos,file.length)开始读取文件
	 * 
	 * @param file
	 * @return
	 */
	long beforeRead(File file);

	void onChange(File file, String line);

	void onError(File file, Throwable e);

	/**
	 * curPostion:当前已经读取的位置,可以记录下来以便后续继续从这里读取
	 * 
	 * @param file
	 * @param curPostion
	 */
	void onClose(File file, long curPostion);
	
	void onShutdown();
}
