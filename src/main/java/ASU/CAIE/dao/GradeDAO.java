package ASU.CAIE.dao;

import ASU.CAIE.Database.DatabaseManager;
import ASU.CAIE.config.AppConfig;
import ASU.CAIE.model.Grade;
import ASU.CAIE.util.MockDataProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GradeDAO {

    public boolean insertGrade(Grade grade) {
        if (AppConfig.USE_MOCK_DATA) {
            // Mock mode doesn't persist data between runs in this basic implementation
            MockDataProvider.getMockGrades().add(grade);
            return true;
        }

        String sql = "INSERT INTO grades (student_id, course_id, instructor_id, score, letter_grade, semester, submitted_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, grade.getStudentId());
            pstmt.setInt(2, grade.getCourseId());
            pstmt.setInt(3, grade.getInstructorId());
            pstmt.setDouble(4, grade.getScore());
            pstmt.setString(5, grade.getLetterGrade());
            pstmt.setString(6, grade.getSemester());
            pstmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            return false;
        }
    }

    public boolean updateGrade(Grade grade) {
        if (AppConfig.USE_MOCK_DATA) {
            return true; // Mock mode
        }

        String sql = "UPDATE grades SET score = ?, letter_grade = ?, submitted_at = ? WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, grade.getScore());
            pstmt.setString(2, grade.getLetterGrade());
            pstmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(4, grade.getStudentId());
            pstmt.setInt(5, grade.getCourseId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            return false;
        }
    }

    public List<Grade> getGradesByCourse(int courseId) {
        if (AppConfig.USE_MOCK_DATA) {
            return MockDataProvider.getMockGrades().stream()
                    .filter(g -> g.getCourseId() == courseId)
                    .collect(Collectors.toList());
        }

        List<Grade> grades = new ArrayList<>();
        String sql = "SELECT * FROM grades WHERE course_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    grades.add(mapResultSetToGrade(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return grades;
    }

    public List<Grade> getGradesByStudent(int studentId) {
        if (AppConfig.USE_MOCK_DATA) {
            return MockDataProvider.getMockGrades().stream()
                    .filter(g -> g.getStudentId() == studentId)
                    .collect(Collectors.toList());
        }

        List<Grade> grades = new ArrayList<>();
        String sql = "SELECT * FROM grades WHERE student_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    grades.add(mapResultSetToGrade(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
        return grades;
    }

    private Grade mapResultSetToGrade(ResultSet rs) throws SQLException {
        Grade grade = new Grade();
        grade.setGradeId(rs.getInt("grade_id"));
        grade.setStudentId(rs.getInt("student_id"));
        grade.setCourseId(rs.getInt("course_id"));
        grade.setInstructorId(rs.getInt("instructor_id"));
        grade.setScore(rs.getDouble("score"));
        grade.setLetterGrade(rs.getString("letter_grade"));
        grade.setSemester(rs.getString("semester"));
        
        Timestamp ts = rs.getTimestamp("submitted_at");
        if (ts != null) {
            grade.setSubmittedAt(ts.toLocalDateTime());
        }
        return grade;
    }
}
