package com.dabao.spider.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Info {
	private Map<String, Object> map;

	public Info() {
		map = new HashMap<String, Object>();
	}

	public Object get(String key) {
		return map.get(key);
	}

	/**
	 * 要是不存在key,添加key-value
	 */
	public void put(String key, Object value) {
		put(key, value, false);
	}

	/**
	 * true:覆盖添加,false:等同于put(String key, Object value)
	 */
	public void put(String key, Object value, boolean b) {
		if (b) {
			map.put(key, value);
		} else {
			if (map.containsKey(key)) {
				return;
			} else {
				map.put(key, value);
			}
		}
	}

	/**
	 * org.springframework.jdbc.core.JdbcTemplate.update(String sql,Object[]
	 * args)
	 * 
	 * @return 逗号隔开的字段名
	 */
	public String getKeys() {
		StringBuffer sb = new StringBuffer();
		for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
			sb.append(it.next() + ",");
		}
		sb.append(BaseColumn.KEY_SPIDER_STATUS);
		return sb.toString();
	}

	public String getQuestions() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < map.size(); i++) {
			sb.append("?,");
		}
		sb.append("?");
		return sb.toString();
	}

	/**
	 * org.springframework.jdbc.core.JdbcTemplate.update(String sql,Object[]
	 * args)
	 * 
	 * @return 与getKeys()相对应的value数组
	 */
	public Object[] getParams() {
		List<Object> list = new ArrayList<Object>();
		for (Object obj : map.values()) {
			list.add(obj);
		}
		list.add(1);
		return list.toArray();
	}

	/**
	 * 更新有值的字段
	 * 
	 * @return
	 */
	public String getUpdateKeys() {
		StringBuffer sb = new StringBuffer();
		for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			if (map.get(key) != null) {
				sb.append(key + "=?,");
			}
		}
		sb.append(BaseColumn.KEY_SPIDER_STATUS + "=?");
		return sb.toString();
	}

	/**
	 * 更新有值的字段
	 * 
	 * @return
	 */
	public Object[] getUpdateParams() {
		List<Object> list = new ArrayList<Object>();
		for (Object obj : map.values()) {
			if (obj != null) {
				list.add(obj);
			}
		}
		list.add(2);
		return list.toArray();
	}

	public Set<String> keySet() {
		return map.keySet();
	}
}
