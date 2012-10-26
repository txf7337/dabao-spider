package com.dabao.spider;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.dabao.spider.bean.ColumnRule;
import com.dabao.spider.bean.Info;
import com.dabao.spider.bean.Seed;
import com.dabao.spider.current.SpiderThreadPool;
import com.dabao.spider.log.ErrorLog;
import com.dabao.spider.parser.XPATHParser;
import com.dabao.spider.queue.InfoQueue;
import com.dabao.spider.service.SpiderService;
import com.dabao.spider.util.ObjectUtil;

public class InfoSpider implements Runnable {
	private String fileName;
	private String tableName;
	private XPATHParser xpathParser;
	private InfoQueue queue;
	private SeedSpider seedSpider;
	private SpiderThreadPool pool;
	private Column column;
	private SpiderService spiderService;

	public void setFileName(String fileName) {
		this.fileName = fileName;
		this.tableName = fileName.split("\\.")[0];
	}

	@Override
	public void run() {
		try {
			Document doc = new SAXReader().read("./conf/source/" + fileName);
			initColumn(doc);
			if (spiderService.checkTable(tableName, column.getList())) {
				initBase(doc);
				initSeed(doc);
				seedSpider.start();
				pool.setTable(tableName);
				pool.setQueue(queue);
				pool.setColumn(column);
				pool.execute();
			}
		} catch (Exception e) {
			ErrorLog.logger("初始化" + fileName + "出现严重错误!");
		}
	}

	public void initColumn(Document doc) {
		column = new Column();
		List<Element> els = xpathParser.parser(doc, "/source/columns/column");
		for (Element el : els) {
			ColumnRule cr = new ColumnRule();
			String t = el.attributeValue("name");
			cr.setName(t);
			t = el.attributeValue("type");
			cr.setType(t);
			t = el.attributeValue("file_column");
			if (t != null && t.trim().length() > 0) {
				cr.setFile_column(t);
			}
			t = el.attributeValue("file_path");
			if (t != null && t.trim().length() > 0) {
				cr.setFile_path(t);
			}
			t = el.attributeValue("overwrite");
			if (t != null && t.equals("true")) {
				cr.setOverwrite(true);
			}
			t = el.attributeValue("ocr_column");
			if (t != null && t.trim().length() > 0) {
				cr.setOcr_column(t);
			}
			Element elt = el.element("rule");
			t = elt.attributeValue("type");
			cr.setRule_type(t);
			t = elt.attributeValue("value");
			cr.setRule(t);
			t = elt.attributeValue("index");
			if (t != null && t.matches("[-\\+]?\\d+")) {
				cr.setIndex(Integer.parseInt(t));
			}
			t = elt.attributeValue("attr");
			if (t != null && t.trim().length() > 0) {
				cr.setAttr(t);
			}
			t = elt.attributeValue("url");
			if (t != null && t.trim().length() > 0) {
				cr.setUrl(t);
			}
			column.add(cr);
		}
		els = xpathParser.parser(doc, "/source/seeds/seed/columns/column");
		for (Element el : els) {
			ColumnRule cr = new ColumnRule();
			String t = el.attributeValue("name");
			cr.setName(t);
			t = el.attributeValue("type");
			cr.setType(t);
			if (!column.contains(cr)) {
				column.add(cr);
			}
		}
	}

	public void initBase(Document doc) {
		Element el = xpathParser.parser(doc, "/source/base", 0);
		String t = el.attributeValue("cycle");
		if (t.matches("\\d+")) {
			pool.setCycle(Integer.parseInt(t));
			seedSpider.setCycle(Integer.parseInt(t));
		}
		t = el.attributeValue("threads");
		if (t.matches("\\d+")) {
			pool.setPoolSize(Integer.parseInt(t));
		}
		t = el.attributeValue("is-proxy");
		if (t.equals("true")) {
			pool.setUseProxy(true);
			seedSpider.setUseProxy(true);
		}
	}

	public void initSeed(Document doc) {
		seedSpider.setQueue(queue);
		List<Element> els = xpathParser.parser(doc, "/source/seeds/seed");
		for (Element el_seed : els) {
			Seed seed = new Seed();
			seed.setUrl(el_seed.attributeValue("url"));
			String t = el_seed.attributeValue("from");
			if (t.matches("\\d+")) {
				seed.setFrom(Integer.parseInt(t));
			}
			t = el_seed.attributeValue("to");
			if (t.matches("\\d+")) {
				seed.setTo(Integer.parseInt(t));
			}
			seed.setQuery(el_seed.attributeValue("query"));
			seed.setAttr(el_seed.attributeValue("attr"));
			Info info = new Info();
			for (Object obj : el_seed.selectNodes("columns/column")) {
				Element el = (Element) obj;
				info.put(el.attributeValue("name"), ObjectUtil.toObject(el
						.attributeValue("value"), el.attributeValue("type")));
			}
			seed.setInfo(info);
			seedSpider.addSeed(seed);
		}
	}

	public void setXpathParser(XPATHParser xpathParser) {
		this.xpathParser = xpathParser;
	}

	public void setQueue(InfoQueue queue) {
		this.queue = queue;
	}

	public void setSeedSpider(SeedSpider seedSpider) {
		this.seedSpider = seedSpider;
	}

	public void setPool(SpiderThreadPool pool) {
		this.pool = pool;
	}

	public void setSpiderService(SpiderService spiderService) {
		this.spiderService = spiderService;
	}

}