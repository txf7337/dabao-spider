package com.dabao.spider.log;

import org.apache.log4j.Logger;

public class ErrorLog {
	public static Logger logger = Logger.getLogger("error");

	public static void logger(String info) {
		logger.error(info);
	}
}
