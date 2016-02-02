package com.czp.code.dlog.view;

import java.nio.ByteBuffer;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
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
public class WebViewer extends WebSocketServlet implements WebSocketListener,
		Runnable {

	private String useMap = "org.eclipse.jetty.servlet.Default.useFileMappedBuffer";

	private CopyOnWriteArrayList<Session> clients = new CopyOnWriteArrayList<Session>();

	private static final ByteBuffer PING = ByteBuffer.allocate(1);

	private static final long serialVersionUID = 1L;

	private DaemonTimer timer;

	private Server server;

	private int port;

	public WebViewer(int port) {
		this.port = port;
		this.timer = DaemonTimer.getInstance();
	}

	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.setCreator(new WebSocketCreator() {

			@Override
			public Object createWebSocket(ServletUpgradeRequest req,
					ServletUpgradeResponse resp) {
				// resp.setAcceptedSubProtocol("binary");
				return WebViewer.this;
			}
		});
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
			throw new RuntimeException(e);
		}
	}

	public void stop() {
		try {
			server.stop();
			for (Session s : clients) {
				s.close();
			}
			timer.removeTask(this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onWebSocketBinary(byte[] data, int offset, int size) {
		throw new UnsupportedOperationException(" Unsupport  Binary Message");
	}

	@Override
	public void onWebSocketClose(int paramInt, String reason) {
		System.out.println(reason);
	}

	@Override
	public void onWebSocketConnect(Session session) {
		clients.add(session);
		timer.addTask(this, 1000);
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
			for (Session s : clients) {
				if (s.isOpen())
					s.getRemote().sendPing(PING);
				else
					clients.remove(s);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
