package com.czp.code.dlog.monitor;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Function:监控文件/目录修改<br>
 *
 * Date :2016年1月30日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public class FileMonitor implements Runnable, ThreadFactory {
    
    protected int peroid;
    
    protected String charset;
    
    protected IFileListener listener;
    
    protected ExecutorService service;
    
    protected Collection<String> paths;
    
    protected HashMap<File, FileWraper> allFiles;
    
    private Logger logger = LoggerFactory.getLogger(FileMonitor.class);
    
    public FileMonitor(Collection<String> paths, IFileListener listener, int peroid) {
        this(paths, "utf-8", listener, peroid);
    }
    
    public FileMonitor(Collection<String> paths, String charset, IFileListener listener, int peroid) {
        this.paths = paths;
        this.peroid = peroid;
        this.charset = charset;
        this.listener = listener;
        this.allFiles = new HashMap<File, FileWraper>();
        this.service = Executors.newCachedThreadPool(this);
        logger.info("FileMonitor start");
    }
    
    public void start() {
        for (String path : paths) {
            File root = new File(path);
            findAllFile(root);
            if (root.isDirectory()) {
                /* monitor if path is dir */
                allFiles.put(root, new FileWraper(root));
            }
        }
        DaemonTimer.getInstance().addTask(this, peroid);
    }
    
    @Override
    public void run() {
        Set<Entry<File, FileWraper>> entrySet = allFiles.entrySet();
        for (Entry<File, FileWraper> entry : entrySet) {
            FileWraper value = entry.getValue();
            long modify = value.getFile().lastModified();
            if (modify == 0) {
                /* file may be deleted */
                allFiles.remove(entry.getKey());
            } else if (value.getModifyTime() != modify) {
                value.setModifyTime(modify);
                value.fireChange();
            }
        }
    }
    
    public void stop() {
        service.shutdownNow();
        for (FileWraper fw : allFiles.values()) {
            fw.setContextClosed(true);
            fw.fireChange();
        }
        listener.onShutdown();
        logger.info("FileMonitor stop");
    }
    
    protected void findAllFile(File file) {
        if (file.isFile() && !allFiles.containsKey(file)) {
            doFileFound(file);
            return;
        }
        for (File tmp : file.listFiles()) {
            if (tmp.isDirectory()) {
                findAllFile(tmp);
            } else if (!allFiles.containsKey(tmp)) {
                doFileFound(tmp);
            }
        }
    }
    
    protected FileWraper doFileFound(File file) {
        FileWraper fw = new FileWraper(file);
        service.execute(new FileProcess(fw, listener, charset));
        allFiles.put(file, fw);
        return fw;
    }
    
    @Override
    public Thread newThread(Runnable task) {
        Thread th = new Thread(task);
        th.setName("File-monitor");
        th.setDaemon(true);
        return th;
    }
}
