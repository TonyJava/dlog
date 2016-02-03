package com.czp.code.dlog.analysis;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.consumer.TopicFilter;
import kafka.consumer.Whitelist;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

/**
 * Function:XXX<br>
 *
 * Date :2016年2月3日 <br>
 * Author :coder_czp@126.com<br>
 * Copyright (c) 2015,coder_czp@126.com All Rights Reserved.
 */
public abstract class AbstractHandler implements Runnable {

	/** 当前需要汇总的内存数据库名称 */
	protected BlockingQueue<MemMoryDB> dbs = new LinkedBlockingQueue<MemMoryDB>();

	protected ConsumerConnector consumer;

	protected int statictisThreshold;

	protected long statictisPeroid;

	protected String statictisSQL;

	protected AtomicLong startTime;

	protected AtomicInteger count;

	protected String tableSql;

	protected String zkser;

	protected String topic;

	protected MemMoryDB dao;

	public void onMessage(String topic, String message) {
		int currentLine = count.getAndIncrement();
		String sql = parseMessage(currentLine, topic, message);
		long now = System.currentTimeMillis();
		if (currentLine >= statictisThreshold
				|| (now - startTime.get()) >= statictisPeroid) {
			synchronized (this) {
				this.dbs.add(dao);
				this.startTime.set(now);
				this.dao.shutdown();
				this.count.set(0);
				String dbName = String.valueOf(System.nanoTime());
				this.dao = new MemMoryDB(tableSql, dbName);
			}
		}
		dao.excuteDML(sql);
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				MemMoryDB query = dbs.take();
				processStaticResult(query.query(statictisSQL));
				query.shutdown();
				query.deletDBFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void init() {
		this.count = new AtomicInteger();
		String dbName = String.valueOf(System.nanoTime());
		this.startTime = new AtomicLong(System.currentTimeMillis());
		this.dao = new MemMoryDB(dbName, tableSql);

		final Properties props = new Properties();
		props.put("group.id", topic);
		props.put("zookeeper.connect", zkser);

		Thread consumerTh = new Thread(new Runnable() {

			@Override
			public void run() {
				ConsumerConfig cfg = new ConsumerConfig(props);
				consumer = Consumer.createJavaConsumerConnector(cfg);
				TopicFilter arg0 = new Whitelist(topic);
				List<KafkaStream<byte[], byte[]>> partitions = consumer.createMessageStreamsByFilter(arg0);
				while (!Thread.interrupted()) {
					for (KafkaStream<byte[], byte[]> partition : partitions) {
						ConsumerIterator<byte[], byte[]> it = partition
								.iterator();
						while (it.hasNext()) {
							MessageAndMetadata<byte[], byte[]> msg = it.next();
							onMessage(msg.topic(), new String(msg.message()));
						}
					}
				}
			}
		}, "consumer-" + topic);
		consumerTh.setDaemon(true);
		consumerTh.start();

		Thread th = new Thread(this, "count-thread");
		th.setDaemon(true);
		th.start();

	}

	public void shutDown() {
		dao.shutdown();
	}

	public void setStatictisThreshold(int statictisThreshold) {
		this.statictisThreshold = statictisThreshold;
	}

	public void setStatictisPeroid(long statictisPeroid) {
		this.statictisPeroid = statictisPeroid;
	}

	public void setStatictisSQL(String statictisSQL) {
		this.statictisSQL = statictisSQL;
	}

	public void setTableSql(String tableSql) {
		this.tableSql = tableSql;
	}

	public void setZkser(String zkser) {
		this.zkser = zkser;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	protected abstract String parseMessage(int id, String topic, String message);

	protected abstract void processStaticResult(List<Map<String, Object>> query);
}
