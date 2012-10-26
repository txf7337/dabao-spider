package com.dabao.spider.bean;

public class InfoFile {
	private String table;
	private int id;
	private String file_column;
	private String column;
	private String value;
	private String path;
	private int fail_num;

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFile_column() {
		return file_column;
	}

	public void setFile_column(String file_column) {
		this.file_column = file_column;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getFail_num() {
		return fail_num;
	}

	public void setFail_num(int fail_num) {
		this.fail_num = fail_num;
	}

	@Override
	public String toString() {
		return table + "-" + id + "-" + file_column;
	}
}