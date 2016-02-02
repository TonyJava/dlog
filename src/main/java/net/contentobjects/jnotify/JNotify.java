/*******************************************************************************
 * JNotify - Allow java applications to register to File system events.
 * 
 * Copyright (C) 2005 - Content Objects
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 ******************************************************************************
 * 
 * You may also redistribute and/or modify this library under the terms of the
 * Eclipse Public License. See epl.html.
 * 
 ******************************************************************************
 *
 * Content Objects, Inc., hereby disclaims all copyright interest in the
 * library `JNotify' (a Java library for file system events).
 * 
 * Yahali Sherman, 21 November 2005
 *    Content Objects, VP R&D.
 * 
 ******************************************************************************
 * Author : Omry Yadan
 ******************************************************************************/

package net.contentobjects.jnotify;

import net.contentobjects.jnotify.Native.OS;

public class JNotify {
    
    public static final int FILE_CREATED = 0x1;
    
    public static final int FILE_DELETED = 0x2;
    
    public static final int FILE_MODIFIED = 0x4;
    
    public static final int FILE_RENAMED = 0x8;
    
    public static final int FILE_ANY = FILE_CREATED | FILE_DELETED | FILE_MODIFIED | FILE_RENAMED;
    
    private static IJNotify _instance;
    
    static {
        OS os = Native.load("/", "jnotify");
        String clsName = null;
        String overrideClass = System.getProperty("jnotify.impl.override");
        if (overrideClass != null) {
            clsName = overrideClass;
        } else {
            switch (os) {
                case WINDOWS:
                    clsName = "net.contentobjects.jnotify.win32.JNotifyAdapterWin32";
                    break;
                case LINUX:
                case SOLARIS:
                    clsName = "net.contentobjects.jnotify.linux.JNotifyAdapterLinux";
                    break;
                case MAC:
                    clsName = "net.contentobjects.jnotify.macosx.JNotifyAdapterMacOSX";
                    break;
            }
        }
        try {
            _instance = (IJNotify)Class.forName(clsName).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
    }
    
    public static int addWatch(String path, int mask, boolean watchSubtree, JNotifyListener listener)
        throws JNotifyException {
        return _instance.addWatch(path, mask, watchSubtree, listener);
    }
    
    public static boolean removeWatch(int watchId)
        throws JNotifyException {
        return _instance.removeWatch(watchId);
    }
    
}
