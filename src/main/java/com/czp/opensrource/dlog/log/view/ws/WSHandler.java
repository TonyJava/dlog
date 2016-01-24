package com.czp.opensrource.dlog.log.view.ws;

import java.io.IOException;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

/**
 * Function:XXX<br>
 *
 * Date :2016年1月24日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public class WSHandler extends WebSocketServlet {

	private static final long serialVersionUID = 1L;
	private LogSocket socket = new LogSocket();

	public void send(String message) throws IOException {
		socket.send(message);
	}

	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.getPolicy().setIdleTimeout(10000);
		factory.setCreator(new WebSocketCreator() {

			@Override
			public Object createWebSocket(ServletUpgradeRequest req,
					ServletUpgradeResponse resp) {
				// resp.setAcceptedSubProtocol("binary");
				return socket;
			}
		});
	}

}
