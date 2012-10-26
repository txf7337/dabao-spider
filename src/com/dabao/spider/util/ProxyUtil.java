package com.dabao.spider.util;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import org.apache.tika.io.IOUtils;

import com.dabao.spider.log.WarnLog;

public class ProxyUtil {
	public static boolean checkProxy(com.dabao.spider.bean.Proxy proxy) {
		if (proxy == null) {
			return false;
		}
		try {
			URL url = new URL("http://www.baidu.com");
			URLConnection conn = url.openConnection(new Proxy(Proxy.Type.HTTP,
					new InetSocketAddress(proxy.getHost(), proxy.getPort())));
			if (proxy.getAuth() != null) {
				conn.setRequestProperty("Proxy-Authorization", proxy.getAuth());
			}
			InputStream in = conn.getInputStream();
			String s = IOUtils.toString(in);
			if (s.indexOf("百度") > 0) {
				return true;
			}
		} catch (Exception e) {
			WarnLog.logger(e.getMessage());
		}
		WarnLog.logger("代理" + proxy.getHost() + "失效!");
		return false;
	}

}