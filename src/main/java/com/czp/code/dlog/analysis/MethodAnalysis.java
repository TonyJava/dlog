package com.czp.code.dlog.analysis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.czp.code.dlog.IMessageHandler;
import com.czp.code.dlog.store.IDao;


/**
 * Function: 统计分析方法调用频度
 * 
 * @author: jeff.cao@aoliday.com
 * @date: 2016年1月28日
 * 
 */
public class MethodAnalysis implements IMessageHandler {
    
    protected IDao dao;
    
    protected Pattern pattern = Pattern.compile("\\[([^\\]]*)\\]");
    
    @Override
    public void onMessage(String topic, String message) {
        Matcher matcher = pattern.matcher(message);
        System.out.println(matcher.matches());
    }
    
    public static void main(String[] args) {
        MethodAnalysis a = new MethodAnalysis();
        a.onMessage(null, "[2016-01-28 19:33:23] [INFO ] [main] [AppTest.xtestSender:46] [ddddddddddddd9980]");
    }
}
