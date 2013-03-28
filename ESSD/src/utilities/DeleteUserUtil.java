package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jasypt.util.password.BasicPasswordEncryptor;

public class DeleteUserUtil {
	
	public static void main (String args []) {
		BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
		Connection conn = JdbcUtil.startConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String username = "", password = "", answer = "";
		int userId = 0;
		boolean invalid = true;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			do {
				do {
					System.out.println("Enter Username: ");
					username = br.readLine();
				} while (username.equals(""));
				
				do {
					System.out.println("Enter Password (must be at least 6 characters): ");
					password = br.readLine();
				} while (password.equals("") || password.length() < 6);
			
				ps = conn.prepareStatement("SELECT user_id, password FROM user WHERE username = ?");
					ps.setString(1, username);
				rs = ps.executeQuery();
					
				if (rs.next() && passwordEncryptor.checkPassword(password, rs.getString(2))) {
					invalid = false;
					userId = rs.getInt(1);
				}
				
				if (invalid)
					System.out.println("Not a valid user!");
			} while (invalid);
			
			System.out.println(username + " " + password);
			do {
				System.out.println("ARE YOU SURE YOU WANT TO DELETE USER (Y/N)? ");
				answer = br.readLine();
				
				if (answer.equalsIgnoreCase("Y")) {
					ps = conn.prepareStatement("DELETE FROM user WHERE user_id = ?");
						ps.setInt(1, userId);
					ps.executeUpdate();
					
					System.out.println("USER WAS SUCCESSFULLY DELETED!");
				} else if (answer.equalsIgnoreCase("N")) {
					break;
				} else {
					answer = "";
				}				
			} while (answer.equals(""));		
		} catch (IOException e) {
			e.printStackTrace();
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
}
