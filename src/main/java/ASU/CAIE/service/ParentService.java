package ASU.CAIE.service;

import ASU.CAIE.Users.User;
import ASU.CAIE.config.AppConfig;
import ASU.CAIE.dao.GradeDAO;
import ASU.CAIE.model.Grade;
import ASU.CAIE.model.Parent;

import java.util.List;
import java.util.Optional;

public class ParentService {
    private GradeDAO gradeDAO;

    public ParentService() {
        this.gradeDAO = new GradeDAO();
    }

    public Parent getLinkedStudent(int parentId) {
        if (AppConfig.USE_MOCK_DATA) {
            return (Parent) MockDataProvider.getMockParents();
        }
        return null; // Real DB logic would go here
    }

    public String getStudentName(int studentId) {
        if (AppConfig.USE_MOCK_DATA) {
            Optional<User> student = MockDataProvider.getMockStudents().stream()
                    .filter(u -> u.getId() == studentId)
                    .findFirst();
            return student.isPresent() ? student.get().getName() : "Unknown Student";
        }
        return "Student Name"; // Real DB Logic would go here
    }

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

    public List<Grade> getStudentGrades(int studentId) {
        return gradeDAO.getGradesByStudent(studentId);
    }
}
