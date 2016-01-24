package com.czp.code.dlog.view;

/**
 * Function: 控制台消费者
 * 
 * @author: jeff.cao@aoliday.com
 * @date: 2016年1月23日
 * 
 */
public class ConsoleViewer extends LogViewer implements IMessageHandler {

	public ConsoleViewer(String servers, String zkServes) {
		super(servers, zkServes);
		setHandler(this);
	}

	@Override
	public void onMessage(byte[] message) {
		System.out.println(new String(message));
	}

}
