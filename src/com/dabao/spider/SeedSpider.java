package com.dabao.spider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.dabao.spider.bean.Info;
import com.dabao.spider.bean.Proxy;
import com.dabao.spider.bean.Seed;
import com.dabao.spider.log.ErrorLog;
import com.dabao.spider.log.InfoLog;
import com.dabao.spider.parser.JSOUPParser;
import com.dabao.spider.queue.InfoQueue;
import com.dabao.spider.queue.ProxyQueue;
import com.dabao.spider.util.HttpUtil;
import com.dabao.spider.util.ThreadUtil;
import com.dabao.spider.util.URLUtil;

public class SeedSpider extends Thread {
	private List<Seed> seeds = new ArrayList<Seed>();
	private InfoQueue queue;
	private JSOUPParser parser;
	private ProxyQueue proxyQueue;
	private int cycle = 1000;
	private boolean useProxy = false;

	public void addSeed(Seed seed) {
		seeds.add(seed);
	}

	public void run() {
		Proxy proxy = useProxy ? proxyQueue.poll() : null;
		try {
			for (Seed seed : seeds) {
				for (int i = seed.getFrom(); i < seed.getTo() + 1; i++) {
					String url = seed.getUrl().replace("*", i + "");
					String html = HttpUtil.html(url, proxy);
					List<String> list = parser.parserHTML(html,
							seed.getQuery(), seed.getAttr());
					for (String s : list) {
						Info t = seed.getInfo();
						Info info = new Info();
						for (Iterator<String> iterator = t.keySet().iterator(); iterator
								.hasNext();) {
							String key = iterator.next();
							info.put(key, t.get(key), true);
						}
						info.put("spider_url", URLUtil.makeAbsoluteUrl(url, s),
								true);
						queue.offer(info);
					}
					ThreadUtil.sleep(cycle);
				}
			}
			InfoLog.logger("爬取种子任务完成!");
		} catch (Exception e) {
			ErrorLog.logger("爬取种子出现严重错误!" + e.getMessage());
		} finally {
			if (useProxy) {
				proxyQueue.offer(proxy);
			}
			queue.setFinished(true);
		}
	}

	public void setQueue(InfoQueue queue) {
		this.queue = queue;
	}

	public void setCycle(int cycle) {
		this.cycle = cycle;
	}

	public void setUseProxy(boolean useProxy) {
		this.useProxy = useProxy;
	}

	public void setProxyQueue(ProxyQueue proxyQueue) {
		this.proxyQueue = proxyQueue;
	}

	public void setParser(JSOUPParser parser) {
		this.parser = parser;
	}

}