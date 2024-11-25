package org.test.doc;

import com.farm.doc.util.MailSender;
import com.farm.doc.util.MailSenderConf;

public class SendMail {
	public static void main(String[] args) {
		MailSender mailsender = new MailSender(new MailSenderConf("smtp.ym.163.com", "wangd@sdkeji.com", "23485815"));
		mailsender.setFrom("wangd@sdkeji.com");
		mailsender.setSubject("asdfasdf");
		mailsender.setBody("aasdfasdfasdf");
		mailsender.setTo("wangd@sdkeji.com");
		mailsender.setNeedAuth(true);
		boolean b = mailsender.setOut();
		if (b) {
			System.out.println("\n邮件发送成功!!!!!");
		} else {
			System.out.println("邮件发送失败!!!!");
		}
	}
}
