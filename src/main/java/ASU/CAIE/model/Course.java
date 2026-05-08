package ASU.CAIE.model;

import java.util.List;

public class Course {
    private int courseId;
    private String name;
    private int instructorId;
    private int credits;  // new add

    public Course() {}

    public Course(String name, int courseId, int instructorId) {
        this.courseId = courseId;
        this.name = name;
        this.instructorId = instructorId;
    }

    // new add
    public Course(int courseId, String name, int instructorId, int credits) {
        this.courseId = courseId;
        this.name = name;
        this.instructorId = instructorId;
        this.credits = credits;
    }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getInstructorId() { return instructorId; }
    public void setInstructorId(int instructorId) { this.instructorId = instructorId; }

    // new add
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
}