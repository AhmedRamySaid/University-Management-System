package ASU.CAIE.Database.Dao;

import ASU.CAIE.Database.DatabaseManager;
import ASU.CAIE.model.Grade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GradeDao {

	public boolean saveGrade(Grade grade) {
		// INSERT if no grade exists for this student/course, UPDATE if one does (upsert)
		String sql =
                """
                INSERT INTO student_grades (course_id, student_id, grade)
                VALUES (?, ?, ?)
                ON CONFLICT (course_id, student_id)
                DO UPDATE SET grade = EXCLUDED.grade
                """;

		try (Connection conn = DatabaseManager.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, grade.getCourseId());
			pstmt.setInt(2, grade.getStudentId());
			pstmt.setInt(3, grade.getScore());

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			System.err.println("Database error: " + e.getMessage());
			return false;
		}
	}

	public Grade getGrade(int studentId, int courseId) {
		String sql =
				"""
				SELECT course_id, student_id, grade
				FROM student_grades
				WHERE student_id = ? AND course_id = ?
				""";

		try (Connection conn = DatabaseManager.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, studentId);
			pstmt.setInt(2, courseId);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					Grade grade = new Grade();
					grade.setStudentId(rs.getInt("student_id"));
					grade.setCourseId(rs.getInt("course_id"));
					grade.setScore(rs.getInt("grade"));
					return grade;
				}
			}

		} catch (SQLException e) {
			System.err.println("Database error: " + e.getMessage());
		}

		return null;
	}
}
