package com.farm.doc.util;

import com.farm.core.ParameterService;
import com.farm.parameter.FarmParameterService;

public class MailSenderConf {
	private String stmp;
	private String username;
	private String password;
	private boolean isEnable = true;

	public MailSenderConf(String stmp, String username, String password) {
		this.stmp = stmp;
		this.username = username;
		this.password = password;
	}

	public static MailSenderConf getBaseConf() {
		ParameterService confServer = FarmParameterService.getInstance();
		MailSenderConf conf = new MailSenderConf(confServer.getParameter("config.mail.stmp"),
				confServer.getParameter("config.mail.username"), confServer.getParameter("config.mail.password"));
		conf.isEnable = confServer.getParameter("config.mail.enable").toUpperCase().equals("TRUE");
		return conf;
	}

	public String getStmp() {
		return stmp;
	}

	public void setStmp(String stmp) {
		this.stmp = stmp;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnable() {
		return isEnable;
	}

	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}

}
