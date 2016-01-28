package com.czp.code.dlog.view;

import com.czp.code.dlog.IMessageHandler;


/**
 * Function: 控制台消费者
 * 
 * @author: jeff.cao@aoliday.com
 * @date: 2016年1月23日
 * 
 */
public class ConsoleViewer  implements IMessageHandler {

	@Override
	public void onMessage(String message) {
		System.out.println(message);
	}

}
