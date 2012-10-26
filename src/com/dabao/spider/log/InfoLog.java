package com.dabao.spider.log;

import org.apache.log4j.Logger;

public class InfoLog {
	public static Logger logger = Logger.getLogger("info");

	public static void logger(String info) {
		logger.info(info);
	}
}
