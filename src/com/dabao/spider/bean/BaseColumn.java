package com.dabao.spider.bean;

import java.util.HashMap;
import java.util.Map;

public class BaseColumn {
	public static final String KEY_UUID = "uuid";
	public static final String KEY_SPIDER_URL = "spider_url";
	public static final String KEY_SPIDER_ID = "spider_id";
	public static final String KEY_SPIDER_STATUS = "spider_status";
	public static final String KEY_SPIDER_LAST_MODIFY_TIME = "spider_last_modify_time";
	private Map<String, String> map;

	public BaseColumn() {
		map = new HashMap<String, String>();
		map.put(KEY_UUID, "BIGINT(20)");
		map.put(KEY_SPIDER_URL, "VARCHAR(500)");
		map.put(KEY_SPIDER_ID, "INTEGER NOT NULL AUTO_INCREMENT");
		map.put("PRIMARY KEY", "(spider_id)");
		map.put(KEY_SPIDER_STATUS, "INT(11)");
		map.put(KEY_SPIDER_LAST_MODIFY_TIME, "DATETIME");
	}

	public String get(String key) {
		return map.get(key);
	}

	public Map<String, String> getMap() {
		return map;
	}
}