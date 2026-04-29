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
		String sql = "SELECT name, email, password_hash, role FROM users WHERE email = ?";

		try (Connection conn = DatabaseManager.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, email);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					User user = new User();
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