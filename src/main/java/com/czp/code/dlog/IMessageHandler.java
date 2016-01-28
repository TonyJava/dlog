package com.czp.code.dlog;

/**
 * Function:XXX<br>
 *
 * Date :2016年1月24日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public interface IMessageHandler {
    
    /**
     * 处理日志信息
     * 
     * @param message
     */
    void onMessage(String message);
}
