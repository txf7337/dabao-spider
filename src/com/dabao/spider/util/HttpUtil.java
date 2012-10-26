package com.dabao.spider.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlMapper;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.parser.html.IdentityHtmlMapper;
import org.apache.tika.sax.ToHTMLContentHandler;
import org.xml.sax.ContentHandler;

import com.dabao.spider.log.WarnLog;

public class HttpUtil {

	public static String html(String strUrl, com.dabao.spider.bean.Proxy proxy) {
		HttpURLConnection conn = null;
		InputStream in = null;
		try {
			URL url = new URL(strUrl);
			if (proxy == null) {
				conn = (HttpURLConnection) url.openConnection();
			} else {
				conn = (HttpURLConnection) url.openConnection(new Proxy(
						Proxy.Type.HTTP, new InetSocketAddress(proxy.getHost(),
								proxy.getPort())));
				if (proxy.getAuth() != null) {
					conn.setRequestProperty("Proxy-Authorization", proxy
							.getAuth());
				}
			}
			conn
					.addRequestProperty("User-Agent",
							"Mozilla/5.0 (compatible; dabao/0.7; +http://www.soxieke.com)");
			conn.setConnectTimeout(10000);
			in = new BufferedInputStream(conn.getInputStream());
			ContentHandler handler = new ToHTMLContentHandler();
			Metadata metadata = new Metadata();
			ParseContext parseContext = new ParseContext();
			parseContext.set(HtmlMapper.class, IdentityHtmlMapper.INSTANCE);
			HtmlParser parser = new HtmlParser();
			parser.parse(in, handler, metadata, parseContext);
			return handler.toString();
		} catch (Exception e) {
			WarnLog.logger("URL:" + strUrl + " GET HTML ERROR"
					+ ",ERROR MESSAGE:" + e.getMessage() + ",PROXY:"
					+ proxy.toString());
		} finally {
			try {
				in.close();
			} catch (Exception e) {
			}
			try {
				conn.disconnect();
			} catch (Exception e) {

			}
		}
		return null;
	}

	public static String htmlText(String strUrl,
			com.dabao.spider.bean.Proxy proxy) {
		HttpURLConnection conn = null;
		BufferedReader reader = null;
		try {
			URL url = new URL(strUrl);
			if (proxy == null) {
				conn = (HttpURLConnection) url.openConnection();
			} else {
				conn = (HttpURLConnection) url.openConnection(new Proxy(
						Proxy.Type.HTTP, new InetSocketAddress(proxy.getHost(),
								proxy.getPort())));
				if (proxy.getAuth() != null) {
					conn.setRequestProperty("Proxy-Authorization", proxy
							.getAuth());
				}
			}
			conn
					.addRequestProperty("User-Agent",
							"Mozilla/5.0 (compatible; dabao/0.7; +http://www.soxieke.com)");
			conn.setConnectTimeout(10000);
			reader = new BufferedReader(new InputStreamReader(conn
					.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String lines = null;
			while ((lines = reader.readLine()) != null) {
				sb.append(lines);
			}
			return sb.toString();
		} catch (Exception e) {
			WarnLog.logger("URL:" + strUrl + " GET HTML ERROR"
					+ ",ERROR MESSAGE:" + e.getMessage() + ",PROXY:"
					+ proxy.toString());
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
			}
			try {
				conn.disconnect();
			} catch (Exception e) {
			}
		}
		return null;
	}

	public static String downFile(String url, String basePath, String path) {
		try {
			String result = path
					+ url.substring(url.lastIndexOf("/") + 1, url.length());
			FileUtils.copyURLToFile(new URL(url), new File(basePath + result));
			return result;
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
			return null;
		}
	}


}