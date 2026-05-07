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
                        0
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return courses;
    }

	public List<Course> getCoursesByStudent(int studentId) {
		List<Course> courses = new ArrayList<>();
		// SQL Join: Get course details where the student has an entry in the grades table
		String sql = "SELECT c.course_id, c.name, c.professor_id " +
				"FROM courses c " +
				"JOIN student_grades g ON c.course_id = g.course_id " +
				"WHERE g.student_id = ?";

		try (Connection conn = DatabaseManager.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, studentId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				// Mapping the database columns to your Course model[cite: 8, 13]
				Course course = new Course(
						rs.getInt("course_id"),
						rs.getString("name"),
						rs.getInt("professor_id"),
						0      // Placeholder for credits
				);
				courses.add(course);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return courses;
	}

	public void updateCourse(Course course) {
		// 1. Define the SQL queries
		String updateSql = "UPDATE courses SET name = ?, professor_id = ? WHERE course_id = ?";
		String insertSql = "INSERT INTO courses (course_id, name, professor_id) VALUES (?, ?, ?)";

		try (Connection conn = DatabaseManager.getConnection()) {
			// Try to update the record first
			try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
				updateStmt.setString(1, course.getName());
				updateStmt.setInt(2, course.getInstructorId());
				updateStmt.setInt(3, course.getCourseId());

				int rowsAffected = updateStmt.executeUpdate();

				// 2. If no rows were updated, the ID doesn't exist, so we insert
				if (rowsAffected == 0) {
					try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
						insertStmt.setInt(1, course.getCourseId());
						insertStmt.setString(2, course.getName());
						insertStmt.setInt(3, course.getInstructorId());
						insertStmt.executeUpdate();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}