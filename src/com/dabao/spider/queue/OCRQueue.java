package com.dabao.spider.queue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.dabao.spider.bean.OCR;

public class OCRQueue {
	private Queue<OCR> queue;

	public void init() {
		queue = new ConcurrentLinkedQueue<OCR>();
	}

	public OCR poll() {
		return queue.poll();
	}

	public boolean offer(OCR ocr) {
		return queue.offer(ocr);
	}
}