package com.dabao.spider.log;

import org.apache.log4j.Logger;

public class WarnLog {
	public static Logger logger = Logger.getLogger("warn");

	public static void logger(String info) {
		logger.warn(info);
	}
}