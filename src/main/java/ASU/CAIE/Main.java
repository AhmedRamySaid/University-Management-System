package ASU.CAIE;

import ASU.CAIE.GUI.UMSPortal;
import ASU.CAIE.Database.DatabaseManager;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
	public static void main(String[] args) {
		// DB init: test connection on start (only if not in mock mode)
		if (!ASU.CAIE.config.AppConfig.USE_MOCK_DATA) {
			try (Connection conn = DatabaseManager.getConnection()) {
				if (conn != null) {
					System.out.println("Database connection established successfully.");
				}
			} catch (SQLException e) {
				System.err.println("Failed to connect to database on startup: " + e.getMessage());
			}
		} else {
			System.out.println("Running in MOCK MODE - Database connection skipped.");
		}

		// Launch login scene
		UMSPortal.launch(UMSPortal.class, args);
	}
}