package ASU.CAIE.Database.Dao;

import ASU.CAIE.model.Grade;
import ASU.CAIE.Database.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GradeDao {
    // Insert a new grade
    public boolean submitGrade(Grade grade) {
		// INSERT if no grade exists for this student/course, UPDATE if one does (upsert)
		String sql =
				"""
				INSERT INTO student_grades (course_id, student_id, grade)
				VALUES (?, ?, ?)
				ON CONFLICT (course_id, student_id)
				DO UPDATE SET grade = EXCLUDED.grade
				""";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, grade.getCourseId());
            stmt.setInt(2, grade.getStudentId());
            stmt.setDouble(3, grade.getScore());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get grades for a specific student
    public List<Grade> getGradesForStudent(int studentId) {
        List<Grade> grades = new ArrayList<>();
        String sql = "SELECT * FROM student_grades WHERE student_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Grade g = new Grade();
                g.setCourseId(rs.getInt("course_id"));
                g.setStudentId(rs.getInt("student_id"));
                g.setScore(rs.getDouble("grade"));


                grades.add(g);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return grades;
    }
}