package ASU.CAIE.service;

import ASU.CAIE.config.AppConfig;
import ASU.CAIE.dao.GradeDAO;
import ASU.CAIE.model.Grade;

import java.util.List;

public class PerformanceService {
    private GradeDAO gradeDAO;

    public PerformanceService() {
        this.gradeDAO = new GradeDAO();
    }

    public List<Grade> getGradeTrendByStudent(int studentId) {
        if (AppConfig.USE_MOCK_DATA) {
            return MockDataProvider.getMockGrades().stream()
                    .filter(g -> g.getStudentId() == studentId)
                    .toList();
        }
        return gradeDAO.getGradesByStudent(studentId);
    }
    
    public List<Grade> getCourseAverages(int courseId) {
        if (AppConfig.USE_MOCK_DATA) {
            return MockDataProvider.getMockGrades().stream()
                    .filter(g -> g.getCourseId() == courseId)
                    .toList();
        }
        return gradeDAO.getGradesByCourse(courseId);
    }
}
