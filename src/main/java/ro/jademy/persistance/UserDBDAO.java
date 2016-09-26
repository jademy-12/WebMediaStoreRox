package ro.jademy.persistance;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.TreeSet;

import ro.jademy.domain.entities.User;
import ro.jademy.domain.entities.UserType;

public class UserDBDAO {

	private Properties importFile;
	private static UserDBDAO soleInstance = new UserDBDAO();

	private UserDBDAO() {
		importFile = new Properties() {
			public synchronized Enumeration<Object> keys() {
				return Collections.enumeration(new TreeSet<Object>(keySet()));
			}
		};
		try {
			FileInputStream stream = new FileInputStream("user.properties");
			importFile.load(stream);
		} catch (IOException e) {
			throw new RuntimeException("Cannot open user.properties", e);
		}
	}

	public static UserDBDAO getInstance() {
		return soleInstance;
	}

	public User getUserByUsername(String username) {
		try (Connection connection = ConnectionManager.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM  USERS WHERE USERNAME = ?");
			statement.setString(1, username);
			ResultSet result = statement.executeQuery();
			if (!result.next()) {
				return null;
			}
			User user = new User();
			user.setPassword(result.getString("password"));
			user.setEmailAddress(result.getString("email"));
			user.setUuid(result.getString("uuid"));
			user.setUsername(result.getString("username"));
			return user;
		} catch (SQLException e) {
			throw new RuntimeException("Cannot connect to database", e);
		}

	}

	public int getNextUserIndex() {
		int i = 0;
		while (true) {
			i++;
			String dbUsername = importFile.getProperty("user[" + i + "].username");
			if (dbUsername == null) {
				return i;
			}
		}
	}

	public void createUser(User user) {
		try (Connection connection = ConnectionManager.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("INSERt INTO USERS (USERNAME, PASSWORD, EMAIL) VALUES (?,?,?)");
			statement.setString(1, user.getUsername());
			statement.setString(2, user.getPassword());
			statement.setString(3, user.getEmailAddress());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Cannot connect to database", e);
		}

	}

	public void updateUser(User user) {
		int i = 0;
		while (true) {
			i++;
			String dbUsername = importFile.getProperty("user[" + i + "].username");
			if (dbUsername == null) {
				return;
			}
			if (dbUsername.equals(user.getUsername())) {
				String s = user.getPassword();
				System.out.println(s);
				importFile.setProperty("user[" + i + "].password", user.getPassword());
				importFile.setProperty("user[" + i + "].emailAddress", user.getEmailAddress());
				importFile.setProperty("user[" + i + "].uuid", user.getUuid());
				importFile.setProperty("user[" + i + "].customer", user.getUserType().name());
				FileOutputStream fos;
				try {
					fos = new FileOutputStream("user.properties");
					importFile.store(fos, new Date().toString());
					fos.close();
				} catch (IOException e) {
					throw new RuntimeException("Cannot save user.properties", e);
				}
			}
		}

	}

	public User getUserByUuid(String uuid) {
		int i = 0;
		while (true) {
			i++;
			String dbUuid = importFile.getProperty("user[" + i + "].uuid");
			if (dbUuid == null) {
				return null;
			}
			if (dbUuid.equals(uuid)) {
				String dbUsername = importFile.getProperty("user[" + i + "].username");
				String dbPassword = importFile.getProperty("user[" + i + "].password");
				String dbEmailAddress = importFile.getProperty("user[" + i + "].emailAddress");
				UserType dbUserType = UserType.valueOf(importFile.getProperty("user[" + i + "].customer"));
				User user = new User(dbUsername, dbPassword, dbEmailAddress, uuid, dbUserType);
				return user;
			}
		}
	}

}