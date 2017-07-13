package com.echoii.cloud.platform.service;

import java.util.List;

import com.echoii.cloud.platform.model.User;
import com.echoii.cloud.platform.model.UserDetail;

public interface UserService {
	
	public void createUser(User user);
	
	public void createUserDetail(UserDetail userdetail);

	public void updateUser(User user);
	
	public void updateUserDetail(UserDetail userdetail);

	public User getUserById(String id);

	public User getUserByNameHash(String id);

	public User getUsertByName(String name);

	public User getUserByMail(String email);
	
	public UserDetail getUserDetailByUserId(String userid);
	
	public List<User> listUserByIds(Object[] ids,int begin,int size);
}
