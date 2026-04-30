package ASU.CAIE.util;

import ASU.CAIE.model.Course;
import ASU.CAIE.model.Grade;
import ASU.CAIE.model.Parent;
import ASU.CAIE.Users.User;
import ASU.CAIE.Users.Role;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MockDataProvider {

    public static List<User> getAllMockUsers() {
        List<User> all = new ArrayList<>();
        all.addAll(getMockStudents());
        all.addAll(getMockInstructors());
        all.addAll(getMockAdmins());
        all.addAll(getMockStaff());
        return all;
    }

    public static List<User> getMockStudents() {
        List<User> students = new ArrayList<>();
        students.add(new User(1, "Alice Student", "student@university.edu", "password123", Role.STUDENT));
        return students;
    }

    public static List<User> getMockInstructors() {
        List<User> instructors = new ArrayList<>();
        instructors.add(new User(4, "Dr. Smith", "instructor@university.edu", "password123", Role.INSTRUCTOR));
        instructors.add(new User(5, "Prof. Johnson", "professor@university.edu", "password123", Role.PROFESSOR));
        return instructors;
    }

    public static List<User> getMockAdmins() {
        List<User> admins = new ArrayList<>();
        admins.add(new User(6, "System Admin", "admin@university.edu", "password123", Role.ADMIN));
        return admins;
    }

    public static List<User> getMockStaff() {
        List<User> staff = new ArrayList<>();
        staff.add(new User(7, "Registrar Staff", "staff@university.edu", "password123", Role.STAFF));
        return staff;
    }

    public static List<Course> getMockCourses() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(101, "Intro to Computer Science", 4, "Mon/Wed 10:00 AM"));
        courses.add(new Course(102, "Data Structures", 4, "Tue/Thu 1:00 PM"));
        courses.add(new Course(103, "Calculus I", 5, "Mon/Wed/Fri 9:00 AM"));
        courses.add(new Course(104, "Physics I", 5, "Tue/Thu 3:00 PM"));
        courses.add(new Course(105, "Software Engineering", 4, "Mon/Wed 2:00 PM"));
        return courses;
    }

    public static List<Grade> getMockGrades() {
        List<Grade> grades = new ArrayList<>();
        // gradeId, studentId, courseId, instructorId, score, letterGrade, semester, submittedAt
        grades.add(new Grade(1, 1, 101, 4, 95.0, "A", "Fall 2023", LocalDateTime.now().minusDays(10)));
        grades.add(new Grade(2, 1, 102, 4, 88.0, "B", "Fall 2023", LocalDateTime.now().minusDays(12)));
        grades.add(new Grade(3, 2, 101, 4, 75.0, "C", "Fall 2023", LocalDateTime.now().minusDays(5)));
        grades.add(new Grade(4, 2, 103, 5, 92.0, "A", "Fall 2023", LocalDateTime.now().minusDays(8)));
        return grades;
    }
    
    public static Parent getMockParent() {
        return new Parent(10, "Mr. Parent", 1); // linked to Alice (studentId 1)
    }
}
