package com.czp.opensrource.dlog.log.view.ws;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

/**
 * Function:XXX<br>
 *
 * Date :2016年1月24日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public class LogSocket extends WebSocketAdapter {

	@Override
	public void onWebSocketText(String message) {
		System.out.println(message);
	}

	public void send(String message) throws IOException {
		getRemote().sendString(message);
	}

	@Override
	public void onWebSocketConnect(Session sess) {
		try {
			super.onWebSocketConnect(sess);
			sess.getRemote().sendString("thanks to use weblog");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
