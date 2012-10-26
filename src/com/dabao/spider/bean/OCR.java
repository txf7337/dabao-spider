package com.dabao.spider.bean;

public class OCR {
	private String table;
	private int id;
	private String ocr_column;
	private String column;
	private String value;
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

	public String getOcr_column() {
		return ocr_column;
	}

	public void setOcr_column(String ocr_column) {
		this.ocr_column = ocr_column;
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

	public int getFail_num() {
		return fail_num;
	}

	public void setFail_num(int fail_num) {
		this.fail_num = fail_num;
	}

	@Override
	public String toString() {
		return table + "-" + id + "-" + ocr_column;
	}
}
