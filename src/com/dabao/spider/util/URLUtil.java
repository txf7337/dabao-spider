package com.dabao.spider.util;

import java.net.MalformedURLException;
import java.net.URL;

import com.dabao.spider.log.ErrorLog;

public class URLUtil {
	public static String makeAbsoluteUrl(String baseURL, String relativeURL) {
		if (relativeURL == null) {
			return null;
		}
		if (relativeURL.startsWith("http://")) {
			return relativeURL;
		}
		if (relativeURL.startsWith("www.")) {
			return "http://" + relativeURL;
		}
		try {
			URL url = new URL(baseURL);
			if (relativeURL.startsWith("/")) {
				return "http://" + url.getHost() + relativeURL;
			}
			return baseURL.substring(0, baseURL.lastIndexOf("/") + 1)
					+ relativeURL;
		} catch (MalformedURLException e) {
			ErrorLog.logger("这是链接吗?" + baseURL + "-" + relativeURL + "-"
					+ e.getMessage());
			return null;
		}
	}

}
