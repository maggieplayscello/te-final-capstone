package com.techelevator.model;

import javax.sql.DataSource;

import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.model.User;
import com.techelevator.security.PasswordHasher;

@Component
public class JDBCUserDAO implements UserDAO {

	private JdbcTemplate jdbcTemplate;
	private PasswordHasher hashMaster;

	@Autowired
	public JDBCUserDAO(DataSource dataSource, PasswordHasher hashMaster) {
//	public JDBCUserDAO(DataSource dataSource) {	
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.hashMaster = hashMaster;
	}
	
	@Override
	public void saveUser(String userName, String password) {
		byte[] salt = hashMaster.generateRandomSalt();
		String hashedPassword = hashMaster.computeHash(password, salt);
		String saltString = new String(Base64.encode(salt));
		
//		String saltString = "temp";
		jdbcTemplate.update("INSERT INTO app_user(user_name, password, role, salt) VALUES (?, ?, ?, ?)",
				userName, hashedPassword, "Golfer", saltString);
//				userName, password, "Golfer", saltString);
	}

	@Override
	public boolean searchForUsernameAndPassword(String userName, String password) {
		String sqlSearchForUser = "SELECT * "+
							      "FROM app_user "+
							      "WHERE user_name = ? ";
		
		SqlRowSet user = jdbcTemplate.queryForRowSet(sqlSearchForUser, userName);
		if(user.next()) {
			
//			return password.equals(password);
			
			String dbSalt = user.getString("salt");
			String dbHashedPassword = user.getString("password");
			String givenPassword = hashMaster.computeHash(password, Base64.decode(dbSalt));
			return dbHashedPassword.equals(givenPassword);
		} else {
			return false;
		}
	}
	
	
	
// UPDATE PASSWORD - ask for user to enter login credentials userName password
// verify the user, then ask for newPassword, confirm newPassword
// check that the newPassword meets the criteria, execute the change
	
	@Override
	public void updatePassword(String userName, String password, String newPassword) {
		
		boolean isTrue = searchForUsernameAndPassword(userName, password); //provides boolean
		System.out.println("Old PW = " + password + "  newPassword = " + newPassword);
		String sqlSearchForUser = "SELECT * "+
			      "FROM app_user "+
			      "WHERE user_name = ? ";
		SqlRowSet user = jdbcTemplate.queryForRowSet(sqlSearchForUser, userName); // provides user
		
		byte[] salt = hashMaster.generateRandomSalt();
		String hashedPassword = hashMaster.computeHash(newPassword, salt);
		String saltString = new String(Base64.encode(salt));
		
		if(isTrue && user.next()) {
			System.out.println("User Name = " + userName);
			System.out.println("Salt: " + salt);
			System.out.println("Hashed Password: " + hashedPassword);
			System.out.println("Salt String: " + saltString);			
		jdbcTemplate.update("UPDATE app_user SET password = ?, salt = ? WHERE user_name = ?", hashedPassword, saltString, userName);
		
		
//		return dbHashedPassword.equals(givenPassword);
		}				
	}

	@Override
	public Object getUserByUserName(String userName) {
		String sqlSearchForUsername ="SELECT * "+
		"FROM app_user "+
		"WHERE UPPER(user_name) = ? ";

		SqlRowSet user = jdbcTemplate.queryForRowSet(sqlSearchForUsername, userName.toUpperCase()); 
		User thisUser = null;
		if(user.next()) {
			thisUser = new User();
			thisUser.setUserName(user.getString("user_name"));
			thisUser.setPassword(user.getString("password"));
		}

		return thisUser;
	}
	
	@Override
	public String getRoleByUserName(String userName) {
		String sqlSearchForUsername ="SELECT role "+
		"FROM app_user "+
		"WHERE UPPER(user_name) = ? ";

		SqlRowSet user = jdbcTemplate.queryForRowSet(sqlSearchForUsername, userName.toUpperCase()); 
		String myRole = null;
		if(user.next()) {
			myRole = user.getString("role");
		}

		return myRole;
	}
	
	@Override
	public void updateRole(String userName, String role) {
		String sqlUpdateRole = "UPDATE app_user SET role = ? WHERE user_name = ?";
		jdbcTemplate.update(sqlUpdateRole, role, userName);
						
	}
	

}
