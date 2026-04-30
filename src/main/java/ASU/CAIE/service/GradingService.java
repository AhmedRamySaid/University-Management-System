package ASU.CAIE.service;

import ASU.CAIE.model.Grade;
import ASU.CAIE.dao.GradeDAO;
import java.util.List;

public class GradingService {
    private GradeDAO gradeDAO;

    public GradingService() {
        this.gradeDAO = new GradeDAO();
    }

    public boolean submitGrade(Grade grade) {
        // Business logic: letter grade auto-calculation
        grade.setLetterGrade(calculateLetterGrade(grade.getScore()));
        return gradeDAO.insertGrade(grade);
    }

    public boolean updateGrade(Grade grade) {
        grade.setLetterGrade(calculateLetterGrade(grade.getScore()));
        return gradeDAO.updateGrade(grade);
    }

    public List<Grade> getGradesByCourse(int courseId) {
        return gradeDAO.getGradesByCourse(courseId);
    }

    public String calculateLetterGrade(double score) {
        if (score >= 90) return "A";
        if (score >= 80) return "B";
        if (score >= 70) return "C";
        if (score >= 60) return "D";
        return "F";
    }
}
