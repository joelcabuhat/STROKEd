package model;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;

/**
 * @author Arisa C. Ochavez
 *
 * Model attributes of users of the application
 */
@ManagedBean
public class User implements Serializable{

	/**
	 * Global variables
	 */
	private static final long serialVersionUID = 1L;
	int userId;
	String usId, username, name, password, license, type, user_type;
	
	/**
	 * Constructor
	 */
	public User () {
	}

	/**
	 * Getter for user id
	 * 
	 * @return userId - id given to the user by the database
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * Setter for user id
	 * 
	 * @param userId - id given to the user by the database
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * Getter for the string equivalent of the user id
	 * @return usId - id given to the user by the database in string format
	 */
	public String getUsId() {
		return usId;
	}

	/**
	 * Setter for the string equivalent of the user id
	 * @param usId - id given to the user by the database in string format
	 */
	public void setUsId(String usId) {
		this.usId = usId;
	}

	/**
	 * Getter for the user's name
	 * @return name - name to identify the user
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter for the user's name
	 * @param name - name to identify the user
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter for the user's password
	 * @return password - string used to authenticate user log in
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Setter for the user's password
	 * @param password - string used to authenticate user log in
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Getter for the user's type
	 * @return type - identify if user is an ADMIN or a USER
	 */
	public String getType() {
		return type;
	}

	/**
	 * Setter for the user's type
	 * @param type - identify if user is an ADMIN or a USER
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Getter for the user's username
	 * @return username - name used by the user to log in the system
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Setter for the user's username
	 * @param username - name used by the user to log in the system
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Getter for the user's license
	 * @return license - identification associated to a user that's a doctor
	 */
	public String getLicense() {
		return license;
	}

	/**
	 * Setter for the user's license
	 * @param license - identification associated to a user that's a doctor
	 */
	public void setLicense(String license) {
		this.license = license;
	}

	/**
	 * Getter for the type of doctor
	 * @return user_type - identifies if the user is a general practitioner or an expert
	 */
	public String getUser_type() {
		return user_type;
	}

	/**
	 * Setter for the type of doctor
	 * @param user_type - identifies if the user is a general practitioner or an expert
	 */
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	
	/**
	 * Copies the details of one user variable to another
	 * 
	 * @param toClone - user variable to be copied
	 * @return user - the new variable where the information has been copied
	 */
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
