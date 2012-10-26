package com.dabao.spider.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.dabao.spider.bean.ColumnRule;
import com.dabao.spider.bean.Info;
import com.dabao.spider.log.WarnLog;
import com.dabao.spider.util.ObjectUtil;

public class JSOUPParser {

	public List<String> parserHTML(String html, String query, String attr) {
		Document doc = Jsoup.parse(html);
		List<Element> els = parser(doc, query);
		List<String> list = new ArrayList<String>();
		for (Element el : els) {
			if (attr == null) {
				list.add(el.ownText());
			} else {
				list.add(el.attr(attr));
			}
		}
		return list;
	}

	public void parserHTML(String html, List<ColumnRule> list, Info info) {
		Document doc = Jsoup.parse(html);
		for (ColumnRule columnRule : list) {
			String value = null;
			try {
				Element element = parser(doc, columnRule.getRule(), columnRule
						.getIndex());
				if (columnRule.getAttr() == null) {
					value = element.ownText().trim();
				} else {
					value = element.attr(columnRule.getAttr());
				}
				if (columnRule.getOcr_column() != null) {
					info.put(columnRule.getOcr_column(), value);
				} else if (columnRule.getFile_column() != null) {
					info.put(columnRule.getFile_column(), value);
				} else {
					info.put(columnRule.getName(), ObjectUtil.toObject(value,
							columnRule.getType()), columnRule.isOverwrite());
				}
			} catch (Exception e) {
				WarnLog.logger("字段 " + columnRule.getName() + " 赋值失败,"
						+ columnRule.getType() + ":" + value
						+ " ,ERROR MESSAGE:" + e.getMessage());
			}
		}
	}

	public Element parser(Document doc, String xpath, int index) {
		return doc.select(xpath).get(index);
	}

	public List<Element> parser(Document doc, String xpath) {
		return doc.select(xpath);
	}
}