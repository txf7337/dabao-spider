package com.dabao.spider.parser;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dabao.spider.bean.ColumnRule;
import com.dabao.spider.bean.Info;
import com.dabao.spider.log.WarnLog;
import com.dabao.spider.util.ObjectUtil;

public class RegularParser {

	public void parserString(String str, List<ColumnRule> list, Info info) {
		for (ColumnRule columnRule : list) {
			String value = null;
			try {
				Pattern pattern = Pattern.compile(columnRule.getRule());
				Matcher matcher = pattern.matcher(str);
				if (matcher.find()) {
					value = matcher.group(columnRule.getIndex() + 1);
				}
				if (value != null) {
					if (columnRule.getOcr_column() != null) {
						info.put(columnRule.getOcr_column(), value);
					} else if (columnRule.getFile_column() != null) {
						info.put(columnRule.getFile_column(), value);
					} else {
						info.put(columnRule.getName(), ObjectUtil.toObject(
								value, columnRule.getType()), columnRule
								.isOverwrite());
					}
				}
			} catch (Exception e) {
				WarnLog.logger("字段 " + columnRule.getName() + " 赋值失败,"
						+ columnRule.getType() + ":" + value
						+ " ,ERROR MESSAGE:" + e.getMessage());
			}
		}
	}
}