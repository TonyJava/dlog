package com.czp.code.dlog.monitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Function:处理文件修改事件<br>
 *
 * Date :2016年1月30日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public class FileProcess implements Runnable {
    
    private FileWraper fw;
    
    private BufferedReader bis;
    
    private IFileListener listener;
    
    private Logger logger = LoggerFactory.getLogger(FileProcess.class);
    
    public FileProcess(FileWraper fw, IFileListener listener, String charset) {
        try {
            this.fw = fw;
            this.listener = listener;
            InputStream is = new FileInputStream(fw.getFile());
            this.bis = new BufferedReader(new InputStreamReader(is, charset));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void run() {
        try {
            String line;
            File file = fw.getFile();
            logger.info("start read:{}", file);
            long pos = listener.beforeRead(file);
            if (pos > 0) {
                bis.skip(Math.min(pos, file.length()));
            }
            while (!Thread.interrupted()) {
                try {
                    while ((line = bis.readLine()) != null) {
                        listener.onChange(file, line);
                        pos += line.length();
                    }
                    fw.waitChange();
                } catch (InterruptedException e) {
                    logger.error("read file thread interrupted ", e);
                    Thread.interrupted();
                } catch (Exception e) {
                    listener.onError(file, e);
                }
            }
            bis.close();
            listener.onClose(file, pos);
            logger.info("end read::{}", file);
        } catch (Exception e) {
            logger.error("read file error", e);
        }
    }
    
}
