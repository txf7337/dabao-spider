package com.dabao.spider.job;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.dabao.spider.bean.Proxy;
import com.dabao.spider.log.ErrorLog;
import com.dabao.spider.log.InfoLog;
import com.dabao.spider.log.WarnLog;
import com.dabao.spider.parser.XPATHParser;
import com.dabao.spider.queue.ProxyQueue;
import com.dabao.spider.util.ProxyUtil;

public class ProxyJob {
	private ProxyQueue queue;
	private XPATHParser xpathParser;

	public void work() {
		for (int i = 0; i < queue.size(); i++) {
			Proxy proxy = queue.poll();
			if (ProxyUtil.checkProxy(proxy)) {
				queue.offer(proxy);
			}
		}
		try {
			Document doc = new SAXReader().read("./conf/proxy.xml");
			List<Element> list = xpathParser.parser(doc, "");
			for (Element el : list) {
				Proxy proxy = new Proxy();
				String host = el.element("host").getTextTrim();
				String port = el.element("port").getTextTrim();
				if (host != null && port != null && port.matches("\\d+")) {
					proxy.setHost(host);
					proxy.setPort(Integer.parseInt(port));
					Element auth = el.element("auth");
					if (auth != null) {
						String user = auth.attributeValue("user");
						String pwd = auth.attributeValue("pwd");
						if (user != null && pwd != null) {
							proxy.setAuth(user, pwd);
						}
					}
					if (ProxyUtil.checkProxy(proxy)) {
						queue.offer(proxy);
						InfoLog.logger("新代理" + proxy.getHost() + "添加成功!");
					}
				}
			}
		} catch (Exception e) {
			ErrorLog.logger("加载proxy.xml出错!" + e.getMessage());
		}
		if (queue.size() < 20) {
			WarnLog.logger("代理数量少于20!");
		}
	}

	public void setQueue(ProxyQueue queue) {
		this.queue = queue;
	}

}