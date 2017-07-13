package com.echoii.cloud.platform.manager;

import java.util.Date;

import net.sf.json.JSONObject;

import com.echoii.cloud.platform.entity.UserEntity;
import com.echoii.cloud.platform.entity.UserLoginEntity;
import com.echoii.cloud.platform.entity.factory.UserEntityFactory;
import com.echoii.cloud.platform.model.User;
import com.echoii.cloud.platform.model.UserDetail;
import com.echoii.cloud.platform.service.CacheService;
import com.echoii.cloud.platform.service.GroupService;
import com.echoii.cloud.platform.service.StatisticService;
import com.echoii.cloud.platform.service.UserService;
import com.echoii.cloud.platform.service.impl.CacheServiceMapImpl;
import com.echoii.cloud.platform.service.impl.CacheServiceRedisImpl;
import com.echoii.cloud.platform.service.impl.GroupServiceImpl;
import com.echoii.cloud.platform.service.impl.StatisticServiceImpl;
import com.echoii.cloud.platform.service.impl.UserServiceImpl;
import com.echoii.cloud.platform.util.Config;
import com.echoii.cloud.platform.util.HashUtil;

public class AuthManager {

	private static volatile AuthManager MANAGER = null;
	private  Config config = Config.getInstance();
	private UserService userService;
	private StatisticService statisticService;
	private CacheService cacheservice;

	public static AuthManager getInstance() {
		if (MANAGER == null) {
			synchronized (AuthManager.class) {
				// when more than two threads run into the first null check same
				// time, to avoid instanced more than one time, it needs to be
				// checked again.
				if (MANAGER == null) {
					MANAGER = new AuthManager();
				}
			}
		}
		return MANAGER;
	}

	AuthManager() {
		userService = UserServiceImpl.getInstance();
		statisticService = StatisticServiceImpl.getInstance();
		if(config.getStringValue("cache.impl.method", "map").equals("map")){
			cacheservice = CacheServiceMapImpl.getInstance();
		}else{
			cacheservice = CacheServiceRedisImpl.getInstance();
			System.out.println("we are using redis");
		}
		
	}

	public UserEntity register(String email, String passwordhash) {
		
		User user = userService.getUserByMail(email); 
		
		// if the dataset has exit the email return null
		if(user != null) { //has exit
			return null;
		}

		// create user;
		user = new User();
		user.setId(HashUtil.strHash(HashUtil.getRandomId())); // the key
		user.setEmail(email);

		user.setCreateDate(new Date());
		user.setPassword(passwordhash); // the password real
		userService.createUser(user);

		// create userdetail
		UserDetail userdetail = new UserDetail();
		userdetail.setId(HashUtil.getRandomId());
		userdetail.setUserid(user.getId());
		userService.createUserDetail(userdetail);


		return UserEntityFactory.getUserEntity(user);
			
		
	}
	
    public UserEntity mregister(String email, String password) {
		
		User user = userService.getUserByMail(email); 
		
		if(user != null){
			return null;
		}

		user = new User();
		user.setId(HashUtil.getRandomId()); // the key
		user.setEmail(email);

		user.setCreateDate(new Date());
		user.setPassword(HashUtil.strHash(password)); // the password real
		userService.createUser(user);
		
		UserDetail userdetail = new UserDetail();
		userdetail.setId(HashUtil.getRandomId());
		userdetail.setUserid(user.getId());
		userService.createUserDetail(userdetail);
		
		return UserEntityFactory.getUserEntity(user);
			
		
	}


	public String pre_login(String email) {

		cacheservice.setIdcode(email, HashUtil.getPreTokenId(), 10000);
		
		JSONObject pre_login_token = new JSONObject();
		pre_login_token.put("pre_login_token", cacheservice.getIdcode(email));
	

		return cacheservice.getIdcode(email);
	}

	public UserLoginEntity login(String email, String passwordhash,String pre_login_token, String ip) {

		// validate the idcode
		if (!cacheservice.getIdcode(email).toLowerCase().equals(pre_login_token.toLowerCase())) {
			return null;
		}

		User user = userService.getUserByMail(email);
		if(user == null ){
			return null;
		}
		
		String hashpass = HashUtil.strHash(user.getPassword() + cacheservice.getIdcode(email));
		
		if(!hashpass.equals(passwordhash)){
			return null;
		}

		String token = HashUtil.getRandomId();
		String key = user.getId() + ":" + ip; // (user_id + ip) as the key
		cacheservice.setToken(token, key);

		// convert entity
		UserLoginEntity ue = (UserLoginEntity) UserEntityFactory.getUserLoginEntity(user);

		ue.setCurrentlogindate(new Date());
		ue.setToken(token);

		// update the user
		user.setLastLoginDate(new Date());
		userService.updateUser(user);

		// update userdetail
		String userid = user.getId();
		UserDetail ud = userService.getUserDetailByUserId(userid);
		if(ud == null){
			System.out.println("user >> userDetail not complete!");
		}
	
		System.out.println("userdetail complete!");
		long doc, mus, video, pic, size, other, all;
		doc = statisticService.countDocuments(userid);
		mus = statisticService.countMusics(userid);
		video = statisticService.countVideos(userid);
		pic = statisticService.countPictures(userid);
		size = statisticService.sumAllSize(userid, "size");
		all = statisticService.countAll(userid);
		other = all - doc - mus - video - pic;
		ud.setDocNumber(doc);
		ud.setVideoNumber(video);
		ud.setMusicNumber(mus);
		ud.setSize(size);
		ud.setPictureNumber(pic);
		ud.setOthersNumber(other);
		ud.setPercents((int) (100 * size / ud.getCapability()));

		userService.updateUserDetail(ud);

		return ue;

	}
	
	public UserLoginEntity mlogin(String email, String password,String ip) {

		User user = userService.getUserByMail(email);
		if(user == null )
			return null;
		
		String passwordr = user.getPassword();
		
		String pass = HashUtil.strHash(password);
		
		if (!passwordr.equals(pass)){
			return null;
		}

		String token = HashUtil.getRandomId();
		String key = user.getId() + ":" + ip; // (user_id + ip) as the key
		cacheservice.setToken(token, key);

		// convert entity
		UserLoginEntity ue = (UserLoginEntity) UserEntityFactory
				.getUserLoginEntity(user);
		ue.setCurrentlogindate(new Date());
		ue.setToken(token);

		// update the user
		user.setLastLoginDate(new Date());
		userService.updateUser(user);

		String userid = user.getId();
		UserDetail ud = userService.getUserDetailByUserId(userid);

		if (ud == null) {
			System.out.println("user >> userDetail not complete!");
		}

		long doc, mus, video, pic, size, other, all;
		doc = statisticService.countDocuments(userid);
		mus = statisticService.countMusics(userid);
		video = statisticService.countVideos(userid);
		pic = statisticService.countPictures(userid);
		size = statisticService.sumAllSize(userid, "size");
		all = statisticService.countAll(userid);
		other = all - doc - mus - video - pic;
		ud.setDocNumber(doc);
		ud.setVideoNumber(video);
		ud.setMusicNumber(mus);
		ud.setSize(size);
		ud.setPictureNumber(pic);
		ud.setOthersNumber(other);
		ud.setPercents((int) (100 * size / ud.getCapability()));

		userService.updateUserDetail(ud);
				
	    return ue;
		
	}
	
	public UserEntity check_login(String userid){
		
		User user = userService.getUserById(userid);
		
		if(user == null){
			return null;
		}

		return UserEntityFactory.getUserEntity(user);
		
	}
	
	public void logout(String key,String token){
		cacheservice.delete(key, token);
	}
}
