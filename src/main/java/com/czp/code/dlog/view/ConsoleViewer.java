package com.czp.code.dlog.view;

/**
 * Function: 控制台消费者
 * 
 * @author: jeff.cao@aoliday.com
 * @date: 2016年1月23日
 * 
 */
public class ConsoleViewer  implements IMessageHandler {

	@Override
	public void onMessage(byte[] message) {
		System.out.println(new String(message));
	}

}
