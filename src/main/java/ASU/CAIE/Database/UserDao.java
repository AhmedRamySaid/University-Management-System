package ASU.CAIE.Database;

import ASU.CAIE.Users.Role;
import ASU.CAIE.Users.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static ASU.CAIE.Database.PasswordUtils.hashPassword;

public class UserDao {
	
	// From Login Branch
	public boolean createUser(User user) {
		if (ASU.CAIE.config.AppConfig.USE_MOCK_DATA) {
			System.out.println("MOCK MODE: Simulating user registration for " + user.getEmail());
			return true;
		}
		// tell Postgres to cast that string into ENUM type.
		String sql = "INSERT INTO users (name, email, password_hash, role) VALUES (?, ?, ?, ?::user_role)";

		// Using try-with-resources ensures the connection closes automatically
		try (Connection conn = DatabaseManager.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, user.getName());
			pstmt.setString(2, user.getEmail());

			String hashedPassword = hashPassword(user.getPassword());
			pstmt.setString(3, hashedPassword);

			// sends the enum as a lowercase string
			pstmt.setString(4, user.getRole().name().toLowerCase());

			int rowsInserted = pstmt.executeUpdate();
			return rowsInserted > 0;

		} catch (SQLException e) {
			System.err.println("Database error: " + e.getMessage());
			return false;
		}
	}

	// From DB Branch
	public boolean createUser(User user, String password) {
		// tell Postgres to cast that string into ENUM type.
		String sql = "INSERT INTO users (name, email, password_hash, role) VALUES (?, ?, ?, ?::user_role)";

		// Using try-with-resources ensures the connection closes automatically
		try (Connection conn = DatabaseManager.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, user.GetName());
			pstmt.setString(2, user.GetEmail());

			String hashedPassword = hashPassword(password);
			pstmt.setString(3, hashedPassword);

			// sends the enum as a lowercase string
			pstmt.setString(4, user.GetRole().name().toLowerCase());

			int rowsInserted = pstmt.executeUpdate();
			return rowsInserted > 0;

		} catch (SQLException e) {
			System.err.println("Database error: " + e.getMessage());
			return false;
		}
	}

	public Optional<User> GetUser(String email) {
		if (ASU.CAIE.config.AppConfig.USE_MOCK_DATA) {
			return ASU.CAIE.util.MockDataProvider.getAllMockUsers().stream()
					.filter(u -> u.getEmail().equalsIgnoreCase(email))
					.findFirst();
		}
		String sql = "SELECT id, name, email, password_hash, role FROM users WHERE email = ?";

		try (Connection conn = DatabaseManager.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, email);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					User user = new User();
					user.SetId(rs.getInt("id"));
					user.SetName(rs.getString("name"));
					user.SetEmail(rs.getString("email"));

					// Convert the DB string back to your Java Enum
					// Assuming your Enum is named UserRole
					String roleStr = rs.getString("role").toUpperCase();
					user.SetRole(Role.valueOf(roleStr));

					return Optional.of(user);
				}
			}
		} catch (SQLException e) {
			System.err.println("Database error while fetching user: " + e.getMessage());
		}

		return Optional.empty();
	}

	public boolean VerifyUserPassword(String email, String plainTextPassword) {
		if (ASU.CAIE.config.AppConfig.USE_MOCK_DATA) {
			return ASU.CAIE.util.MockDataProvider.getAllMockUsers().stream()
					.anyMatch(u -> u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(plainTextPassword));
		}
		String sql = "SELECT password_hash FROM users WHERE email = ?";

		try (Connection conn = DatabaseManager.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, email);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					String storedHash = rs.getString("password_hash");
					return PasswordUtils.verifyPassword(plainTextPassword, storedHash);
				}
			}
		} catch (SQLException e) {
			System.err.println("Database error while verifying password: " + e.getMessage());
		}

		return false;
	}
}
