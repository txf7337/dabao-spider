package com.dabao.spider.bean;

import sun.misc.BASE64Encoder;

public class Proxy {
	private String host;
	private int port;
	private String auth;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String user, String pwd) {
		this.auth = "Basic "
				+ new BASE64Encoder().encode((user + ":" + pwd).getBytes());
	}

	@Override
	public String toString() {
		return this.auth + ":" + this.port;
	}
}
