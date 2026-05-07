package ASU.CAIE.Database.AsyncDao;

import ASU.CAIE.Database.DatabaseManager;
import ASU.CAIE.model.Course;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AsyncCourseDao {
	// Async facades for CourseDao methods
	public CompletableFuture<List<Course>> getCoursesByProfessor(int professorId) {
		return DatabaseManager.runAsync(() -> DatabaseManager.CourseDaoInstance.getCoursesByProfessor(professorId));
	}

	public CompletableFuture<List<Course>> getAllCourses() {
		return DatabaseManager.runAsync(DatabaseManager.CourseDaoInstance::getAllCourses);
	}

	public CompletableFuture<List<Course>> getCoursesByStudent(int studentId) {
		return DatabaseManager.runAsync(() -> DatabaseManager.CourseDaoInstance.getCoursesByStudent(studentId));
	}

	public CompletableFuture<Void> updateCourse(Course course) {
		return DatabaseManager.runAsync(() -> DatabaseManager.CourseDaoInstance.updateCourse(course));
	}
}
