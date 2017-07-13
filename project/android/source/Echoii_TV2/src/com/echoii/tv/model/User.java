package com.echoii.tv.model;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>用户封装类</p>
 *
 */
public class User {
	
	private String id;
	private String username;
	private String password;
	
	public User() {
		super();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	

}
