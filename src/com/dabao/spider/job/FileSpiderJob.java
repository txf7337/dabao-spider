package com.dabao.spider.job;

import com.dabao.spider.current.FileSpiderThreadPool;
import com.dabao.spider.log.ErrorLog;

public class FileSpiderJob {
	private FileSpiderThreadPool pool;

	public void work() {
		try {
			pool.execute();
		} catch (Exception e) {
			ErrorLog.logger("下载文件时出现严重错误!" + e.getMessage());
		}
	}

	public void setPool(FileSpiderThreadPool pool) {
		this.pool = pool;
	}

}