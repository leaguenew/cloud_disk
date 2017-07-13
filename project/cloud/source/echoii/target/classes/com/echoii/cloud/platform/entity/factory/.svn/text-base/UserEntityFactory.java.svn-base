package com.echoii.cloud.platform.entity.factory;

import java.util.ArrayList;
import java.util.List;

import com.echoii.cloud.platform.entity.UserDetailEntity;
import com.echoii.cloud.platform.entity.UserEntity;
import com.echoii.cloud.platform.entity.UserLoginEntity;
import com.echoii.cloud.platform.model.User;
import com.echoii.cloud.platform.model.UserDetail;
import com.echoii.cloud.platform.util.CommUtil;
import com.echoii.cloud.platform.util.DateUtil;

public class UserEntityFactory {
	
	public final static UserEntity getUserEntity( User user ){
		if( user == null ){
			return null;
		}
		
		UserEntity ue = new UserEntity();
		ue.setId( user.getId() );
		ue.setCreateDate( DateUtil.parseDateToString( 
				user.getCreateDate(), 
				DateUtil.PATTERN_yyyy_MM_dd_HHmmss ) );
		ue.setEmail( user.getEmail() );
		ue.setName(user.getName());
		ue.setLastLoginDate(DateUtil.parseDateToString( 
				user.getLastLoginDate(), 
				DateUtil.PATTERN_yyyy_MM_dd_HHmmss));
		ue.setNickName(user.getNickName());
		
		return ue;
		
	}
	
	public final static UserLoginEntity getUserLoginEntity( User user ){
		if( user == null ){
			return null;
		}
		
		UserLoginEntity ue = new UserLoginEntity();
		ue.setId( user.getId() );
		ue.setCreateDate( DateUtil.parseDateToString( 
				user.getCreateDate(), 
				DateUtil.PATTERN_yyyy_MM_dd_HHmmss) );
		ue.setEmail( user.getEmail() );
		ue.setName(user.getName());
		ue.setLastLoginDate(DateUtil.parseDateToString( 
				user.getLastLoginDate(), 
				DateUtil.PATTERN_yyyy_MM_dd_HHmmss));
		ue.setNickName(user.getNickName());
		
		return ue;
		
	}
	
	public static UserDetailEntity getUserDetailEntity(UserDetail userdetail, User user){
		
		if(userdetail == null){
			return null;
		}
		
		UserDetailEntity ud = new UserDetailEntity();
		ud.setId(user.getId());
		ud.setBirthday(userdetail.getBirthday());
		ud.setBloodType(userdetail.getBloodType());
		ud.setConstellation(userdetail.getConstellation());
		ud.setCreateDate(DateUtil.parseDateToString( 
				userdetail.getCreateDate(), 
				DateUtil.PATTERN_yyyy_MM_dd_HHmmss ));
		ud.setDocNumber(String.valueOf(userdetail.getDocNumber()));
		ud.setGender(userdetail.getGender());
		ud.setHeigth(String.valueOf(userdetail.getHeigth()));

		ud.setJob(userdetail.getJob());
		ud.setMusicNumber(String.valueOf(userdetail.getMusicNumber()));
		ud.setOthersNumber(String.valueOf(userdetail.getOthersNumber()));
		ud.setPictureNumber(String.valueOf(userdetail.getPictureNumber()));
		ud.setQQ(userdetail.getQQ());
		ud.setSize(CommUtil.formatSizeDisp(userdetail.getSize()));
		ud.setTel(userdetail.getTel());
		ud.setUserId(user.getId());
		ud.setVideoNumber(String.valueOf(userdetail.getVideoNumber()));
		ud.setPercents(String.valueOf(userdetail.getPercents()));
		ud.setCapability(CommUtil.formatSizeDisp(userdetail.getCapability()));
		
		ud.setLastLoginDate(DateUtil.parseDateToString( 
				user.getLastLoginDate(), 
				DateUtil.PATTERN_yyyy_MM_dd_HHmmss));
		ud.setEmail(user.getEmail());
		ud.setNickName(user.getNickName());
		ud.setName(user.getName());
		ud.setIdCard(user.getIdCard());
		ud.setIntroduction(userdetail.getIntroduction());
	
		
		return ud;
		
	}
	
    public static UserDetailEntity getUserDetailEntity(UserDetail userdetail){
		
		if(userdetail == null){
			return null;
		}
	
		UserDetailEntity ud = new UserDetailEntity();
		ud.setBirthday(userdetail.getBirthday());
		ud.setBloodType(userdetail.getBloodType());
		ud.setConstellation(userdetail.getConstellation());
		ud.setCreateDate(DateUtil.parseDateToString( 
				userdetail.getCreateDate(), 
				DateUtil.PATTERN_yyyy_MM_dd_HHmmss ));
		ud.setDocNumber(String.valueOf(userdetail.getDocNumber()));
		ud.setGender(userdetail.getGender());
		ud.setHeigth(String.valueOf(userdetail.getHeigth()));
		ud.setId(userdetail.getId());
		ud.setJob(userdetail.getJob());
		ud.setMusicNumber(String.valueOf(userdetail.getMusicNumber()));
		ud.setOthersNumber(String.valueOf(userdetail.getOthersNumber()));
		ud.setPictureNumber(String.valueOf(userdetail.getPictureNumber()));
		ud.setQQ(userdetail.getQQ());
		ud.setSize(CommUtil.formatSizeDisp(userdetail.getSize()));
		ud.setTel(userdetail.getTel());
		ud.setUserId(userdetail.getUserid());
		ud.setVideoNumber(String.valueOf(userdetail.getVideoNumber()));
		ud.setPercents(String.valueOf(userdetail.getPercents()));
		ud.setCapability(CommUtil.formatSizeDisp(userdetail.getCapability()));
		ud.setIntroduction(userdetail.getIntroduction());
		
		return ud;
		
	}
    
    public final static List<UserEntity> listUserEntity( List<User> users ){
		if( users == null || users.size() == 0 ){
			return null;
		}
		List<UserEntity> ues = new ArrayList<UserEntity>();
		for(int i = 0;  i < users.size(); ++i){
			ues.add(getUserEntity(users.get(i)));
		}
	
		return ues;
		
	}
}
