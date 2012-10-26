package com.dabao.spider;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Spider {
	public static ApplicationContext ac;

	public static void main(String[] args) throws Exception {
		ac = new ClassPathXmlApplicationContext("applicationContext.xml");
	}
}