package com.czp.opensrource.dlog.log.view.ws;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import kafka.consumer.ConsumerIterator;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import com.czp.opensrource.dlog.log.view.BaseLogViewer;

/**
 * Function: WebsocketLog
 * 
 * @author: jeff.cao@aoliday.com
 * @date: 2016年1月23日
 *        http://www.eclipse.org/jetty/documentation/current/jetty-websocket
 *        -server-api.html
 */
public class WebLogViewer extends BaseLogViewer {

	private String useMap = "org.eclipse.jetty.servlet.Default.useFileMappedBuffer";

	private Server server;

	private int port;

	public WebLogViewer(int port, String servers, String zkServes, String topic) {
		super(servers, zkServes, topic);
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

			startRecvMessgae(handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void startRecvMessgae(WSHandler handler) throws IOException,
			UnsupportedEncodingException {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put(topic, 1);
		ConsumerIterator<byte[], byte[]> it = consumer
				.createMessageStreams(map).get(topic).get(0).iterator();
		while (it.hasNext()) {
			try {
				handler.send(new String(it.next().message(), "utf-8"));
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void stop() {
		try {
			consumer.shutdown();
			server.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
