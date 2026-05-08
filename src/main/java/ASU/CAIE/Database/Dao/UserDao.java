package ASU.CAIE.Database.Dao;

import ASU.CAIE.Database.DatabaseManager;
import ASU.CAIE.Database.PasswordUtils;
import ASU.CAIE.model.Role;
import ASU.CAIE.model.Student;
import ASU.CAIE.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ASU.CAIE.Database.PasswordUtils.hashPassword;

public class UserDao {

    public boolean createUser(User user, String password) {

        String sql = "INSERT INTO users (name, email, password_hash, role) VALUES (?, ?, ?, ?::user_role)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.GetName());
            pstmt.setString(2, user.GetEmail());

            pstmt.setString(3, hashPassword(password));

            // ✅ DB uses lowercase enum values
            pstmt.setString(4, user.GetRole().name().toLowerCase());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Role safeParseRole(String dbValue) {
        return Role.valueOf(dbValue.trim().toUpperCase());
    }

    public Optional<User> GetUser(int id) {

        String sql = "SELECT user_id, name, email, password_hash, role FROM users WHERE user_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {

                    Role role = safeParseRole(rs.getString("role"));

                    User user = (role == Role.STUDENT) ? new Student() : new User();

                    user.SetID(rs.getInt("user_id"));
                    user.SetName(rs.getString("name"));
                    user.SetEmail(rs.getString("email"));
                    user.SetRole(role);

                    return Optional.of(user);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public Optional<User> GetUser(String email) {

        String sql = "SELECT user_id, name, email, password_hash, role FROM users WHERE email = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {

                    Role role = safeParseRole(rs.getString("role"));

                    User user = (role == Role.STUDENT) ? new Student() : new User();

                    user.SetID(rs.getInt("user_id"));
                    user.SetName(rs.getString("name"));
                    user.SetEmail(rs.getString("email"));
                    user.SetRole(role);

                    return Optional.of(user);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public boolean VerifyUserPassword(String email, String password) {

        String sql = "SELECT password_hash FROM users WHERE email = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    return PasswordUtils.verifyPassword(password, rs.getString("password_hash"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<User> GetAllUsers() {

        List<User> users = new ArrayList<>();

        String sql = "SELECT user_id, name, email, role FROM users";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {

                Role role = safeParseRole(rs.getString("role"));

                User user = (role == Role.STUDENT) ? new Student() : new User();

                user.SetID(rs.getInt("user_id"));
                user.SetName(rs.getString("name"));
                user.SetEmail(rs.getString("email"));
                user.SetRole(role);

                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public boolean UpdateUserRole(int userId, Role role) {

        String sql = "UPDATE users SET role = ?::user_role WHERE user_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, role.name().toLowerCase()); // DB expects lowercase
            pstmt.setInt(2, userId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}