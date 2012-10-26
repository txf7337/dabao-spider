package com.dabao.spider.current;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.dabao.spider.Column;
import com.dabao.spider.ColumnSpider;
import com.dabao.spider.bean.Info;
import com.dabao.spider.bean.Proxy;
import com.dabao.spider.log.InfoLog;
import com.dabao.spider.queue.InfoQueue;
import com.dabao.spider.queue.ProxyQueue;
import com.dabao.spider.util.ThreadUtil;

public class SpiderThreadPool {
	private InfoQueue queue;
	private ProxyQueue proxyQueue;
	private Column column;
	private ColumnSpider columnSpider;
	private String table;
	private int poolSize = 3;
	private boolean useProxy = false;
	private int cycle = 1000;

	@SuppressWarnings("unchecked")
	public void execute() {
		InfoLog.logger("爬取数据源" + table + "使用线程数" + poolSize + ",每个线程爬取间隔"
				+ cycle + ",代理" + useProxy);
		ExecutorService pool = Executors.newFixedThreadPool(poolSize);
		List<Future> pool_list = new ArrayList<Future>();
		for (int i = 0; i < poolSize; i++) {
			pool_list.add(pool.submit(new Runnable() {

				@Override
				public void run() {
					Proxy proxy = useProxy ? proxyQueue.poll() : null;
					while (true) {
						if (queue.isFinished()) {
							break;
						}
						Info info = queue.poll();
						if (info == null) {
							ThreadUtil.sleep(cycle);
						} else {
							columnSpider.spider(info, column, proxy, table);
							ThreadUtil.sleep(cycle);
						}
					}
					InfoLog.logger("爬取数据源" + table + "线程"
							+ Thread.currentThread().getId() + "完成!");
				}
			}));
		}
		boolean finished = false;
		while (!finished) {
			for (Future f : pool_list) {
				finished = f.isDone();
			}
			ThreadUtil.sleep(60000);
		}
		InfoLog.logger("爬取数据源" + table + "任务完成!");
	}

	public void setColumn(Column column) {
		this.column = column;
	}

	public void setColumnSpider(ColumnSpider columnSpider) {
		this.columnSpider = columnSpider;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public void setUseProxy(boolean useProxy) {
		this.useProxy = useProxy;
	}

	public void setCycle(int cycle) {
		this.cycle = cycle;
	}

	public void setQueue(InfoQueue queue) {
		this.queue = queue;
	}

	public void setProxyQueue(ProxyQueue proxyQueue) {
		this.proxyQueue = proxyQueue;
	}

	public void setTable(String table) {
		this.table = table;
	}

}