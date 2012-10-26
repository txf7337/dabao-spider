package com.dabao.spider.queue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.dabao.spider.bean.Proxy;
import com.dabao.spider.util.ProxyUtil;

public class ProxyQueue {
	private Queue<Proxy> queue;

	public void init() {
		queue = new ConcurrentLinkedQueue<Proxy>();
	}

	public Proxy poll() {
		Proxy proxy = queue.poll();
		if (proxy == null) {
			return null;
		}
		if (ProxyUtil.checkProxy(proxy)) {
			return proxy;
		}
		return poll();
	}

	public boolean offer(Proxy proxy) {
		return queue.offer(proxy);
	}

	public int size() {
		return queue.size();
	}
}