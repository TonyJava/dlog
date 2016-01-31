package com.czp.code.dlog.monitor;

import java.io.File;

/**
 * Function:监控器空实现<br>
 *
 * Date :2016年1月31日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public class AbstractFileListener implements IFileListener {

	@Override
	public void onChange(File file, String line) {

	}

	@Override
	public void onError(File file, Throwable e) {
		e.printStackTrace();
	}

	@Override
	public long beforeRead(File file) {
		return 0;
	}

	@Override
	public void onClose(File file, long curReadPostion) {

	}

	@Override
	public void onShutdown() {
		
	}

}
