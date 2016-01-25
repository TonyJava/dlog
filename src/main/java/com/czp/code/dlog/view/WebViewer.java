package com.czp.code.dlog.view;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

/**
 * Function: WebsocketLog
 * 
 * @author: jeff.cao@aoliday.com
 * @date: 2016年1月23日
 *        http://www.eclipse.org/jetty/documentation/current/jetty-websocket
 *        -server-api.html
 */
public class WebViewer extends WebSocketServlet implements IMessageHandler,
		WebSocketListener, Runnable {

	private String useMap = "org.eclipse.jetty.servlet.Default.useFileMappedBuffer";

	private static final ByteBuffer PING = ByteBuffer.allocate(1);

	private static final long serialVersionUID = 1L;

	private LinkedList<String> buffer = new LinkedList<String>();

	private RemoteEndpoint remote;

	private Server server;

	private int port;

	public WebViewer(int port) {
		this.port = port;
	}

	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.getPolicy().setIdleTimeout(100000);
		factory.setCreator(new WebSocketCreator() {

			@Override
			public Object createWebSocket(ServletUpgradeRequest req,
					ServletUpgradeResponse resp) {
				// resp.setAcceptedSubProtocol("binary");
				return WebViewer.this;
			}
		});
	}

	@Override
	public void onMessage(byte[] message) {
		try {
			String strMsg = new String(message);
			if (remote == null) {
				buffer.add(strMsg);
			} else {
				remote.sendString(strMsg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		try {
			WebAppContext web = new WebAppContext();
			web.setResourceBase("web");
			web.setParentLoaderPriority(true);
			web.addServlet(new ServletHolder(this), "/ws");
			web.setInitParameter(useMap, System.getProperty("debug", "false"));

			server = new Server(port);
			server.setHandler(web);
			server.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		try {
			server.stop();
			DaemonTimer.getInstance().removeTask(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onWebSocketBinary(byte[] data, int offset, int size) {
		throw new UnsupportedOperationException(" Unsupport  Binary Message");
	}

	@Override
	public void onWebSocketClose(int paramInt, String reason) {
		DaemonTimer.getInstance().removeTask(this);
	}

	@Override
	public void onWebSocketConnect(Session session) {
		this.remote = session.getRemote();
		Iterator<String> iterator = buffer.iterator();
		while (iterator.hasNext()) {
			try {
				remote.sendString(iterator.next());
				iterator.remove();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		DaemonTimer.getInstance().addTask(this, 1000);
	}

	@Override
	public void onWebSocketError(Throwable err) {
		err.printStackTrace();
	}

	@Override
	public void onWebSocketText(String msg) {
		System.out.println("Recv ws client:" + msg);
	}

	@Override
	public void run() {
		try {
			remote.sendPing(PING);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
