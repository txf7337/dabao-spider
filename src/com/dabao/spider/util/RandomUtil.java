package com.dabao.spider.util;

import java.util.Random;

public class RandomUtil {
	public static final String NUMBER = "0123456789";

	public static String random(int num) {
		StringBuffer sb = new StringBuffer();
		int length = NUMBER.length() - 1;
		for (int i = 0; i < num; i++) {
			sb.append(NUMBER.charAt(new Random().nextInt(length)));
		}
		return sb.toString();
	}
}
