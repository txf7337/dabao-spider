package com.dabao.spider.service;

import com.dabao.spider.bean.Info;
import com.dabao.spider.bean.OCR;
import com.dabao.spider.dao.InfoDao;

public class SpiderOcrService {
	private InfoDao infoDao;

	public Info findOCR(OCR ocr) {
		return infoDao.findInfo(ocr.getTable(), ocr.getId(), new String[] {
				ocr.getColumn(), ocr.getOcr_column() });
	}

	public void saveOCR(OCR ocr) {
		infoDao.saveColumnValue(ocr.getTable(), ocr.getId(), ocr.getColumn(),
				ocr.getValue());
	}

	public void setInfoDao(InfoDao infoDao) {
		this.infoDao = infoDao;
	}
}