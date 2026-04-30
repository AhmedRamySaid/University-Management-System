package ASU.CAIE.model;

import java.time.LocalDateTime;

public class Grade {
    private int gradeId;
    private int studentId;
    private int courseId;
    private int instructorId;
    private double score;
    private String letterGrade;
    private String semester;
    private LocalDateTime submittedAt;

    public Grade() {}

    public Grade(int gradeId, int studentId, int courseId, int instructorId, double score, String letterGrade, String semester, LocalDateTime submittedAt) {
        this.gradeId = gradeId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.instructorId = instructorId;
        this.score = score;
        this.letterGrade = letterGrade;
        this.semester = semester;
        this.submittedAt = submittedAt;
    }

    public int getGradeId() { return gradeId; }
    public void setGradeId(int gradeId) { this.gradeId = gradeId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public int getInstructorId() { return instructorId; }
    public void setInstructorId(int instructorId) { this.instructorId = instructorId; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public String getLetterGrade() { return letterGrade; }
    public void setLetterGrade(String letterGrade) { this.letterGrade = letterGrade; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
}
