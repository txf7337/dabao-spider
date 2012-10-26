package com.dabao.spider.parser;

import java.util.List;

import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.dabao.spider.bean.ColumnRule;
import com.dabao.spider.bean.Info;
import com.dabao.spider.log.WarnLog;
import com.dabao.spider.util.ObjectUtil;

public class XPATHParser {

	public void XMLparser(String xml, List<ColumnRule> list, Info info) {
		try {
			DOCParser(DocumentHelper.parseText(xml), list, info);
		} catch (DocumentException e) {
			WarnLog.logger("DOM4J READ XML ERROR. ERROR MESSAGE:"
					+ e.getMessage());
		}
	}

	public void JSONparser(String json, List<ColumnRule> list, Info info) {
		XMLparser(new XMLSerializer().write(JSONSerializer.toJSON(json)), list,
				info);
	}

	public void DOCParser(Document doc, List<ColumnRule> list, Info info) {
		for (ColumnRule columnRule : list) {
			String value = null;
			try {
				Element element = parser(doc, columnRule.getRule(), columnRule
						.getIndex());
				if (columnRule.getAttr() == null) {
					value = element.getTextTrim();
				} else {
					value = element.attributeValue(columnRule.getAttr());
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
		return (Element) doc.selectNodes(xpath).get(index);
	}

	@SuppressWarnings("unchecked")
	public List<Element> parser(Document doc, String xpath) {
		return doc.selectNodes(xpath);
	}

}