package com.dabao.spider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dabao.spider.bean.ColumnRule;

public class Column {
	private List<ColumnRule> list;

	public Column() {
		list = new ArrayList<ColumnRule>();
	}

	public void add(ColumnRule cr) {
		list.add(cr);
	}

	/**
	 * 
	 * @return 返回所有用HTML解析的字段
	 */
	public List<ColumnRule> getHTMLRule() {
		List<ColumnRule> list = new ArrayList<ColumnRule>();
		for (ColumnRule cr : this.list) {
			if ("html".equals(cr.getRule_type())) {
				list.add(cr);
			}
		}
		return list;
	}

	/**
	 * String是url
	 * 
	 * @return 返回所有用XML解析的字段
	 */
	public Map<String, List<ColumnRule>> getXMLRule() {
		Map<String, List<ColumnRule>> map = new HashMap<String, List<ColumnRule>>();
		for (ColumnRule cr : this.list) {
			if ("xml".equals(cr.getRule_type())) {
				List<ColumnRule> list = null;
				if (map.containsKey(cr.getUrl())) {
					list = map.get(cr.getUrl());
					list.add(cr);
				} else {
					list = new ArrayList<ColumnRule>();
					list.add(cr);
				}
				map.put(cr.getUrl(), list);
			}
		}
		return map;
	}

	/**
	 * String是url
	 * 
	 * @return 返回所有用JSON解析的字段
	 */
	public Map<String, List<ColumnRule>> getJSONRule() {
		Map<String, List<ColumnRule>> map = new HashMap<String, List<ColumnRule>>();
		for (ColumnRule cr : this.list) {
			if ("json".equals(cr.getRule_type())) {
				List<ColumnRule> list = null;
				if (map.containsKey(cr.getUrl())) {
					list = map.get(cr.getUrl());
					list.add(cr);
				} else {
					list = new ArrayList<ColumnRule>();
					list.add(cr);
				}
				map.put(cr.getUrl(), list);
			}
		}
		return map;
	}

	/**
	 * String是url
	 * 
	 * @return 返回所有用JS(正则匹配)解析的字段
	 */
	public Map<String, List<ColumnRule>> getTextRule() {
		Map<String, List<ColumnRule>> map = new HashMap<String, List<ColumnRule>>();
		for (ColumnRule cr : this.list) {
			if ("text".equals(cr.getRule_type())) {
				List<ColumnRule> list = null;
				if (map.containsKey(cr.getUrl())) {
					list = map.get(cr.getUrl());
					list.add(cr);
				} else {
					list = new ArrayList<ColumnRule>();
					list.add(cr);
				}
				map.put(cr.getUrl(), list);
			}
		}
		return map;
	}

	public List<ColumnRule> getList() {
		return list;
	}

	public boolean contains(ColumnRule cr) {
		return list.contains(cr);
	}

	public List<ColumnRule> getOCRColumnRule() {
		List<ColumnRule> list = new ArrayList<ColumnRule>();
		for (ColumnRule cr : this.list) {
			if (cr.getOcr_column() != null) {
				list.add(cr);
			}
		}
		return list;
	}

	public List<ColumnRule> getFileColumnRule() {
		List<ColumnRule> list = new ArrayList<ColumnRule>();
		for (ColumnRule cr : this.list) {
			if (cr.getFile_column() != null) {
				list.add(cr);
			}
		}
		return list;
	}
}
