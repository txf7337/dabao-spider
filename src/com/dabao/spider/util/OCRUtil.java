package com.dabao.spider.util;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.dabao.spider.log.WarnLog;

public class OCRUtil {

	public static String recognizeText(String img, String ocr,String ocrData) {
		try {
			Process process = Runtime.getRuntime().exec(
					"tesseract " + img + " " + ocr
							+ Thread.currentThread().getId() + " -l "+ocrData);
			int w = process.waitFor();
			if (w == 0) {
				StringBuffer sb = new StringBuffer();
				Reader reader = new InputStreamReader(new FileInputStream(ocr
						+ Thread.currentThread().getId() + ".txt"));
				int tempchar;
				while ((tempchar = reader.read()) != -1) {
					sb.append((char) tempchar);
				}
				reader.close();
				return sb.toString().replaceAll("\n", "").trim().toLowerCase();
			}
		} catch (Exception e) {
			WarnLog.logger(e.getMessage());
			return null;
		}
		return null;
	}
}