package com.echoii.cloud.platform.manager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.echoii.cloud.platform.model.User;
import com.echoii.cloud.platform.service.EmailService;
import com.echoii.cloud.platform.service.UserService;
import com.echoii.cloud.platform.service.impl.EmailServiceImpl;
import com.echoii.cloud.platform.service.impl.UserServiceImpl;
import com.echoii.cloud.platform.util.CommUtil;
import com.echoii.cloud.platform.util.Config;
import com.echoii.cloud.platform.util.DateUtil;
import com.echoii.cloud.platform.util.HashUtil;
import com.echoii.cloud.platform.util.Status;

public class EmailManager {

	public static Logger log = Logger.getLogger(EmailManager.class);
	private static volatile EmailManager MANAGER = null;
	private EmailService emailService = null;
	private UserService userService = null;
	private Config config = null;
	private DocumentBuilderFactory dbFactory = null;
	private DocumentBuilder dbBuilder = null;
	private Document doc = null;
	private String extPath = null;
	private String inPath = null;
	private static final String EXT_EMAIL_PATH = "ext.config.email.path";
	private static final String DEF_EXT_EMAIL_PATH = "/data/echoii/config/email_forget_pw.xml";
	private static final String IN_EMAIL_PATH = "in.config.email.path";
	private static final String DEF_IN_EMAIL_PATH = "email_forget_pw.xml";
	private static final long MAX_TIME_DIFF_MILLI = 600000; // 10*60*1000(ms)
	// private String extPath1 = null;
	// private String inPath1 = null;

	public static EmailManager getInstance() {
		if (MANAGER == null) {
			synchronized (EmailManager.class) {
				if (MANAGER == null) {
					MANAGER = new EmailManager();
				}
			}
		}
		return MANAGER;
	}

	private EmailManager() {
		emailService = EmailServiceImpl.getInstance();
		userService = UserServiceImpl.getInstance();
		config = Config.getInstance();
		extPath = config.getStringValue(EXT_EMAIL_PATH, DEF_EXT_EMAIL_PATH);
		inPath = config.getStringValue(IN_EMAIL_PATH, DEF_IN_EMAIL_PATH);
		// extPath1 = config.getStringValue("ext.config.email.path1",
		// "/data/echoii/config/reset.email.xml");
		// inPath1 = config.getStringValue("in.config.email.path1",
		// "src/main/resources/reset.email.xml");
	}

	public String sendValidEmail(String email) throws IOException,
			ParserConfigurationException, SAXException {
		if ( email == null || !CommUtil.isMail(email) ) {
			log.debug("the email is invalid");
			return Status.NOT_FOUND;
		}
		log.debug("email = " + email );
		User user = userService.getUserByMail(email);
		if (user == null) {
			log.debug("user does not exist");
			return Status.NOT_FOUND;
		}
		log.debug("user id = " + user.getId());

		final String pwValidCode = user.getPwValidCode();
		final Date validCodePeriod = user.getValidCodePeriod();
		if (pwValidCode == null || pwValidCode.isEmpty()
				|| validCodePeriod == null) {
			log.debug("the password valid code or period is null");
			user.setPwValidCode(HashUtil.getRandomId());
			user.setValidCodePeriod(new Date());
			userService.updateUser(user);
		} else {
			long timeDiff = DateUtil.getTimeDiff(user.getValidCodePeriod(),
					new Date());
			// void the email bombing
			if (timeDiff >= MAX_TIME_DIFF_MILLI) {
				log.debug("send email safely");
				user.setPwValidCode(HashUtil.getRandomId());
				user.setValidCodePeriod(new Date());
				userService.updateUser(user);
			} else {
				log.debug("send email much to frequently, can not send email,wait for a moment!");
				return Status.AUTH_ERROR;
			}
		}

		dbFactory = DocumentBuilderFactory.newInstance();
		dbBuilder = dbFactory.newDocumentBuilder();
		File extConfig = new File(extPath);
		InputStream inConfig = this.getClass().getClassLoader()
				.getResourceAsStream(inPath);
		if (extConfig.canRead()) {
			//use external config file
			log.debug("use external email.xml : " + extConfig.getAbsolutePath());
			doc = dbBuilder.parse(extConfig);
		}else{
			//use internal config file
			doc = dbBuilder.parse(inConfig);
		}

		String link = this.config.getStringValue("mail.server.location",
				"http://210.75.252.180:7080")
				+ "/echoii/reset_password.jsp?email="
				+ user.getEmail() + "&pw_valid_code=" + user.getPwValidCode();
		log.debug("link = " + link);

		NodeList nlist = doc.getElementsByTagName("content");
		Node node = nlist.item(0);
		Element element = (Element) node;
		String title = element.getElementsByTagName("title").item(0)
				.getChildNodes().item(0).getNodeValue();
		String link1 = element.getElementsByTagName("link").item(0)
				.getChildNodes().item(0).getNodeValue();
		String content = title + "\n" + link1;
		log.debug("content = " + content);
		content = content.replace("{url}", link);
		log.debug("content = " + content);

		String subject = doc.getElementsByTagName("subject").item(0)
				.getFirstChild().getNodeValue();
		log.debug("subject = " + subject);

		emailService.sendEmail(email, subject, content);

		return Status.OK;
	}

	
	public void sendResetPwdEmail(User user)
			throws ParserConfigurationException, SAXException, IOException {
		// TODO Auto-generated method stub
		dbFactory = DocumentBuilderFactory.newInstance();
		dbBuilder = dbFactory.newDocumentBuilder();
		// log.debug("extPath1 = "+extPath1);
		// log.debug("inPath1 = " + inPath1);
		File extConfig = new File(extPath);
		File inConfig = new File(inPath);
		if (extConfig.canRead()) {
			log.debug("use external email.xml : " + extConfig.getAbsolutePath());
			doc = dbBuilder.parse(extConfig);
		} else if (inConfig.canRead()) {
			log.debug("user internal email.xml : " + inConfig.getAbsolutePath());
			doc = dbBuilder.parse(inConfig);
		}

		String email = user.getEmail();
		log.debug("email = " + email);

		String link = this.config.getStringValue("mail.server.location",
				"localhost:8080")
				+ "/echoii/reset_password.html?email="
				+ user.getEmail() + "&pw_valid_code=" + user.getPwValidCode();
		log.debug("link = " + link);

		NodeList nlist = doc.getElementsByTagName("getbackcontent");
		// log.debug("nlist.length() = " + nlist.getLength() );
		Node node = nlist.item(0);
		Element element = (Element) node;
		String title = element.getElementsByTagName("title").item(0)
				.getChildNodes().item(0).getNodeValue();
		String link1 = element.getElementsByTagName("link").item(0)
				.getChildNodes().item(0).getNodeValue();
		String content = title + "\n" + link1;
		log.debug("content = " + content);

		String subject = doc.getElementsByTagName("getbacksubject").item(0)
				.getFirstChild().getNodeValue();
		log.debug("subject = " + subject);

		emailService.sendEmail(email, subject, content);

	}
	
	public String ResetPassword(String email, String password ,String pwValidCode){
		
		if ( email == null || !CommUtil.isMail(email) ) {
			log.debug("the email is invalid");
			return Status.NOT_FOUND;
		}
		log.debug("email = " + email );
		
		User user = userService.getUserByMail(email);
		if (user == null) {
			log.debug("user does not exist");
			return Status.NOT_FOUND;
		}
		log.debug("user id = " + user.getId());
		
		if( !pwValidCode.equals( user.getPwValidCode() ) ){
			log.debug("pwValidCode = "+pwValidCode+" user.getPwValideCode : "+user.getPwValidCode() );
			log.debug("auth error");
			return  Status.AUTH_ERROR;
		}else{
			long timeDiff = DateUtil.getTimeDiff(user.getValidCodePeriod(),
					new Date());
			if (timeDiff <= MAX_TIME_DIFF_MILLI) {
				log.debug("reset the password safely");
				//user.setPwValidCode(HashUtil.getRandomId());
				user.setPassword(password);
				user.setValidCodePeriod(new Date());
				userService.updateUser(user);
			} else {
				log.debug("Time has passed , can not reset the password");
				return Status.AUTH_ERROR;
			}
		}
		
		return Status.OK;
	}
	
}
