package com.dabao.spider;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.DocumentHelper;

import com.dabao.spider.bean.ColumnRule;
import com.dabao.spider.bean.Info;
import com.dabao.spider.bean.InfoFile;
import com.dabao.spider.bean.OCR;
import com.dabao.spider.bean.Proxy;
import com.dabao.spider.log.InfoLog;
import com.dabao.spider.log.WarnLog;
import com.dabao.spider.parser.JSOUPParser;
import com.dabao.spider.parser.RegularParser;
import com.dabao.spider.parser.XPATHParser;
import com.dabao.spider.queue.FileQueue;
import com.dabao.spider.queue.OCRQueue;
import com.dabao.spider.service.SpiderService;
import com.dabao.spider.util.HttpUtil;

public class ColumnSpider {
	private SpiderService spiderService;
	private FileQueue fileQueue;
	private OCRQueue ocrQueue;
	private JSOUPParser jParser;
	private XPATHParser xParser;
	private RegularParser rParser;

	public void spider(Info info, Column column, Proxy proxy, String table) {
		String html = HttpUtil.html(info.get("spider_url").toString(), proxy);
		jParser.parserHTML(html, column.getHTMLRule(), info);
		Map<String, List<ColumnRule>> map = column.getJSONRule();
		for (Iterator<String> i = map.keySet().iterator(); i.hasNext();) {
			String url = i.next();
			try {
				String r_url = format(url, info);
				xParser.JSONparser(HttpUtil.htmlText(r_url, proxy), map
						.get(url), info);
			} catch (Exception e) {
				WarnLog.logger("JSON抽取出现错误!" + e.getMessage());
			}
		}
		map = column.getXMLRule();
		for (Iterator<String> i = map.keySet().iterator(); i.hasNext();) {
			String url = i.next();
			try {
				String r_url = format(url, info);
				xParser.DOCParser(DocumentHelper.parseText(HttpUtil.htmlText(
						r_url, proxy)), map.get(url), info);
			} catch (Exception e) {
				WarnLog.logger("XML抽取出现错误!" + e.getMessage());
			}
		}
		map = column.getTextRule();
		for (Iterator<String> i = map.keySet().iterator(); i.hasNext();) {
			String url = i.next();
			try {
				String r_url = format(url, info);
				rParser.parserString(HttpUtil.htmlText(r_url, proxy), map
						.get(url), info);
			} catch (Exception e) {
				WarnLog.logger("正则抽取出现错误!" + e.getMessage());
			}
		}
		try {
			int id = spiderService.saveInfo(table, info);
			for (ColumnRule cr : column.getOCRColumnRule()) {
				OCR ocr = new OCR();
				ocr.setId(id);
				ocr.setTable(table);
				ocr.setColumn(cr.getName());
				ocr.setOcr_column(cr.getOcr_column());
				ocrQueue.offer(ocr);
				InfoLog.logger("添加ocr至队列成功!" + ocr.toString());
			}
			for (ColumnRule cr : column.getFileColumnRule()) {
				InfoFile infoFile = new InfoFile();
				infoFile.setId(id);
				infoFile.setTable(table);
				infoFile.setColumn(cr.getName());
				infoFile.setFile_column(cr.getFile_column());
				infoFile.setPath(cr.getFile_path());
				fileQueue.offer(infoFile);
				InfoLog.logger("添加file至队列成功!" + infoFile.toString());
			}
		} catch (Exception e) {
			WarnLog.logger("保存数据时出现错误!" + e.getMessage());
		}
	}

	private String format(String url, Info info) throws Exception {
		Pattern p = Pattern.compile("\\{(.*?)\\}");
		Matcher m = p.matcher(url);
		while (m.find()) {
			String k = m.group();
			k = k.substring(1, k.length() - 1);
			if (info.get(k) != null) {
				url = url.replaceAll("\\{" + k + "\\}", info.get(k).toString());
			} else {
				throw new Exception("字段" + k + "的值为空");
			}
		}
		return url;
	}

	public void setSpiderService(SpiderService spiderService) {
		this.spiderService = spiderService;
	}

	public void setFileQueue(FileQueue fileQueue) {
		this.fileQueue = fileQueue;
	}

	public void setOcrQueue(OCRQueue ocrQueue) {
		this.ocrQueue = ocrQueue;
	}

	public void setJParser(JSOUPParser parser) {
		jParser = parser;
	}

	public void setXParser(XPATHParser parser) {
		xParser = parser;
	}

	public void setRParser(RegularParser parser) {
		rParser = parser;
	}

}