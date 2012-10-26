package com.dabao.spider;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import com.dabao.spider.bean.Info;
import com.dabao.spider.bean.OCR;
import com.dabao.spider.bean.Proxy;
import com.dabao.spider.log.WarnLog;
import com.dabao.spider.service.SpiderOcrService;
import com.dabao.spider.util.OCRUtil;
import com.dabao.spider.util.URLUtil;

public class OCRSpider {
	private SpiderOcrService soService;
	private final String ocrPath = "./tmp/ocr/";
	private String ocrData = "eng";

	public boolean spider(OCR ocr, Proxy proxy) {
		Info info = soService.findOCR(ocr);
		try {
			String url = URLUtil.makeAbsoluteUrl(info.get("spider_url")
					.toString(), info.get(ocr.getOcr_column()).toString());
			String img = ocrPath + Thread.currentThread().getId()
					+ url.substring(url.lastIndexOf("."), url.length());
			FileUtils.copyURLToFile(new URL(url), new File(img));
			String value = OCRUtil.recognizeText(img, ocrPath, ocrData);
			if (value == null) {
				return false;
			} else {
				ocr.setValue(value);
				soService.saveOCR(ocr);
				return true;
			}
		} catch (Exception e) {
			WarnLog.logger("识别文字" + ocr.toString() + "出现错误!" + e.getMessage());
			return false;
		}
	}

	public void setSoService(SpiderOcrService soService) {
		this.soService = soService;
	}

	public void setOcrData(String ocrData) {
		this.ocrData = ocrData;
	}

}