package com.dabao.spider.job;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.dabao.spider.InfoSpider;
import com.dabao.spider.Spider;
import com.dabao.spider.util.ThreadUtil;

public class SpiderJob {
	private ThreadPoolTaskExecutor taskExecutor;

	public void work() {
		List<String> source_list = new ArrayList<String>();
		File file = new File("./conf/source");
		for (String f : file.list()) {
			if (f.endsWith(".xml")) {
				source_list.add(f);
			}
		}
		for (String source : source_list) {
			InfoSpider is = (InfoSpider) Spider.ac.getBean("infoSpider");
			is.setFileName(source);
			taskExecutor.execute(is);
		}
		ThreadUtil.sleep(3000);
		while (taskExecutor.getActiveCount() > 0) {
			ThreadUtil.sleep(600000);
		}
	}

	public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

}