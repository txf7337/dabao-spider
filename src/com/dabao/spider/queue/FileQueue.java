package com.dabao.spider.queue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.dabao.spider.bean.InfoFile;

public class FileQueue {
	private Queue<InfoFile> queue;

	public void init() {
		queue = new ConcurrentLinkedQueue<InfoFile>();
	}

	public InfoFile poll() {
		return queue.poll();
	}

	public boolean offer(InfoFile infoFile) {
		return queue.offer(infoFile);
	}

}