package com.dabao.spider.util;

import java.text.DateFormat;

import com.dabao.spider.log.WarnLog;

public class ObjectUtil {
	public static Object toObject(String value, String className) {
		try {
			className = className.toLowerCase();
			if (className.startsWith("varchar") || className.startsWith("char")
					|| className.startsWith("text")) {
				return value;
			}
			if (className.startsWith("int") || className.startsWith("smallint")) {
				return Integer.valueOf(value);
			}
			if (className.startsWith("bigint")) {
				return Long.valueOf(value);
			}
			if (className.startsWith("float")) {
				return Float.valueOf(value);
			}
			if (className.startsWith("double")) {
				return Double.valueOf(value);
			}
			if (className.startsWith("datetime")) {
				DateFormat df = DateFormat.getDateTimeInstance();
				return df.format(df.parse(value));
			}
			if (className.startsWith("date")) {
				DateFormat df = DateFormat.getDateInstance();
				return df.format(df.parse(value));
			}
			if (className.startsWith("time")) {
				DateFormat df = DateFormat.getTimeInstance();
				return df.format(df.parse(value));
			}
		} catch (Exception e) {
			WarnLog
					.logger("转化类型出错!" + value + ":" + className
							+ e.getMessage());
		}
		return null;
	}

}