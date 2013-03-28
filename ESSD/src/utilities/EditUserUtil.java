package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jasypt.util.password.BasicPasswordEncryptor;

public class EditUserUtil {

	public static void main (String args []) {
		BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
		Connection conn = JdbcUtil.startConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String username = "", password = "", name = "", license = "";
		int type = 0, userId = 0, adminId = 0;
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
			
				ps = conn.prepareStatement("SELECT user_id, password, type FROM user WHERE username = ?");
					ps.setString(1, username);
				rs = ps.executeQuery();
					
				if (rs.next() && passwordEncryptor.checkPassword(password, rs.getString(2))) {
					invalid = false;
					userId = rs.getInt(1);
					type = rs.getInt(3);
				}
				
				if (invalid)
					System.out.println("Not a valid user!");
			} while (invalid);
			
			System.out.println(username + " " + password + " " + type);
			invalid = true;
			
			do {
				//Username should be unique
				System.out.println("Enter New Username: ");
				username = br.readLine();
				ps = conn.prepareStatement("SELECT * FROM user WHERE username = ?");
					ps.setString(1, username);
				
				if (!(ps.executeQuery()).next())
					invalid = false;
			} while (invalid || username.equals(""));
			
			//Enter Password
			do {
				System.out.println("Enter New Password (must be at least 6 characters): ");
				password = br.readLine();
			} while (password.equals("") || password.length() < 6);
			
			//Enter Name
			do {
				System.out.println("Enter Doctor's New Name: ");
				name = br.readLine();
			} while (name.equals(""));
			
			//Enter License
			do {
				System.out.println("Enter New License (exactly 6 characters): ");
				license = br.readLine();
			} while (license.equals("") || license.length() != 6);
			
			//Enter Doctor Type
			do {
				try {
					System.out.println("Enter User Type (0 - GP, 1 - Expert): ");
					type = Integer.parseInt(br.readLine());
				} catch (NumberFormatException e) {
					type = -1;
				}
			} while (type > 1 || type < 0);
			
			//Enter ID of the one inserting the new user
			invalid = true;
			do {
				System.out.println("Enter Your Admin Id: ");
				try {
					adminId = Integer.parseInt(br.readLine());
					invalid = false;
				} catch (NumberFormatException e) {
					invalid = true;
				}
			} while (invalid);
			
			//Update into the database
			ps = conn.prepareStatement("UPDATE user SET " +
										"username = ? ," +
										"password = ? ," +
										"type = ? ," +
										"name = ? , " +
										"license = ? , " +
										"last_update_by = ? ," +
										"last_update_date = SYSDATE() " +
										"WHERE user_id = ?");
				ps.setString(1, username);
				ps.setString(2, passwordEncryptor.encryptPassword(password));
				ps.setInt(3, type);
				ps.setString(4, name);
				ps.setString(5, license);
				ps.setInt(6, adminId);
				ps.setInt(7, userId);
			ps.executeUpdate();
			
			System.out.println("USER SUCCESSFULLY MODIFIED!");
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
