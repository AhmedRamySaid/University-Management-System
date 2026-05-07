package ASU.CAIE.Database.AsyncDao;

import ASU.CAIE.Database.DatabaseManager;
import ASU.CAIE.model.Course;
import ASU.CAIE.model.Grade;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AsyncGradeDao {
	// Async facades for GradeDao methods
	public CompletableFuture<Boolean> submitGrade(Grade grade) {
		return DatabaseManager.runAsync(() -> DatabaseManager.GradeDaoInstance.submitGrade(grade));
	}

	public CompletableFuture<List<Grade>> getGradesForStudent(int studentId) {
		return DatabaseManager.runAsync(() -> DatabaseManager.GradeDaoInstance.getGradesForStudent(studentId));
	}
}
