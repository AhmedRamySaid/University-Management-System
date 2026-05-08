package ASU.CAIE.Database.AsyncDao;

import ASU.CAIE.Database.DatabaseManager;
import ASU.CAIE.model.Enrollment;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AsyncEnrollmentDao {
    public CompletableFuture<Boolean> requestEnrollment(int studentId, int courseId) {
        return DatabaseManager.runAsync(() -> DatabaseManager.EnrollmentDaoInstance.requestEnrollment(studentId, courseId));
    }

    public CompletableFuture<List<Enrollment>> getEnrollmentsByStudent(int studentId) {
        return DatabaseManager.runAsync(() -> DatabaseManager.EnrollmentDaoInstance.getEnrollmentsByStudent(studentId));
    }
}
