package com.echoii.cloud.platform.service.test;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.echoii.cloud.platform.manager.EmailManager;
import com.echoii.cloud.platform.model.User;
import com.echoii.cloud.platform.service.EmailService;
import com.echoii.cloud.platform.service.UserService;
import com.echoii.cloud.platform.service.impl.EmailServiceImpl;
import com.echoii.cloud.platform.service.impl.UserServiceImpl;

public class EmailServiceTest {
	
	public static EmailManager emailManager =  EmailManager.getInstance();
	public static UserService userService = UserServiceImpl.getInstance();
	
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException{
		String  email = "zedongliguo@163.com";
		emailManager.sendValidEmail(email);
		//emailManager.ResetPassword(email, "81dc9bdb52d04dc20036dbd8313ed055", "364afe45932fce355346c1ac69bbf7a5");
	}
	  
}
