package model;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/* Global Variables */
	int userId;
	String usId, username, name, password, license, type, user_type;
	
	/*
	 * Constructor
	 */
	public User () {
	}

	/*
	 * Getter for user id
	 */
	public int getUserId() {
		return userId;
	}

	/*
	 * Setter for user id
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsId() {
		return usId;
	}

	public void setUsId(String usId) {
		this.usId = usId;
	}

	/*
	 * Getter for user name
	 */
	public String getName() {
		return name;
	}

	/*
	 * Setter for user name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * Getter for user password
	 */
	public String getPassword() {
		return password;
	}

	/*
	 * Setter for user password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	
	public User clone(User toClone) {
		User user = new User ();
		user.setLicense(toClone.getLicense());
		user.setName(toClone.getName());
		user.setPassword(toClone.getPassword());
		user.setType(toClone.getType());
		user.setUser_type(toClone.getUser_type());
		user.setUserId(toClone.getUserId());
		user.setUsername(toClone.getUsername());
		user.setUsId(toClone.getUsId());
		
		return user;
	}
}
