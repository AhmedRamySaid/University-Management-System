package ASU.CAIE.util;

import ASU.CAIE.Database.DatabaseManager;
import ASU.CAIE.model.*;

import java.util.List;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(User user) {
        this.currentUser = user;
		if (user instanceof Student) {
			getStudentCourses();
			getStudentGrades();
		}
		if (user.GetRole() == Role.PROFESSOR) getProfessorCourses();
    }

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

	private void getStudentCourses () {
		List<Course> courses = DatabaseManager.CourseDaoInstance.getCoursesByStudent(currentUser.GetID());
		currentUser.SetTakenCourses(courses);
	}

	private void getStudentGrades () {
		List<Grade> grades = DatabaseManager.GradeDaoInstance.getGradesForStudent(currentUser.GetID());
		((Student)currentUser).SetCourseGrades(grades);
	}

	private void getProfessorCourses () {
		List<Course> courses = DatabaseManager.CourseDaoInstance.getCoursesByProfessor(currentUser.GetID());
		currentUser.SetTakenCourses(courses);
	}
}
