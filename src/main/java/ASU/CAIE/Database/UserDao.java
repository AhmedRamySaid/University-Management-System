package ASU.CAIE.Database;

import ASU.CAIE.Users.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDao {
	public boolean createUser(User user) {
		// tell Postgres to cast that string into ENUM type.
		String sql = "INSERT INTO users (name, email, password_hash, role) VALUES (?, ?, ?, ?::user_role)";

		// Using try-with-resources ensures the connection closes automatically
		try (Connection conn = DatabaseManager.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, user.getName());
			pstmt.setString(2, user.getEmail());

			String dummyHashedPassword = hashPassword(user.getPassword());
			pstmt.setString(3, dummyHashedPassword);

			// sends the enum as a lowercase string
			pstmt.setString(4, user.getRole().name().toLowerCase());

			int rowsInserted = pstmt.executeUpdate();
			return rowsInserted > 0;

		} catch (SQLException e) {
			System.err.println("Database error: " + e.getMessage());
			return false;
		}
	}

	// todo: Implement hashing logic
	private String hashPassword(String plainText) {
		return plainText + "_hashed_safely";
	}
}
