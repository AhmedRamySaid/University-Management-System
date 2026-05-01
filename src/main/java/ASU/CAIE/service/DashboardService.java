package ASU.CAIE.service;

import ASU.CAIE.config.AppConfig;
import ASU.CAIE.dao.GradeDAO;
import ASU.CAIE.model.Course;
import ASU.CAIE.model.Grade;

import java.util.List;

public class DashboardService {
    private GradeDAO gradeDAO;

    public DashboardService() {
        this.gradeDAO = new GradeDAO();
    }

    // Returns a list of courses a student is enrolled in
    public List<Course> getStudentCourses(int studentId) {
        if (AppConfig.USE_MOCK_DATA) {
            // Mock mode: assume student 1 is enrolled in first 3 courses
            List<Course> mockCourses = MockDataProvider.getMockCourses();
            return mockCourses.subList(0, Math.min(3, mockCourses.size()));
        }
        // DB Implementation: would require a CourseDAO and Enrollment table
        return List.of(); 
    }

    // Returns a list of courses an instructor teaches
    public List<Course> getInstructorCourses(int instructorId) {
        if (AppConfig.USE_MOCK_DATA) {
            // Mock mode: return courses that match instructor ID
            return MockDataProvider.getMockCourses().stream()
                    .filter(c -> c.getInstructorId() == instructorId)
                    .toList();
        }
        return List.of();
    }

    // Calculates GPA based on 4.0 scale
    public double calculateGPA(int studentId) {
        List<Grade> grades = gradeDAO.getGradesByStudent(studentId);
        if (grades.isEmpty()) return 0.0;

        double totalPoints = 0;
        for (Grade g : grades) {
            String letter = g.getLetterGrade();
            if ("A".equals(letter)) totalPoints += 4.0;
            else if ("B".equals(letter)) totalPoints += 3.0;
            else if ("C".equals(letter)) totalPoints += 2.0;
            else if ("D".equals(letter)) totalPoints += 1.0;
        }
        return totalPoints / grades.size();
    }

    public int getPendingGradingCount(int instructorId) {
        if (AppConfig.USE_MOCK_DATA) {
            return 5; // Placeholder for mock mode
        }
        return 0; // DB Implementation would query students enrolled in instructor's courses without grades
    }
}
