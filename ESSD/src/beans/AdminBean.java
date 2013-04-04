package beans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.primefaces.context.RequestContext;

import utilities.JdbcUtil;

import mapping.MapToClass;
import model.User;
import model.UserDataModel;

/**
 * @author Arisa C. Ochavez
 *
 * Backing bean for the administrative functionalities of the application
 */
@ManagedBean
@SessionScoped
public class AdminBean implements Serializable {

	/**
	 * Global variables
	 */
	private static final long serialVersionUID = 1L;
	private User user, toDelete;
	private User selectedUser = new User();
	private List<User> userList;
	private UserDataModel userListDataModel;
	private List<User> filteredUsers;
	private String confirmPass, newPass;
	
	/**
	 * Constructor 
	 */
	public AdminBean () {
		user = new User();
	}

	/**
	 * Getter for variable that holds user information
	 * @return user - information about the logged in user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Setter for variable that holds user information
	 * @param user - information about the logged in user
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * Getter for the selected user
	 * @return selectedUser - information of the user selected by the administrator
	 */
	public User getSelectedUser() {
		return selectedUser;
	}

	/**
	 * Setter for the selected user
	 * @param selectedUser - information of the user selected by the administrator
	 */
	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}

	/**
	 * Getter for the list of users
	 * @return userList - list of users associated with the administrator
	 */
	public List<User> getUserList() {
		return userList;
	}

	/**
	 * Setter for the list of users
	 * @param userList - list of users associated with the administrator
	 */
	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	/**
	 * Getter for the data model of the list of users
	 * @return userListDataModel - data model for the list of users associated with the administrator
	 */
	public UserDataModel getUserListDataModel() {
		return userListDataModel;
	}

	/**
	 * Setter for the data model of the list of users
	 * @param userListDataModel - data model for the list of users associated with the administrator
	 */
	public void setUserListDataModel(UserDataModel userListDataModel) {
		this.userListDataModel = userListDataModel;
	}

	/**
	 * Getter for filtered user list
	 * @return filteredUsers - user list that was trimmed down depending on a particular criteria
	 */
	public List<User> getFilteredUsers() {
		return filteredUsers;
	}

	/**
	 * Setter for filtered user list
	 * @param filteredUsers - user list that was trimmed down depending on a particular criteria
	 */
	public void setFilteredUsers(List<User> filteredUsers) {
		this.filteredUsers = filteredUsers;
	}
	
	/**
	 * Getter for the password entered to confirm deletion of user
	 * @return confirmPass - administrator password to confirm deletion of user
	 */
	public String getConfirmPass() {
		return confirmPass;
	}

	
	/**
	 * Setter for the password entered to confirm deletion of user
	 * @param confirmPass - administrator password to confirm deletion of user
	 */
	public void setConfirmPass(String confirmPass) {
		this.confirmPass = confirmPass;
	}

	/**
	 * Getter for the new password entered for the user
	 * @return newPass - new password entered by the administrator
	 */
	public String getNewPass() {
		return newPass;
	}

	/**
	 * Setter for the new password entered for the user
	 * @param newPass - new password entered by the administrator
	 */
	public void setNewPass(String newPass) {
		this.newPass = newPass;
	}

	/**
	 * Retrieve the list of users associated with the administrator from the database  
	 */
	public void populateUsers () {
		userList = new ArrayList<User>();
		
		Connection conn = JdbcUtil.startConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		User us;
		
		try {
			
			ps = conn.prepareStatement("SELECT * " +
										"FROM user " +
										"WHERE user_id != ? ");
				ps.setInt(1, AccountMgtBean.getUserId());
			rs = ps.executeQuery();
			
			while(rs.next()) {
				us = new User();
				userList.add(MapToClass.mapUser(us, rs));
			}
					
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (ps != null)
					ps.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
		
		userListDataModel = new UserDataModel(userList); //Populate the data model with the list of users
	}
	
	/**
	 * Store the user information to be deleted in another variable 
	 */
	public void populateDeleteUser() {
		toDelete = new User();
		toDelete = toDelete.clone(selectedUser);
	}
	
	/**
	 * Add the new user entered by the administrator to the database 
	 */
	public void addUser () {
		BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
		FacesContext context = FacesContext.getCurrentInstance();
		Connection conn = JdbcUtil.startConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			//Username should be unique
			ps = conn.prepareStatement("SELECT user_id FROM user WHERE username = ?");
				ps.setString(1, user.getUsername());
				
			if (ps.executeQuery().next()) {
				context.addMessage(null, new FacesMessage("Adding New User Failed", "Username should be unique."));
				return;
			}
			
			//License should be unique	
			ps = conn.prepareStatement("SELECT user_id FROM user WHERE license = ?");
				ps.setString(1, user.getLicense());
			
			if (ps.executeQuery().next()) {
				context.addMessage(null, new FacesMessage("Adding New User Failed", "License should be unique."));
				return;
			}
					
			//Insert everything in the database
			ps = conn.prepareStatement("INSERT INTO user " +
										"(username," +
										"password," +
										"type," +
										"user_type," +
										"name," +
										"license," +
										"created_date," +
										"created_by," +
										"last_update_date," +
										"last_update_by)" +
										"VALUES" +
										"(?, ?, ?, ?, ?, ?, SYSDATE(), ?, SYSDATE(), ?)");
				ps.setString(1, user.getUsername());
				ps.setString(2, passwordEncryptor.encryptPassword(user.getPassword()));
				ps.setString(3, user.getType());
				ps.setString(4, user.getUser_type());
				ps.setString(5, user.getName());
				ps.setString(6, user.getLicense());
				ps.setInt(7, AccountMgtBean.getUserId());
				ps.setInt(8, AccountMgtBean.getUserId());				
			ps.executeUpdate();
			
			context.addMessage(null, new FacesMessage("Adding New User Success", "User is now added in the database."));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (ps != null)
					ps.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
	}
	
	/**
	 * Enter the user information modified by the administrator in the database
	 */
	public void editUser () {
		BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
		FacesContext context = FacesContext.getCurrentInstance();
		Connection conn = JdbcUtil.startConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			//Username should be unique
			ps = conn.prepareStatement("SELECT user_id FROM user WHERE username = ? AND user_id != ?");
				ps.setString(1, selectedUser.getUsername());
				ps.setInt(2, selectedUser.getUserId());
				
			if (ps.executeQuery().next()) {
				context.addMessage(null, new FacesMessage("Editing User Failed", "Username already exists."));
				resetValues();
				return;
			}
			
			//License should be unique
			ps = conn.prepareStatement("SELECT user_id FROM user WHERE license = ? AND user_id != ?");
				ps.setString(1, selectedUser.getLicense());
				ps.setInt(2, selectedUser.getUserId());
			
			if (ps.executeQuery().next()) {
				context.addMessage(null, new FacesMessage("Editing User Failed", "License already exists."));
				resetValues();
				return;
			}
			
			//Check if there is no new password
			if (!newPass.equals(""))
				selectedUser.setPassword(passwordEncryptor.encryptPassword(newPass));
			
			//Update into the database
			ps = conn.prepareStatement("UPDATE user SET " +
										"username = ? ," +
										"password = ? ," +
										"type = ? ," +
										"user_type = ? ," +
										"name = ? , " +
										"license = ? , " +
										"last_update_by = ? ," +
										"last_update_date = SYSDATE() " +
										"WHERE user_id = ?");
				ps.setString(1, selectedUser.getUsername());
				ps.setString(2, selectedUser.getPassword());
				ps.setString(3, selectedUser.getType());
				ps.setString(4, selectedUser.getUser_type());
				ps.setString(5, selectedUser.getName());
				ps.setString(6, selectedUser.getLicense());
				ps.setInt(7, AccountMgtBean.getUserId());
				ps.setInt(8, selectedUser.getUserId());
			ps.executeUpdate();
			
			context.addMessage(null, new FacesMessage("Editing User Success", "User information is now modified in the database."));
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (ps != null)
					ps.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		resetValues();		
		RequestContext.getCurrentInstance().execute("editUserInfoDialog.hide()");
	}
	
	/**
	 * Delete the user chosen by the administrator from the database
	 */
	public void deleteUser () {
		BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
		FacesContext context = FacesContext.getCurrentInstance();
		Connection conn = JdbcUtil.startConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement("SELECT user_id, password FROM user WHERE username = ?");
				ps.setString(1, toDelete.getUsername());
			rs = ps.executeQuery();
					
			if (rs.next() && passwordEncryptor.checkPassword(confirmPass, toDelete.getPassword())) {
				ps = conn.prepareStatement("DELETE FROM user WHERE user_id = ?");
					ps.setInt(1, toDelete.getUserId());
				ps.executeUpdate();
				
				context.addMessage(null, new FacesMessage("Deleting User Success", "User is now delete from the database."));
			} else {
				context.addMessage(null, new FacesMessage("Deleting User Failed", "Incorrect password."));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
				if (ps != null)
					ps.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		resetValues();
		RequestContext.getCurrentInstance().execute("deleteUserDialog.hide()");
	}
	
	/**
	 * Reset the variables after deletion or modification then refresh the list of users for any changes
	 */
	private void resetValues() {
		newPass = "";
		confirmPass = "";
		populateUsers();
	}
}
