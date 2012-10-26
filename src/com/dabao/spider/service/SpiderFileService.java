package com.dabao.spider.service;

import com.dabao.spider.bean.Info;
import com.dabao.spider.bean.InfoFile;
import com.dabao.spider.dao.InfoDao;

public class SpiderFileService {
	private InfoDao infoDao;

	public Info findInfo(InfoFile infoFile) {
		return infoDao
				.findInfo(infoFile.getTable(), infoFile.getId(), new String[] {
						infoFile.getColumn(), infoFile.getFile_column() });
	}

	public void saveInfoFile(InfoFile infoFile) {
		infoDao.saveColumnValue(infoFile.getTable(), infoFile.getId(), infoFile
				.getColumn(), infoFile.getValue());
	}

	public void setInfoDao(InfoDao infoDao) {
		this.infoDao = infoDao;
	}

}
