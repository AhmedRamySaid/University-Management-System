package ASU.CAIE.Database.Dao;
import ASU.CAIE.Database.DatabaseManager;

import ASU.CAIE.model.Course;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDao {
    // Get courses taught by a specific professor
    public List<Course> getCoursesByProfessor(int professorId) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses WHERE professor_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, professorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // Using "TBD" and 0 for schedule/credits since they aren't in the DB yet
                courses.add(new Course(
                        rs.getInt("course_id"),
                        rs.getString("name"),
                        rs.getInt("professor_id"),
                        "TBD",
                        0
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return courses;
    }

    // Get all courses (for students/admins)
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                courses.add(new Course(
                        rs.getInt("course_id"),
                        rs.getString("name"),
                        rs.getInt("professor_id"),
                        "TBD",
                        0
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return courses;
    }
}