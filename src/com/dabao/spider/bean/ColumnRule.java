package com.dabao.spider.bean;

public class ColumnRule {
	private String name;
	// column的类型,默认是string
	private String type = "java.lang.String";
	private String attr;
	// html,xml,json,js四种类型,默认是html
	private String rule_type = "html";
	private String rule;
	// 异步获取的url
	private String url;
	// xpath或者query匹配后获得的都是数组形式的对象,index表示第几个下标是需要的信息,默认是0,-1表示所有的都需要
	private int index;
	private boolean overwrite = false;
	private String file_column;
	private String file_path;
	private String ocr_column;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}

	public String getRule_type() {
		return rule_type;
	}

	public void setRule_type(String rule_type) {
		this.rule_type = rule_type;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isOverwrite() {
		return overwrite;
	}

	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}

	public String getFile_column() {
		return file_column;
	}

	public void setFile_column(String file_column) {
		this.file_column = file_column;
	}

	public String getFile_path() {
		return file_path;
	}

	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}

	public String getOcr_column() {
		return ocr_column;
	}

	public void setOcr_column(String ocr_column) {
		this.ocr_column = ocr_column;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ColumnRule) {
			ColumnRule cr = (ColumnRule) obj;
			return cr.getName().equals(this.name);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
}