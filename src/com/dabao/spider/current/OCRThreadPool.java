package com.dabao.spider.current;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.dabao.spider.OCRSpider;
import com.dabao.spider.bean.OCR;
import com.dabao.spider.bean.Proxy;
import com.dabao.spider.log.ErrorLog;
import com.dabao.spider.log.InfoLog;
import com.dabao.spider.queue.OCRQueue;
import com.dabao.spider.queue.ProxyQueue;
import com.dabao.spider.util.ThreadUtil;

public class OCRThreadPool {
	private OCRQueue ocrQueue;
	private ProxyQueue proxyQueue;
	private OCRSpider ocrSpider;
	private int poolSize = 3;
	private int cycle = 500;
	private boolean useProxy = false;

	public void execute() {
		ExecutorService pool = Executors.newFixedThreadPool(poolSize);
		for (int i = 0; i < poolSize; i++) {
			pool.execute(new Runnable() {

				@Override
				public void run() {
					Proxy proxy = useProxy ? proxyQueue.poll() : null;
					while (true) {
						OCR ocr = ocrQueue.poll();
						if (ocr == null) {
							InfoLog.logger("ocr队列没有数据,文字识别线程"
									+ Thread.currentThread().getId()
									+ "休息10分钟!");
							ThreadUtil.sleep(600000);
							continue;
						}
						if (ocr.getFail_num() > 3) {
							ErrorLog.logger("识别文字" + ocr.toString() + "失败3次!");
							continue;
						}
						if (!ocrSpider.spider(ocr, proxy)) {
							ocr.setFail_num(ocr.getFail_num() + 1);
							ocrQueue.offer(ocr);
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

	public void setOcrQueue(OCRQueue ocrQueue) {
		this.ocrQueue = ocrQueue;
	}

	public void setProxyQueue(ProxyQueue proxyQueue) {
		this.proxyQueue = proxyQueue;
	}

	public void setOcrSpider(OCRSpider ocrSpider) {
		this.ocrSpider = ocrSpider;
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
}
