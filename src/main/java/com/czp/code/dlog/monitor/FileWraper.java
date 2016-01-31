package com.czp.code.dlog.monitor;

import java.io.File;

/**
 * Function:包装文件对象,记录修改时间<br>
 *
 * Date :2016年1月30日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public class FileWraper {
	private File file;
	private long modifyTime;
	private boolean contextClosed;

	public FileWraper(File file) {
		this.file = file;
		this.modifyTime = file.lastModified();
	}

	public File getFile() {
		return file;
	}

	public long getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(long modifyTime) {
		this.modifyTime = modifyTime;
	}

	public boolean isContextClosed() {
		return contextClosed;
	}

	public void setContextClosed(boolean contextClosed) {
		this.contextClosed = contextClosed;
	}

	public synchronized void waitChange() throws InterruptedException {
		if (contextClosed)
			return;
		this.wait();
	}

	public synchronized void fireChange() {
		this.notifyAll();
	}
}
