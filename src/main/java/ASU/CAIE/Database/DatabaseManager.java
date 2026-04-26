package ASU.CAIE.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseManager {
	private static final Dotenv dotenv = Dotenv.load();

	private static final String URL = "jdbc:postgresql://" +
			dotenv.get("PGHOST") + dotenv.get("PGDATABASE");
	private static final String USER = dotenv.get("PGUSER");
	private static final String PASSWORD = dotenv.get("PGPASSWORD");

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
}
