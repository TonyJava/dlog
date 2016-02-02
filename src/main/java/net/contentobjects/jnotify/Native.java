package net.contentobjects.jnotify;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Function:JNI帮助类<br>
 *
 * Date :2016年2月1日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */

public class Native {
    
    private static boolean loaded = false;
    
    public enum OS {
        
        WINDOWS("win32", "dll"), LINUX("linux", "so"), MAC("darwin", "dylib"), SOLARIS("solaris", "so");
        
        public final String name, ext;
        
        private OS(String name, String ext) {
            this.name = name;
            this.ext = ext;
        }
    }
    
    public static synchronized OS load(String baseDir, String name) {
        OS os = os();
        if (loaded)
            return os;
        
        String url = resourceName(os, baseDir, name);
        InputStream is = Native.class.getResourceAsStream(url);
        if (is == null) {
            throw new UnsupportedOperationException("Unsupported OS/arch, cannot find " + url);
        }
        try {
            File tempLib = new File(name + "." + os.ext);
            if (!tempLib.exists()) {
                int read = -1;
                FileOutputStream out = new FileOutputStream(tempLib);
                byte[] buf = new byte[4096];
                while ((read = is.read(buf)) != -1) {
                    out.write(buf, 0, read);
                }
                out.close();
            }
            System.load(tempLib.getAbsolutePath());
            loaded = true;
            is.close();
            return os;
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Cannot unpack dll/so for java" + e);
        }
    }
    
    public static OS os() {
        String osName = System.getProperty("os.name");
        if (osName.contains("Linux")) {
            return OS.LINUX;
        }
        if (osName.contains("Mac")) {
            return OS.MAC;
        }
        if (osName.contains("Windows")) {
            return OS.WINDOWS;
        }
        if (osName.contains("Solaris")) {
            return OS.SOLARIS;
        }
        throw new UnsupportedOperationException("Unsupported os:" + osName);
    }
    
    private static String resourceName(OS os, String dir, String name) {
        String arch = System.getProperty("os.arch");
        return String.format("%s/%s/%s/lib%s.%s", dir, os.name, arch, name, os.ext).replaceAll("/{2,}", "/");
    }
}
