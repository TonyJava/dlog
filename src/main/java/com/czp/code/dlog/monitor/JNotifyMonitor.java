package com.czp.code.dlog.monitor;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;
import net.contentobjects.jnotify.JNotifyListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Function: XXX
 * 
 * @author: jeff.cao@aoliday.com
 * @date: 2016年2月1日
 * 
 */
public class JNotifyMonitor extends FileMonitor implements JNotifyListener {
    
    private HashMap<String, FileWraper> map = new HashMap<String, FileWraper>();
    
    private Logger logger = LoggerFactory.getLogger(JNotifyMonitor.class);
    
    private List<Integer> watchIDs;
    
    public JNotifyMonitor(Collection<String> paths, String charset, IFileListener listener) {
        super(paths, charset, listener, 100);
        watchIDs = new LinkedList<Integer>();
    }
    
    @Override
    public void start() {
        try {
            boolean watchSubtree = true;
            for (String path : paths) {
                findAllFile(new File(path));
                watchIDs.add(JNotify.addWatch(path, JNotify.FILE_ANY, watchSubtree, this));
            }
        } catch (JNotifyException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void fileRenamed(int wd, String rootPath, String oldName, String newName) {
        logger.info("file renamed:{} old:{} new:{} ", oldName, newName);
    }
    
    public void fileModified(int wd, String rootPath, String name) {
        try {
            String path = rootPath.concat(File.separator).concat(name);
            FileWraper fw = map.get(path);
            if (fw == null) {
                fw = doFileFound(new File(path));
                map.put(path, fw);
            } else {
                long lastModified = fw.getFile().lastModified();
                if (fw.getModifyTime() != lastModified) {
                    fw.setModifyTime(lastModified);
                    fw.fireChange();
                }
            }
            logger.debug("file modified:{} {}", rootPath, name);
        } catch (Exception e) {
            logger.error("fileModified error", e);
        }
    }
    
    public void fileDeleted(int wd, String rootPath, String name) {
        String path = rootPath.concat(File.separator).concat(name);
        FileWraper fw = map.get(path);
        if (fw != null) {
            fw.fireChange();
            fw.setContextClosed(true);
        }
        logger.error("file deleted:{}/{} ", rootPath, name);
    }
    
    public void fileCreated(int wd, String rootPath, String name) {
        String path = rootPath.concat(File.separator).concat(name);
        FileWraper fw = map.get(path);
        if (fw == null) {
            fw = doFileFound(new File(path));
            map.put(path, fw);
        }
        logger.info("file fileCreated:{}/{} ", rootPath, name);
    }
    
    @Override
    public void stop() {
        try {
            super.stop();
            for (Integer wid : watchIDs) {
                if (!JNotify.removeWatch(wid)) {
                    logger.error("fial to removeWatch:{}", wid);
                }
            }
        } catch (Exception e) {
            logger.error("stop error", e);
        }
    }
    
}
