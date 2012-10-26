package com.dabao.spider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dabao.spider.bean.Info;
import com.dabao.spider.bean.InfoFile;
import com.dabao.spider.bean.Proxy;
import com.dabao.spider.log.WarnLog;
import com.dabao.spider.service.SpiderFileService;
import com.dabao.spider.util.HttpUtil;
import com.dabao.spider.util.URLUtil;

public class FileSpider {
	private SpiderFileService sfService;
	private String basepath = "./file/";

	public boolean spider(InfoFile infoFile, Proxy proxy) {
		Info info = sfService.findInfo(infoFile);
		if (info.get(infoFile.getColumn()) == null) {
			try {
				String url = URLUtil.makeAbsoluteUrl(info.get("spider_url")
						.toString(), info.get(infoFile.getFile_column())
						.toString());
				String value = HttpUtil.downFile(url, basepath, infoFile
						.getTable()
						+ "/" + pathFormat(infoFile.getPath()));
				if (value == null) {
					return false;
				} else {
					infoFile.setValue(value);
					sfService.saveInfoFile(infoFile);
				}
			} catch (Exception e) {
				WarnLog.logger("下载图片" + infoFile.toString() + "出现错误!"
						+ e.getMessage());
				return false;
			}
		}
		return true;
	}

	private String pathFormat(String path) {
		Pattern p = Pattern.compile("\\{(.*?)\\}");
		Matcher m = p.matcher(path);
		Date date = new Date();
		while (m.find()) {
			String k = m.group();
			k = k.substring(1, k.length() - 1);
			path = path.replaceAll("\\{" + k + "\\}", new SimpleDateFormat(k)
					.format(date));
		}
		return path;
	}

	public void setSfService(SpiderFileService sfService) {
		this.sfService = sfService;
	}

	public void setBasepath(String basepath) {
		this.basepath = basepath;
	}
}