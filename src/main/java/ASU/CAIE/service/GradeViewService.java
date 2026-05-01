package ASU.CAIE.service;

import ASU.CAIE.dao.GradeDAO;
import ASU.CAIE.model.Grade;

import java.util.List;

public class GradeViewService {
    private GradeDAO gradeDAO;

    public GradeViewService() {
        this.gradeDAO = new GradeDAO();
    }

    public List<Grade> getStudentGrades(int studentId) {
        return gradeDAO.getGradesByStudent(studentId);
    }

    public double calculateGPA(List<Grade> grades) {
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
}
