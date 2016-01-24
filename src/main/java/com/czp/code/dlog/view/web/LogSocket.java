package com.czp.code.dlog.view.web;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

/**
 * Function:XXX<br>
 *
 * Date :2016年1月24日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public class LogSocket extends WebSocketAdapter implements Runnable {

	private static final ByteBuffer PING = ByteBuffer.allocate(1);
	private LinkedList<String> buffer = new LinkedList<String>();
	private RemoteEndpoint remote;

	@Override
	public void onWebSocketText(String message) {
		System.out.println("Recv-client:" + message);
	}

	public void send(String message) throws IOException {
		if (remote == null) {
			buffer.add(message);
		} else {
			remote.sendString(message);
		}
	}

	@Override
	public void onWebSocketConnect(Session sess) {
		try {
			super.onWebSocketConnect(sess);
			remote = sess.getRemote();
			remote.sendString("thanks to use weblog");
			
			Iterator<String> iterator = buffer.iterator();
			while (iterator.hasNext()) {
				remote.sendString(iterator.next());
				iterator.remove();
			}
			Thread thread = new Thread(this, "WS-PING");
			thread.setDaemon(true);
			thread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (!Thread.interrupted()&&isConnected()) {
			if (remote != null)
				try {
					remote.sendPing(PING);
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}

}
