package com.echoii.cloud.platform.service.impl;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.log4j.Logger;
import com.echoii.cloud.platform.service.EmailService;
import com.echoii.cloud.platform.util.Config;


public class EmailServiceImpl implements EmailService{
	
	private Properties props;
	private Session mySession;
	private Config config;
	public static Logger log = Logger.getLogger(EmailServiceImpl.class);
	public static volatile EmailService SERVICE = null ;
	
	private EmailServiceImpl(){
		this.config =  Config.getInstance();
		this.props = new Properties();
		this.props.put("mail.smtp.host", 
				config.getStringValue("mail.smtp.host", "smtp.cstnet.cn"));
		this.props.put("mail.smtp.auth", 
				config.getStringValue("mail.smtp.auth", "true"));
	}
	
	public static EmailService getInstance() {
		if (SERVICE == null) {
			synchronized (EmailServiceImpl.class) {
				// when more than two threads run into the first null check same
				// time, to avoid instanced more than one time, it needs to be
				// checked again.
				if (SERVICE == null) {
					SERVICE = new EmailServiceImpl();
				}
			}
		}
		return SERVICE;
	}

	
	@Override
	public void sendEmail(String addr, String subject, String content) {
		// TODO Auto-generated method stub
		mySession = Session.getDefaultInstance(props, new Authenticator(){
			public PasswordAuthentication getPasswordAuthentication(){
				return new PasswordAuthentication(config.getStringValue("mail.username", "username"),
						config.getStringValue("mail.password", "password"));
			}
		});
		
		mySession.setDebug(true);
		MimeMessage message = new MimeMessage(mySession);
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		
		try{
			messageBodyPart.setContent(content,"text/html;charset=utf8");
			MimeMultipart multpart = new MimeMultipart("related");
			multpart.addBodyPart(messageBodyPart);
			
			message.setFrom(new InternetAddress(this.config.getStringValue("mail.username", "username")));
			message.setRecipient(Message.RecipientType.TO,new InternetAddress(addr));
			message.setSubject(subject);
			message.setContent(multpart);
			
			Transport.send(message);
			log.debug("the email has been sent!");
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}
}
