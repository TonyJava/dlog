package com.czp.code.dlog.view.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import com.czp.code.dlog.view.LogViewer;

/**
 * Function: WebsocketLog
 * 
 * @author: jeff.cao@aoliday.com
 * @date: 2016年1月23日
 *        http://www.eclipse.org/jetty/documentation/current/jetty-websocket
 *        -server-api.html
 */
public class WebViewer extends LogViewer {

	private String useMap = "org.eclipse.jetty.servlet.Default.useFileMappedBuffer";
	private Server server;
	private int port;

	public WebViewer(int port, String servers, String zkServes) {
		super(servers, zkServes);
		this.port = port;
	}

	@Override
	public void start() {
		try {
			WSHandler handler = new WSHandler();
			WebAppContext web = new WebAppContext();
			web.setResourceBase("web");
			web.setParentLoaderPriority(true);
			web.addServlet(new ServletHolder(handler), "/ws");
			web.setInitParameter(useMap, System.getProperty("debug", "false"));

			server = new Server(port);
			server.setHandler(web);
			server.start();

			super.setHandler(handler);
			super.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		try {
			super.stop();
			server.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
