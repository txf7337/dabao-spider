package com.dabao.spider.job;

import com.dabao.spider.current.OCRThreadPool;
import com.dabao.spider.log.ErrorLog;

public class OCRJob {
	private OCRThreadPool pool;

	public void work() {
		try {
			pool.execute();
		} catch (Exception e) {
			ErrorLog.logger("识别文字时出现严重错误!" + e.getMessage());
		}
	}

	public void setPool(OCRThreadPool pool) {
		this.pool = pool;
	}

}
