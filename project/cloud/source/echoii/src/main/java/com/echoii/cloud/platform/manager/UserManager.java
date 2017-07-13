package com.echoii.cloud.platform.manager;
import com.echoii.cloud.platform.entity.UserDetailEntity;
import com.echoii.cloud.platform.entity.UserEntity;
import com.echoii.cloud.platform.entity.factory.UserEntityFactory;
import com.echoii.cloud.platform.model.User;
import com.echoii.cloud.platform.model.UserDetail;
import com.echoii.cloud.platform.service.StatisticService;
import com.echoii.cloud.platform.service.UserService;
import com.echoii.cloud.platform.service.impl.StatisticServiceImpl;
import com.echoii.cloud.platform.service.impl.UserServiceImpl;

public class UserManager {
	private static volatile UserManager MANAGER;
	private UserService userService;
	private StatisticService statisticService;
	
	public static UserManager getInstance() {
		if (MANAGER == null) {
			synchronized (UserManager.class) {
				// when more than two threads run into the first null check same
				// time, to avoid instanced more than one time, it needs to be
				// checked again.
				if (MANAGER == null) {
					MANAGER = new UserManager();
				}
			}
		}
		return MANAGER;
	}

	UserManager() {
		userService = UserServiceImpl.getInstance();
		statisticService = StatisticServiceImpl.getInstance();
		
	}
	
   public UserDetailEntity userdetail(String userid){
		
		UserDetail userdetail = userService.getUserDetailByUserId(userid);
		User user = userService.getUserById(userid);
		
		if(userdetail == null){
			return null;
		}
	
		return UserEntityFactory.getUserDetailEntity(userdetail,user);
		
		
	}

	public UserDetailEntity update_userdetail(String userid, String idCard,
			String name, String nickName, String gender, String birthday,
			String QQ, String tel, String job, String introduction) {
		
		UserDetail userdetail = userService.getUserDetailByUserId(userid);
		User user = userService.getUserById(userid);
		
		if(userdetail == null || user == null){
			return null;
		}
	
		
		user.setName(name);
		user.setNickName(nickName);
		user.setIdCard(idCard);
		
		userdetail.setGender(gender);
		userdetail.setBirthday(birthday);
		userdetail.setQQ(QQ);
		userdetail.setTel(tel);
		userdetail.setJob(job);
		userdetail.setIntroduction(introduction);
		
		userService.updateUser(user);
		userService.updateUserDetail(userdetail);
		
		return UserEntityFactory.getUserDetailEntity(userdetail, user);
		
		
	}
   
	public UserEntity update_password(String userid, String oldPassword,String password) {
		
		User user = userService.getUserById(userid);
		
		if(user == null || !user.getPassword().equals(oldPassword)){
			return null;
		}
		
		user.setPassword(password);
		userService.updateUser(user);
		return UserEntityFactory.getUserEntity(user);
		
		
	}
    //update the userdetail (upload or delete)
 	public boolean updateStatistics(String userid) {
 		
 		UserDetail ud = userService.getUserDetailByUserId(userid);
 		
 		if(ud == null){
 			return false;
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
		
		return true;
 	}

}
