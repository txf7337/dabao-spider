package com.dabao.spider.queue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.dabao.spider.bean.Info;

public class InfoQueue {
	private boolean isFinished = false;
	private Queue<Info> queue;

	public void init() {
		queue = new ConcurrentLinkedQueue<Info>();
	}

	public boolean isFinished() {
		if (isFinished && queue.isEmpty()) {
			return true;
		}
		return false;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	public Info poll() {
		return queue.poll();
	}

	public boolean offer(Info info) {
		return queue.offer(info);
	}

	public int size() {
		return queue.size();
	}
}
