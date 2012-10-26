package com.dabao.spider.current;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.dabao.spider.FileSpider;
import com.dabao.spider.bean.InfoFile;
import com.dabao.spider.bean.Proxy;
import com.dabao.spider.log.ErrorLog;
import com.dabao.spider.log.InfoLog;
import com.dabao.spider.queue.FileQueue;
import com.dabao.spider.queue.ProxyQueue;
import com.dabao.spider.util.ThreadUtil;

public class FileSpiderThreadPool {
	private int poolSize = 3;
	private int cycle = 500;
	private boolean useProxy = false;
	private FileQueue fileQueue;
	private ProxyQueue proxyQueue;
	private FileSpider fileSpider;

	public void execute() {
		ExecutorService pool = Executors.newFixedThreadPool(poolSize);
		for (int i = 0; i < poolSize; i++) {
			pool.execute(new Runnable() {

				@Override
				public void run() {
					Proxy proxy = useProxy ? proxyQueue.poll() : null;
					while (true) {
						InfoFile infoFile = fileQueue.poll();
						if (infoFile == null) {
							InfoLog.logger("文件队列没有数据,文件下载线程"
									+ Thread.currentThread().getId()
									+ "休息10分钟!");
							ThreadUtil.sleep(600000);
							continue;
						}
						if (infoFile.getFail_num() > 3) {
							ErrorLog.logger("下载文件" + infoFile.toString()
									+ "失败3次!");
							continue;
						}
						if (!fileSpider.spider(infoFile, proxy)) {
							infoFile.setFail_num(infoFile.getFail_num() + 1);
							fileQueue.offer(infoFile);
						}
						ThreadUtil.sleep(cycle);
					}
				}
			});
		}
		while (true) {
			ThreadUtil.sleep(3600000);
		}
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public void setCycle(int cycle) {
		this.cycle = cycle;
	}

	public void setUseProxy(boolean useProxy) {
		this.useProxy = useProxy;
	}

	public void setFileQueue(FileQueue fileQueue) {
		this.fileQueue = fileQueue;
	}

	public void setProxyQueue(ProxyQueue proxyQueue) {
		this.proxyQueue = proxyQueue;
	}

	public void setFileSpider(FileSpider fileSpider) {
		this.fileSpider = fileSpider;
	}

}
