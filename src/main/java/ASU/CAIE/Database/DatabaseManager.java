package ASU.CAIE.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import ASU.CAIE.Users.User;
import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseManager {
	private static final Dotenv dotenv = Dotenv.load();

	private static final String URL = "jdbc:postgresql://" +
			dotenv.get("PGHOST") + ":5432/" + dotenv.get("PGDATABASE");
	private static final String USER = dotenv.get("PGUSER");
	private static final String PASSWORD = dotenv.get("PGPASSWORD");

	public static User CurrentUser;
	public static final UserDao UserDaoInstance = new UserDao();

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
}
